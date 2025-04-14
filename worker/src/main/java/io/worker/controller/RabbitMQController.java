package io.worker.controller;

import io.worker.dto.CrackHashTaskRequestBody;
import io.worker.service.CrackHashService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RabbitMQController {
    private final CrackHashService crackHashService;

    @RabbitListener(queues = "queue.Tasks")
    private void receiveTask(CrackHashTaskRequestBody crackHashTaskRequestBody) {
        log.info("Task started: {}", crackHashTaskRequestBody);
        crackHashService.crackHash(crackHashTaskRequestBody);
        log.info("Task completed");
    }
}
