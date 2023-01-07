package com.weather.repository.service.mq.listener;

import com.weather.repository.dto.WeatherDto;
import com.weather.repository.service.mq.processor.WeatherProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncomingWeatherListener {
    private final Jackson2JsonMessageConverter messageConverter;
    private final WeatherProcessor weatherProcessor;

    @RabbitListener(
            bindings = @QueueBinding(
                    exchange = @Exchange("${mq.weather.capture.queue}"),
                    value = @Queue("${mq.weather.capture.queue}")
            ),
            containerFactory = "weatherFactory"
    )
    public void onIncomingWork(Message message) {
        processMessage(message);
    }


    private void processMessage(Message message) {
        try {
            WeatherDto weatherDto = (WeatherDto) messageConverter.fromMessage(
                    message, ParameterizedTypeReference.forType(WeatherDto.class)
            );
            weatherProcessor.processWeather(weatherDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
