package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.*;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.exception.group.GroupNotFoundException;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.repository.GroupMemberRepository;
import com.EffortlyTimeTracker.repository.GroupRepository;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
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

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private GroupService groupService;

    private GroupEntity groupEntity;
    private ProjectEntity projectEntity;
    private UserEntity userEntity;
    private UserEntity anotherUserEntity;
    private RoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setName(Role.MANAGER);

        userEntity = UserEntity.builder()
                .userName("name")
                .userSecondname("secondname")
                .email("qwerty@gmail.com")
                .role(roleEntity)
                .userId(1)
                .build();

        anotherUserEntity = UserEntity.builder()
                .userName("anotherName")
                .userSecondname("anotherSecondname")
                .email("another@gmail.com")
                .role(roleEntity)
                .userId(2)
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
                .members(new HashSet<>())  // Initialize members
                .build();
    }

    @Test
    void addGroupTestSuccess() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(projectEntity));
        when(groupRepository.save(any(GroupEntity.class))).thenReturn(groupEntity);

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
    //todo test
//
//    @Test
//    void addUserToGroupTestSuccess() {
//        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(groupEntity));
//        when(userRepository.findById(2)).thenReturn(Optional.of(anotherUserEntity));
//        when(groupMemberRepository.save(any(GroupMermberEntity.class))).thenReturn(null);
//
//        groupService.addUserToGroup(groupEntity.getGroupId(), anotherUserEntity.getUserId());
//
//        verify(groupMemberRepository).save(any(GroupMermberEntity.class));
//        verify(groupRepository).save(any(GroupEntity.class));
//    }
//
//    @Test
//    void addUserToGroupTestGroupNotFound() {
//        when(groupRepository.findById(anyInt())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            groupService.addUserToGroup(1, anotherUserEntity.getUserId());
//        });
//
//        assertEquals("Group not found", exception.getMessage());
//    }
//
//    @Test
//    void addUserToGroupTestUserNotFound() {
//        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(groupEntity));
//        when(userRepository.findById(2)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            groupService.addUserToGroup(groupEntity.getGroupId(), anotherUserEntity.getUserId());
//        });
//
//        assertEquals("User not found", exception.getMessage());
//    }
//
//    @Test
//    void addUserToGroupTestUserAlreadyMember() {
//        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(groupEntity));
//        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
//
//        GroupMermberEntity existingMember = new GroupMermberEntity();
//        existingMember.setGroup(groupEntity);
//        existingMember.setUser(userEntity);
//
//        groupEntity.getMembers().add(existingMember);
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            groupService.addUserToGroup(groupEntity.getGroupId(), userEntity.getUserId());
//        });
//
//        assertEquals("User is already a member of this group", exception.getMessage());
//    }
//
//    @Test
//    void removeUserFromGroupTestSuccess() {
//        GroupMermberEntity existingMember = new GroupMermberEntity();
//        existingMember.setGroup(groupEntity);
//        existingMember.setUser(anotherUserEntity);
//
//        groupEntity.getMembers().add(existingMember);
//
//        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(groupEntity));
//        when(userRepository.findById(2)).thenReturn(Optional.of(anotherUserEntity));
//
//        groupService.removeUserFromGroup(groupEntity.getGroupId(), anotherUserEntity.getUserId());
//
//        verify(groupMemberRepository).delete(any(GroupMermberEntity.class));
//        verify(groupRepository).save(any(GroupEntity.class));
//    }
//
//    @Test
//    void removeUserFromGroupTestGroupNotFound() {
//        when(groupRepository.findById(anyInt())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            groupService.removeUserFromGroup(1, anotherUserEntity.getUserId());
//        });
//
//        assertEquals("Group not found", exception.getMessage());
//    }
//
//    @Test
//    void removeUserFromGroupTestUserNotFound() {
//        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(groupEntity));
//        when(userRepository.findById(2)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            groupService.removeUserFromGroup(groupEntity.getGroupId(), anotherUserEntity.getUserId());
//        });
//
//        assertEquals("User not found", exception.getMessage());
//    }
//
//    @Test
//    void removeUserFromGroupTestUserNotMember() {
//        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(groupEntity));
//        when(userRepository.findById(2)).thenReturn(Optional.of(anotherUserEntity));
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            groupService.removeUserFromGroup(groupEntity.getGroupId(), anotherUserEntity.getUserId());
//        });
//
//        assertEquals("User is not a member of this group", exception.getMessage());
//    }
//
//    @Test
//    void removeUserFromGroupTestUserIsAdmin() {
//        when(groupRepository.findById(anyInt())).thenReturn(Optional.of(groupEntity));
//        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            groupService.removeUserFromGroup(groupEntity.getGroupId(), userEntity.getUserId());
//        });
//
//        assertEquals("Cannot remove the admin of the group", exception.getMessage());
//    }
}
