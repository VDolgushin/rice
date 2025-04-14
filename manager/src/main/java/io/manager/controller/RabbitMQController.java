package io.manager.controller;

import io.manager.dto.CrackHashTaskResponseBody;
import io.manager.exception.RequestNotFoundException;
import io.manager.service.CrackHashService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RabbitMQController {
    private final CrackHashService crackHashService;

    @RabbitListener(queues = "queue.Results")
    private void receiveTask(CrackHashTaskResponseBody crackHashTaskResponseBody) throws RequestNotFoundException {
        log.info("Task completed: {}", crackHashTaskResponseBody);
        System.out.println(crackHashService);
        crackHashService.completeTask(crackHashTaskResponseBody);
        log.info("Task completed and sent to manager");
    }
}
