package io.worker.service;

import io.worker.dto.CrackHashTaskResponseBody;
import io.worker.dto.CrackHashTaskRequestBody;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;

import static org.paukov.combinatorics.CombinatoricsFactory.createPermutationWithRepetitionGenerator;
import static org.paukov.combinatorics.CombinatoricsFactory.createVector;

@Service
public class CrackHashService {
    private List<String> words;
    private final MessageDigest md;

    @Value("${spring.hash.generated-combinations-count}")
    private int generateCount;
    private String[] alphabet;
    private final RestClient restClient = RestClient.create();

    @Value("${spring.manager.uri}")
    private String managerURI;

    @Autowired
    public CrackHashService(@Value("${spring.hash.alphabet}") String alphabet,
                            @Value("${spring.hash.algorithm}") String hashAlgorithm) throws NoSuchAlgorithmException {
        this.alphabet = alphabet.split("");
        this.md = MessageDigest.getInstance(hashAlgorithm);
    }

    @Async
    public void crackHash(CrackHashTaskRequestBody crackHashTaskRequestBody){
        words = new ArrayList<>();
        for (int i = 1; i <= crackHashTaskRequestBody.getMaxLength(); i++){
            System.out.println(i);
            crackHashFixedLength(i, crackHashTaskRequestBody.getPartNumber(),
                    crackHashTaskRequestBody.getPartCount(), crackHashTaskRequestBody.getHash());
        }
        CrackHashTaskResponseBody crackHashTaskResponseBody = new CrackHashTaskResponseBody(crackHashTaskRequestBody.getRequestId(), words, crackHashTaskRequestBody.getTaskId());
        System.out.println("BOBER");
        var response = restClient.post()
                .uri("http://" + managerURI + ":8000/internal/api/manager/hash" + "/crack/request")
                .body(crackHashTaskResponseBody)
                .retrieve()
                .toEntity(String.class);
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
        System.out.println("total number: " + totalNumber + " number to compute: " + numberToCompute
        + " start: " + start + " i: " + i + " generate count: " + generateCount);
        crackHashForGeneratedCombinations(gen.generateObjectsRange(i-generateCount+1, start+numberToCompute),hash);
    }

    private void crackHashForGeneratedCombinations(List<ICombinatoricsVector<String>> combinationsList, String hash){
        for(var combination : combinationsList){
            String word = String.join("", combination.getVector());
            if(word.equals("abcd")){
                System.out.println("ABOBA");
                System.out.println(HexFormat.of().formatHex(md.digest(word.getBytes(StandardCharsets.UTF_8))));
                System.out.println(hash);
            }
            String calculatedHash = HexFormat.of().formatHex(md.digest(word.getBytes(StandardCharsets.UTF_8)));
            if(calculatedHash.equals(hash)){
                System.out.println("ABOBAABOBAABOBA");
                words.add(word);
            }
        }
    }
}
