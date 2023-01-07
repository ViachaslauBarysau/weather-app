package com.weather.service.service;

import com.weather.service.dto.WeatherDto;

import java.util.List;

public interface WeatherService {
    void captureWeather(String city);
    List<WeatherDto> getWeather(String searchedValue, int pageSize, int pageNumber);
}
