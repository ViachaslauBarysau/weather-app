package com.weather.repository.service.mq.processor;

import com.weather.repository.dto.WeatherDto;

public interface WeatherProcessor {
    void processWeather(WeatherDto weatherDto);
}
