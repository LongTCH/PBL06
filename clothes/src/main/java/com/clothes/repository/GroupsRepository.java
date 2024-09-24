package com.clothes.repository;

import com.clothes.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GroupsRepository extends MongoRepository<Group, String> {

    List<Group> findByNameIn(List<String> groupNames);
}
