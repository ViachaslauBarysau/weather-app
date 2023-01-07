package com.weather.service.repository;

import com.weather.service.entity.Weather;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface WeatherRepository {
    List<Weather> getWeather(Query query);
}
