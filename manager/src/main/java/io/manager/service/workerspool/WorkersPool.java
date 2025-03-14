package io.manager.service.workerspool;

import io.manager.dto.CrackHashTaskRequestBody;
import io.manager.entity.TaskEntity;
import io.manager.service.mapper.TaskMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
public class WorkersPool {
    @Getter
    private final int workersCount;

    @Value("${spring.request.expiration-time}")
    private long requestExpirationTime;

    private final HashMap<String, WorkerStatus> workers = new HashMap<>();
    private final RestClient restClient = RestClient.create();

    @Autowired
    private TaskMapper taskMapper;

    ConcurrentLinkedQueue<TaskEntity> tasksQuery = new ConcurrentLinkedQueue<>();

    @Autowired
    public WorkersPool(@Value("${spring.worker.workers-count}") int workersCount,
                       @Value("${spring.worker.worker-service-domain-name}") String workerServiceName) {
        this.workersCount = workersCount;
        setWorkersURIs(workerServiceName);
    }

    private void setWorkersURIs(String workerServiceName) {
        for (int i = 1; i <= workersCount; i++) {
            workers.put(workerServiceName + "-" + i, WorkerStatus.IDLE);
        }
    }

    public void addRequest(TaskEntity taskEntity) {
        tasksQuery.add(taskEntity);
    }

    @Scheduled(fixedDelay = 1000)
    private void executeTasks() {
        while (tasksQuery.peek() != null && (tasksQuery.peek().getCreatedAt() + requestExpirationTime) < new Date().getTime()) {
            tasksQuery.poll();
        }
        for (String uri : workers.keySet()) {
            if (workers.get(uri).equals(WorkerStatus.IDLE)) {
                postTaskToWorker(uri, tasksQuery.poll());
            }
        }
    }

    private void postTaskToWorker(String workerURI, TaskEntity taskEntity) {
        if (taskEntity == null) {
            return;
        }
        workers.put(workerURI, WorkerStatus.WORKING);
        CrackHashTaskRequestBody crackHashTaskRequestBody = new CrackHashTaskRequestBody();
        taskMapper.toModel(taskEntity, crackHashTaskRequestBody);
        crackHashTaskRequestBody.setTaskId(workerURI);
        System.out.println(workerURI + "/internal/api/worker/hash" + "/crack/task");
        var response = restClient.post()
                .uri("http://" + workerURI + ":8000/internal/api/worker/hash" + "/crack/task")
                .body(crackHashTaskRequestBody)
                .retrieve()
                .toEntity(String.class);
        System.out.println(response.getBody());
    }

    public void completeTask(String workerURI) {
        workers.put(workerURI, WorkerStatus.IDLE);
    }
}
