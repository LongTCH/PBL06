package com.clothes.service.impl;

import com.clothes.model.Group;
import com.clothes.repository.GroupsRepository;
import com.clothes.service.GroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupsServiceImpl implements GroupsService {

    @Autowired
    private GroupsRepository groupsRepository;


    @Override
    public List<String> getGroupIdByNames(List<String> groupNames) {

        List<Group> groups = groupsRepository.findByNameIn(groupNames);
        return groups.stream()
                .map(Group::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> getAllGroups() {
        List<Group> groups = groupsRepository.findAll();
        return groups;
    }

}
