package io.worker.service;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import io.worker.dto.AddWorkerRequestBody;
import io.worker.dto.CrackHashTaskResponseBody;
import io.worker.dto.CrackHashTaskRequestBody;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

import static org.paukov.combinatorics.CombinatoricsFactory.createPermutationWithRepetitionGenerator;
import static org.paukov.combinatorics.CombinatoricsFactory.createVector;

@Service
@Slf4j
public class CrackHashService {
    private List<String> words;
    private final MessageDigest md;

    @Value("${spring.hash.generated-combinations-count}")
    private int generateCount;
    private String[] alphabet;
    private final RestClient restClient = RestClient.create();

    @Value("${spring.manager.uri}")
    private String managerURI;
    @Value("${spring.manager.port}")
    private String managerPort;
    @Value("${spring.manager.api-path}")
    private String managerApiPath;
    @Value("${spring.manager.endpoints.crack-result}")
    private String managerCrackResultEndpoint;
    @Value("${spring.manager.endpoints.workers}")
    private String managerWorkerEndpoint;

    @Autowired
    public CrackHashService(@Value("${spring.hash.alphabet}") String alphabet,
                            @Value("${spring.hash.algorithm}") String hashAlgorithm) throws NoSuchAlgorithmException, UnknownHostException {
        this.alphabet = alphabet.split("");
        this.md = MessageDigest.getInstance(hashAlgorithm);
    }

    @PostConstruct
    private void informManager() throws UnknownHostException {
        AddWorkerRequestBody addWorkerRequestBody = new AddWorkerRequestBody(getHostName());

        log.info("Worker uri is: {}, informing manager",addWorkerRequestBody.getWorkerURI());

        var response = restClient.post()
                .uri("http://" + managerURI + ":" + managerPort + managerApiPath + managerWorkerEndpoint)
                .body(addWorkerRequestBody)
                .retrieve()
                .toEntity(String.class);

        log.info("Manager add worker request sent. Manager response: {}",response);
    }

    private String getHostName() throws UnknownHostException {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder().dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
        String hostname = InetAddress.getLocalHost().getHostName();
        List<Container> containers = dockerClient.listContainersCmd().exec();
        for(var c : containers){
            if(c.getId().substring(0,12).equals(hostname)){
                return c.getNames()[0].substring(1);
            }
        }
        return null;
    }

    @Async
    public void crackHash(CrackHashTaskRequestBody crackHashTaskRequestBody){
        log.info("Task started");
        words = new ArrayList<>();
        for (int i = 1; i <= crackHashTaskRequestBody.getMaxLength(); i++){
            System.out.println(i);
            crackHashFixedLength(i, crackHashTaskRequestBody.getPartNumber(),
                    crackHashTaskRequestBody.getPartCount(), crackHashTaskRequestBody.getHash());
        }
        CrackHashTaskResponseBody crackHashTaskResponseBody = new CrackHashTaskResponseBody(crackHashTaskRequestBody.getRequestId(), words, crackHashTaskRequestBody.getTaskId());
        var response = restClient.post()
                .uri("http://" + managerURI + ":" + managerPort + managerApiPath + managerCrackResultEndpoint)
                .body(crackHashTaskResponseBody)
                .retrieve()
                .toEntity(String.class);
        log.info("Task completed and sent to manager. Manager response: {}",response);
    }

    private void crackHashFixedLength(int length, int partNumber, int partCount, String hash){
        ICombinatoricsVector<String> vector = createVector(alphabet);
        Generator<String> gen = createPermutationWithRepetitionGenerator(vector, length);

        long totalNumber = (long)Math.pow(vector.getSize(), length);
        long numberToCompute = (long) Math.ceil( (double) totalNumber / partCount);
        long start = numberToCompute*(partNumber-1);
        long i = start+generateCount;
        for(; i < start+numberToCompute; i+=generateCount){
            crackHashForGeneratedCombinations(gen.generateObjectsRange(i-generateCount+1, i),hash);
        }
        log.info("Cracking hash for words length: {}, total number of combinations: {} number to be computed by worker: {} start: {}",
                length, totalNumber, numberToCompute, start);
        crackHashForGeneratedCombinations(gen.generateObjectsRange(i-generateCount+1, start+numberToCompute),hash);
    }

    private void crackHashForGeneratedCombinations(List<ICombinatoricsVector<String>> combinationsList, String hash){
        for(var combination : combinationsList){
            String word = String.join("", combination.getVector());
            String calculatedHash = HexFormat.of().formatHex(md.digest(word.getBytes(StandardCharsets.UTF_8)));
            if(calculatedHash.equals(hash)){
                words.add(word);
            }
        }
    }
}
