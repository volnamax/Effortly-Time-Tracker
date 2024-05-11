package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.GroupEntity;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.exception.group.GroupNotFoundException;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.repository.GroupMemberRepository;
import com.EffortlyTimeTracker.repository.GroupRepository;
import com.EffortlyTimeTracker.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private GroupMemberRepository groupMemberRepository;
    @InjectMocks
    private GroupService groupService;

    private GroupEntity groupEntity;
    private ProjectEntity projectEntity;
    private UserEntity userEntity;
    private RoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setName(Role.USER);

        userEntity =  UserEntity.builder()
                .userName("name")
                .userSecondname("secondname")
                .email("qwerty@gmail.com")
                .role(roleEntity)
                .build();

        projectEntity = ProjectEntity.builder()
                .projectId(1)
                .userProject(userEntity)
                .name("Project Name")
                .description("Project Description")
                .build();

        groupEntity = GroupEntity.builder()
                .groupId(1)
                .name("New Group")
                .description("New Group Description")
                .project(projectEntity)
                .build();
    }


    @Test
    void addGroupTestSuccess() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(projectEntity));
        when(groupRepository.save(any(GroupEntity.class))).thenReturn(groupEntity);
        when(groupMemberRepository.save(any())).thenReturn(null);  // Mock save method for GroupMemberRepository

        GroupEntity savedGroup = groupService.addGroup(groupEntity);

        assertNotNull(savedGroup);
        assertNotNull(savedGroup.getProject());
        assertEquals("New Group", savedGroup.getName());
        verify(groupRepository).save(any(GroupEntity.class));
        verify(projectRepository).save(projectEntity); // Verify that the project is updated
    }

    @Test
    void addGroupTestProjectNotFound() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProjectNotFoundException.class, () -> {
            groupService.addGroup(groupEntity);
        });

        String expectedMessage = "Project with ID 1 not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delGroupByIdTestExists() {
        when(groupRepository.existsById(anyInt())).thenReturn(true);

        groupService.delGroupById(1);

        verify(groupRepository).deleteById(1);
    }

    @Test
    void delGroupByIdTestNotExists() {
        when(groupRepository.existsById(anyInt())).thenReturn(false);

        Exception exception = assertThrows(GroupNotFoundException.class, () -> {
            groupService.delGroupById(1);
        });

        assertEquals("Group with id 1 not found", exception.getMessage());
    }

    @Test
    void getGroupByIdTestFound() {
        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(groupEntity));

        GroupEntity foundGroup = groupService.getGroupById(1);

        assertNotNull(foundGroup);
        assertEquals("New Group", foundGroup.getName());
    }

    @Test
    void getGroupByIdTestNotFound() {
        when(groupRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(GroupNotFoundException.class, () -> {
            groupService.getGroupById(1);
        });

        assertEquals("Group with id 1 not found", exception.getMessage());
    }
}

