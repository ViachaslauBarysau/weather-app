package com.weather.service.service.impl;

import com.weather.service.converter.WeatherConverter;
import com.weather.service.dto.CityDataDto;
import com.weather.service.dto.WeatherDataDto;
import com.weather.service.dto.WeatherDto;
import com.weather.service.entity.Weather;
import com.weather.service.repository.WeatherRepository;
import com.weather.service.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static com.weather.service.constant.Constants.*;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    @Value("${mq.weather.capture.queue}")
    private String workCaptureQueueName;
    @Value("${api.url.weather}")
    private String apiWeatherUrl;
    @Value("${api.url.converter}")
    private String apiConverterUrl;
    @Value("${api.key}")
    private String apiKey;
    private MessageConverter messageConverter;
    private RabbitTemplate rabbitTemplate;
    private WeatherRepository weatherRepository;
    private WeatherConverter weatherConverter;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public WeatherServiceImpl(MessageConverter messageConverter, RabbitTemplate rabbitTemplate,
                              WeatherRepository weatherRepository, WeatherConverter weatherConverter) {
        this.messageConverter = messageConverter;
        this.rabbitTemplate = rabbitTemplate;
        this.weatherRepository = weatherRepository;
        this.weatherConverter = weatherConverter;
    }

    @Override
    public List<WeatherDto> getWeather(String searchedValue, int pageSize, int pageNumber) {
        Criteria cityCriteria = Criteria.where(CITY_FIELD).regex(searchedValue, I_OPTION);
        Criteria weatherCriteria = Criteria.where(WEATHER_TYPE_FIELD).regex(searchedValue, I_OPTION);
        Query query = new Query(new Criteria().orOperator(cityCriteria, weatherCriteria));
        query.limit(pageSize);
        query.skip(pageNumber == 0 ? pageNumber : (pageNumber - 1) * pageSize);
        List<Weather> weatherList = weatherRepository.getWeather(query);
        return weatherConverter.entityToDto(weatherList);
    }

    @Override
    public void captureWeather(String city) {
        List<CityDataDto> cityDataDtoList = getCityDataDtoList(city);
        WeatherDataDto weatherDataDto = getWeatherDataDto(cityDataDtoList);
        WeatherDto weatherDto = new WeatherDto();
        weatherDto.setCity(city);
        weatherDto.setWeatherType(weatherDataDto.getWeather().get(0).getWeatherType());
        weatherDto.setDateTime(Instant.now());
        rabbitTemplate.convertAndSend(workCaptureQueueName, getConvertedMessage(weatherDto));
    }

    private WeatherDataDto getWeatherDataDto(List<CityDataDto> cityDataDtoList) {
        Assert.notEmpty(cityDataDtoList, CITY_IS_NOT_FOUND_MESSAGE);
        Double lat = cityDataDtoList.get(0).getLat();
        Double lon = cityDataDtoList.get(0).getLon();
        String weatherUrl = String.format(apiWeatherUrl, lat, lon, apiKey);
        WeatherDataDto weatherDataDto = restTemplate.getForObject(weatherUrl, WeatherDataDto.class);
        return weatherDataDto;
    }

    private List<CityDataDto> getCityDataDtoList(String city) {
        String converterUrl = String.format(apiConverterUrl, city, apiKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<List<CityDataDto>> responseEntity = restTemplate.exchange(converterUrl, HttpMethod.GET,
                requestEntity, new ParameterizedTypeReference<List<CityDataDto>>() {
                });

        List<CityDataDto> cityDataDtoList = responseEntity.getBody();
        return cityDataDtoList;
    }

    private Message getConvertedMessage(WeatherDto weatherDto) {
        Message message = messageConverter.toMessage(weatherDto, new MessageProperties());
        message.getMessageProperties().getHeaders().remove(TYPE_ID_KEY);
        return message;
    }
}
