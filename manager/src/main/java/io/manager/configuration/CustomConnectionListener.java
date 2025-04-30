package io.manager.configuration;

import io.manager.dto.CrackHashTaskRequestBody;
import io.manager.entity.TaskEntity;
import io.manager.events.RabbitMQStartEvent;
import io.manager.repository.TaskRepository;
import io.manager.service.mapper.TaskMapper;
import io.manager.service.workerspool.WorkersPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomConnectionListener implements ConnectionListener {
    private final ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void onCreate(Connection connection) {
        log.info("Rabbitmq connection created");
        applicationEventPublisher.publishEvent(new RabbitMQStartEvent(this,"Rabbit started"));
    }
}
