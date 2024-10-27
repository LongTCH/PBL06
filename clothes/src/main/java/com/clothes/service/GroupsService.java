package com.clothes.service;

import com.clothes.model.Group;

import java.util.List;

public interface GroupsService {

    List<String> getGroupIdByNames(List<String> groupNames);

    List<Group> getAllGroups();

    boolean existsById(String groupId);
}