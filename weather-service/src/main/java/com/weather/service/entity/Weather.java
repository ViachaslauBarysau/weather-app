package com.weather.service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = "weather")
public class Weather {
    @Id
    private String id;
    private String city;
    private String weatherType;
    private Instant dateTime;
}
