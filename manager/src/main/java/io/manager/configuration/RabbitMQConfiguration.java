package io.manager.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RabbitMQConfiguration {
    @Value("${spring..rabbitmq.port}")
    private int rabbitPort;
    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;
    @Value("${spring.rabbitmq.username}")
    private String rabbitUsername;
    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;

    private final CustomConnectionListener connectionListener;

    @Bean
    Queue queueTasks() {
        return new Queue("queue.Tasks", true);
    }

    @Bean
    Queue queueResults() {
        return new Queue("queue.Results", true);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("exchange.direct",true,false);
    }

    @Bean
    Binding bindingTasks(Queue queueTasks, DirectExchange exchange) {
        return BindingBuilder.bind(queueTasks).to(exchange).with("Tasks");
    }

    @Bean
    Binding bindingResults(Queue queueResults, DirectExchange exchange) {
        return BindingBuilder.bind(queueResults).to(exchange).with("Results");
    }

    @Bean
    ApplicationRunner runner(ConnectionFactory cf) {
        return args -> cf.createConnection().close();
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitHost, rabbitPort);
        connectionFactory.setUsername(rabbitUsername);
        connectionFactory.setPassword(rabbitPassword);
        connectionFactory.addConnectionListener(connectionListener);
        return connectionFactory;
    }
}
