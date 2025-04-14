package io.manager.service.workerspool;

import io.manager.dto.CrackHashTaskRequestBody;
import io.manager.dto.HealthResponse;
import io.manager.entity.TaskEntity;
import io.manager.service.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkersPool {

    @Value("${spring.worker.port}")
    private String workerPort;
    @Value("${spring.worker.api-path}")
    private String workerApiPath;
    @Value("${spring.worker.endpoints.task}")
    private String workerTaskEndpoint;

    private final ConcurrentHashMap<String, WorkerInfo> workers = new ConcurrentHashMap<>();
    private final RestClient restClient = RestClient.create();
    private final TaskMapper taskMapper;
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;

    ConcurrentLinkedQueue<TaskEntity> tasksQuery = new ConcurrentLinkedQueue<>();

    public void addRequest(TaskEntity taskEntity) {
        tasksQuery.add(taskEntity);
    }

    public void addWorker(String workerURI){
        workers.put(workerURI, new WorkerInfo(WorkerStatus.IDLE, null));
        log.info("Worker: {} is added to the pool", workerURI);
    }

    public int getWorkersCount(){
        return workers.size();
    }

    @Scheduled(fixedDelay = 1000)
    private void executeTasks() {
        for (String uri : workers.keySet()) {
            if (workers.get(uri).getWorkerStatus().equals(WorkerStatus.IDLE)) {
                postTaskToWorker(uri, tasksQuery.poll());
            }
        }
    }

    @Scheduled(fixedDelay = 30000)
    private void healthCheck() {
        for (String uri : workers.keySet()) {
            try {
                var response = restClient.get()
                        .uri("http://" + uri + ":" + workerPort + "/actuator/health")
                        .retrieve()
                        .toEntity(HealthResponse.class);
                if(response.getStatusCode().isError() || !response.hasBody() || !response.getBody().getStatus().equals("UP")){
                    removeWorker(uri);
                }
            }
            catch (Exception ex){
                removeWorker(uri);
            }
        }
    }

    private void removeWorker(String workerURI){workers.remove(workerURI);
        log.info("Worker: {} is unavailable and removed from the pool", workerURI);
    }

    private void postTaskToWorker(String workerURI, TaskEntity taskEntity) {
        if (taskEntity == null) {
            return;
        }
        workers.put(workerURI, new WorkerInfo(WorkerStatus.WORKING, taskEntity));
        CrackHashTaskRequestBody crackHashTaskRequestBody = new CrackHashTaskRequestBody();
        taskMapper.toModel(taskEntity, crackHashTaskRequestBody);
        crackHashTaskRequestBody.setTaskId(workerURI);

        rabbitTemplate.convertAndSend(directExchange.getName(), "Tasks",crackHashTaskRequestBody);

        log.info("Task: {} sent to worker: {}",taskEntity, workerURI);
    }

    public void completeTask(String workerURI) {
        workers.put(workerURI, new WorkerInfo(WorkerStatus.IDLE, null));
    }
}
