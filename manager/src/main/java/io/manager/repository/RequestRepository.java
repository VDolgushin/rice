package io.manager.repository;

import io.manager.dto.RequestStatus;
import io.manager.entity.RequestEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public interface RequestRepository extends MongoRepository<RequestEntity, String> {
}
