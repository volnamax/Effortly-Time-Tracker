package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.GroupEntity;
import com.EffortlyTimeTracker.entity.GroupMermberEntity;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.exception.group.GroupNotFoundException;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.repository.IProjectRepository;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.postgres.GroupMemberRepository;
import com.EffortlyTimeTracker.repository.postgres.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;
    private final IProjectRepository projectRepository;
    private final IUserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository,
                        IProjectRepository projectRepository,
                        IUserRepository userRepository,
                        GroupMemberRepository groupMemberRepository) {
        this.groupRepository = groupRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    @Transactional
    public GroupEntity addGroup(GroupEntity groupEntity) {
        ProjectEntity project = projectRepository.findById(groupEntity.getProject().getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(groupEntity.getProject().getProjectId()));

        groupEntity.setProject(project);
        GroupEntity savedGroup = groupRepository.save(groupEntity);
        project.setGroup(savedGroup);
        projectRepository.save(project);

        GroupMermberEntity groupMermberEntity = new GroupMermberEntity();
        groupMermberEntity.setGroup(groupEntity);
        groupMermberEntity.setRole(Role.ADMIN);
        groupMermberEntity.setUser(project.getUserProject());

        groupMemberRepository.save(groupMermberEntity);

        return savedGroup;
    }

    public void delGroupById(Integer id) {
        if (!groupRepository.existsById(id)) {
            throw new GroupNotFoundException(id);
        }
        groupRepository.deleteById(id);
        log.info("Group with id {} deleted", id);
    }

    public GroupEntity getGroupById(Integer id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));
    }

    public List<GroupEntity> getAllGroup() {
        return groupRepository.findAll();
    }

    public void addUserToGroup(Integer groupId, Integer userId) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        UserEntity user = findUserById(userId);

        if (group.getMembers().stream().anyMatch(member -> member.getUser().getUserId().equals(userId))) {
            throw new IllegalArgumentException("User is already a member of this group");
        }

        GroupMermberEntity groupMember = new GroupMermberEntity();
        groupMember.setGroup(group);
        groupMember.setUser(user);
        groupMember.setRole(Role.MANAGER);

        group.getMembers().add(groupMember);
        groupMemberRepository.save(groupMember);
        groupRepository.save(group);
    }

    public void removeUserFromGroup(Integer groupId, Integer userId) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        UserEntity user = findUserById(userId);

        GroupMermberEntity groupMember = group.getMembers().stream()
                .filter(member -> member.getUser().getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User is not a member of this group"));

        if (groupMember.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("Cannot remove the admin of the group");
        }

        group.getMembers().remove(groupMember);
        groupMemberRepository.delete(groupMember);
        groupRepository.save(group);
    }

    private UserEntity findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
