package com.weather.repository.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConfigurationProperties("spring.rabbitmq")
public class DefaultRabbitConfig extends GeneralRabbitConfig {
    @Bean(name = "weatherConnectionFactory")
    @Primary
    public ConnectionFactory getConnectionFactory() {
        return super.connectionFactory();
    }

    @Bean(name = "weatherRabbitTemplate")
    @Primary
    public RabbitTemplate getRabbitTemplate(@Qualifier("weatherConnectionFactory") ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean(name = "weatherFactory")
    public SimpleRabbitListenerContainerFactory getRabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("weatherConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean(name = "weatherRabbitAdmin")
    public RabbitAdmin firstRabbitAdmin(@Qualifier("weatherConnectionFactory")ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
