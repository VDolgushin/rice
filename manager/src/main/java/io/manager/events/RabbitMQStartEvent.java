package io.manager.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RabbitMQStartEvent extends ApplicationEvent {
    private final String message;
    public RabbitMQStartEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
}
