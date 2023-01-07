package com.weather.service.repository.impl;

import com.weather.service.entity.Weather;
import com.weather.service.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WeatherRepositoryImpl implements WeatherRepository {
    private MongoOperations mongoOperations;
    @Autowired
    public WeatherRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public List<Weather> getWeather(Query query) {
        return mongoOperations.find(query, Weather.class);
    }
}
