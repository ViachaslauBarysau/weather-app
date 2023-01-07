package com.weather.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDataDto {
    @JsonProperty("name")
    private String city;
    @JsonProperty("weather")
    private List<WeatherDataPayloadDto> weather;
}
