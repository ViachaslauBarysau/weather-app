package com.weather.service.dto;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class WeatherDto {
    private String id;
    private String city;
    private String weatherType;
    private Instant dateTime;
}
