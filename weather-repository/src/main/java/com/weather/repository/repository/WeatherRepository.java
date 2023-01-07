package com.weather.repository.repository;

import com.weather.repository.entity.Weather;
import org.springframework.data.repository.CrudRepository;

interface WeatherRepository extends CrudRepository<Weather, Long> {

}
