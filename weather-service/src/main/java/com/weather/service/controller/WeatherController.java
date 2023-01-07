package com.weather.service.controller;

import com.weather.service.dto.WeatherDto;
import com.weather.service.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/weather")
@RequiredArgsConstructor
@Slf4j
public class WeatherController {
    private WeatherService weatherService;
    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity saveWeather(@RequestBody String city) {
        weatherService.captureWeather(city);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<WeatherDto>> getWeather(@RequestParam(defaultValue="") String searchedValue,
                                                       @RequestParam(defaultValue="0") int pageSize,
                                                       @RequestParam(defaultValue="0") int pageNumber) {
        List<WeatherDto> list = weatherService.getWeather(searchedValue, pageSize, pageNumber);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
