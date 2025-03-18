package io.manager.service.workerspool;

import io.manager.dto.CrackHashTaskRequestBody;
import io.manager.dto.HealthResponse;
import io.manager.entity.TaskEntity;
import io.manager.service.mapper.TaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
public class WorkersPool {

    @Value("${spring.worker.port}")
    private String workerPort;
    @Value("${spring.worker.api-path}")
    private String workerApiPath;
    @Value("${spring.worker.endpoints.task}")
    private String workerTaskEndpoint;

    private final ConcurrentHashMap<String, WorkerInfo> workers = new ConcurrentHashMap<>();
    private final RestClient restClient = RestClient.create();

    @Autowired
    private TaskMapper taskMapper;

    ConcurrentLinkedQueue<TaskEntity> tasksQuery = new ConcurrentLinkedQueue<>();

    public void addRequest(TaskEntity taskEntity) {
        tasksQuery.add(taskEntity);
    }

    public void addWorker(String workerURI){
        System.out.println(workerURI);
        workers.put(workerURI, new WorkerInfo(WorkerStatus.IDLE, null));
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

    private void removeWorker(String workerURI){
        workers.remove(workerURI);
        var task = workers.get(workerURI).currentTask;
        if(task != null){
            tasksQuery.add(task);
        }
    }

    private void postTaskToWorker(String workerURI, TaskEntity taskEntity) {
        if (taskEntity == null) {
            return;
        }
        workers.put(workerURI, new WorkerInfo(WorkerStatus.WORKING, taskEntity));
        CrackHashTaskRequestBody crackHashTaskRequestBody = new CrackHashTaskRequestBody();
        taskMapper.toModel(taskEntity, crackHashTaskRequestBody);
        crackHashTaskRequestBody.setTaskId(workerURI);
        var response = restClient.post()
                .uri("http://" + workerURI + ":" + workerPort + workerApiPath + workerTaskEndpoint)
                .body(crackHashTaskRequestBody)
                .retrieve()
                .toEntity(String.class);
        log.info("Task: {} sent to worker: {}. Worker response: {}",taskEntity, workerURI, response);
    }

    public void completeTask(String workerURI) {
        workers.put(workerURI, new WorkerInfo(WorkerStatus.IDLE, null));
    }
}
