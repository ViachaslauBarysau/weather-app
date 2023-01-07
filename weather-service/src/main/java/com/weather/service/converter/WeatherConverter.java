package com.weather.service.converter;

import com.weather.service.dto.WeatherDto;
import com.weather.service.entity.Weather;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeatherConverter {
    public WeatherDto entityToDto(Weather address) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(address, WeatherDto.class);
    }

    public Weather dtoToEntity(WeatherDto addressDto) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(addressDto, Weather.class);
    }

    public List<WeatherDto> entityToDto(List<Weather> addresses) {
        return addresses.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public List<Weather> dtoToEntity(List<WeatherDto> addressDtos) {
        return addressDtos.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }
}
