package com.clothes.service;

import com.clothes.model.Group;
import com.clothes.repository.GroupsRepository;
import com.clothes.service.impl.GroupsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupServiceTest {

    @Mock
    private GroupsRepository groupsRepository;

    @InjectMocks
    private GroupsServiceImpl groupsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetGroupIdByNames() {
        Group group1 = new Group("1", "Group1");
        Group group2 = new Group("2", "Group2");
        List<String> groupNames = Arrays.asList("Group1", "Group2");
        when(groupsRepository.findByNameIn(groupNames)).thenReturn(Arrays.asList(group1, group2));

        List<String> groupIds = groupsService.getGroupIdByNames(groupNames);

        assertNotNull(groupIds);
        assertEquals(2, groupIds.size());
        assertTrue(groupIds.contains("1"));
        assertTrue(groupIds.contains("2"));

        verify(groupsRepository, times(1)).findByNameIn(groupNames);
    }

    @Test
    void testGetAllGroups() {
        Group group1 = new Group("1", "Group1");
        Group group2 = new Group("2", "Group2");
        when(groupsRepository.findAll()).thenReturn(Arrays.asList(group1, group2));

        List<Group> groups = groupsService.getAllGroups();

        assertNotNull(groups);
        assertEquals(2, groups.size());
        assertTrue(groups.stream().anyMatch(group -> group.getId().equals("1")));
        assertTrue(groups.stream().anyMatch(group -> group.getId().equals("2")));

        verify(groupsRepository, times(1)).findAll();
    }

    @Test
    void testExistsById() {
        String groupId = "1";
        when(groupsRepository.existsById(groupId)).thenReturn(true);

        boolean exists = groupsService.existsById(groupId);

        assertTrue(exists);

        verify(groupsRepository, times(1)).existsById(groupId);
    }

    @Test
    void testGetGroupById() {
        String groupId = "1";
        Group group = new Group(groupId, "Group1");
        when(groupsRepository.findById(groupId)).thenReturn(Optional.of(group));

        Group foundGroup = groupsService.getGroupById(groupId);

        assertNotNull(foundGroup);
        assertEquals(groupId, foundGroup.getId());
        assertEquals("Group1", foundGroup.getName());

        verify(groupsRepository, times(1)).findById(groupId);
    }

    @Test
    void testGetGroupNameById() {
        String groupId = "1";
        Group group = new Group(groupId, "Group1");
        when(groupsRepository.findById(groupId)).thenReturn(Optional.of(group));

        String groupName = groupsService.getGroupNameById(groupId);

        assertEquals("Group1", groupName);

        verify(groupsRepository, times(1)).findById(groupId);
    }

    @Test
    void testGetGroupNameByIdWhenNotFound() {
        String groupId = "1";
        when(groupsRepository.findById(groupId)).thenReturn(Optional.empty());

        String groupName = groupsService.getGroupNameById(groupId);

        assertEquals("Unknown Category", groupName);

        verify(groupsRepository, times(1)).findById(groupId);
    }
}
