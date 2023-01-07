package com.weather.repository.service.mq.processor.impl;

import com.weather.repository.converter.WeatherConverter;
import com.weather.repository.dto.WeatherDto;
import com.weather.repository.service.mq.processor.WeatherProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static com.weather.repository.constant.Constant.WEATHER_PAYLOAD_IS_EMPTY_MESSAGE;

@Service
@RequiredArgsConstructor
public class WeatherProcessorImpl implements WeatherProcessor {
    private MongoTemplate mongoTemplate;
    private WeatherConverter weatherConverter;

    @Autowired
    private WeatherProcessorImpl(MongoTemplate mongoTemplate, WeatherConverter weatherConverter) {
        this.mongoTemplate = mongoTemplate;
        this.weatherConverter = weatherConverter;
    }

    @Override
    public void processWeather(WeatherDto weatherDto) {
        Assert.notNull(weatherDto, WEATHER_PAYLOAD_IS_EMPTY_MESSAGE);
        mongoTemplate.save(weatherConverter.dtoToEntity(weatherDto));
    }
}
