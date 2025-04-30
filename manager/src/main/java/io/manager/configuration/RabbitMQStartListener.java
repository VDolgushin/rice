package io.manager.configuration;

import io.manager.events.RabbitMQStartEvent;
import io.manager.repository.TaskRepository;
import io.manager.service.workerspool.WorkersPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQStartListener implements ApplicationListener<RabbitMQStartEvent> {
    private final TaskRepository taskRepository;
    private final WorkersPool workersPool;

    @Override
    public void onApplicationEvent(RabbitMQStartEvent event) {
        log.info("Sending pending tasks from database");
        for(var t : taskRepository.findAll()){
            workersPool.addTask(t);
            taskRepository.delete(t);
        }
    }
}