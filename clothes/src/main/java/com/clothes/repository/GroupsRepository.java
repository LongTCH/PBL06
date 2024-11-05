package com.clothes.repository;

import com.clothes.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GroupsRepository extends MongoRepository<Group, String> {

    List<Group> findByNameIn(List<String> groupNames);

    Optional<Group> findById(String groupId);
}
