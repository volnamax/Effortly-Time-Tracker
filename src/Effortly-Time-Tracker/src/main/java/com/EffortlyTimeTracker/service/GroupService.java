package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.GroupEntity;
import com.EffortlyTimeTracker.entity.GroupMermberEntity;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.exception.group.GroupNotFoundException;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.mapper.ProjectMapper;
import com.EffortlyTimeTracker.mapper.UserMongoMapper;
import com.EffortlyTimeTracker.repository.GroupMemberRepository;
import com.EffortlyTimeTracker.repository.GroupRepository;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.ProjectRepository;
import com.EffortlyTimeTracker.repository.mongo.IMongoUserRepository;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;
    private final ProjectRepository projectRepository;
    private final IUserRepository userPostgresRepository;
    private final IMongoUserRepository userMongoRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ProjectMapper projectMapper;
    private final String activeDb;

    @Autowired
    public GroupService(GroupRepository groupRepository,
                        ProjectRepository projectRepository,
                        @Qualifier("userPostgresRepository") IUserRepository userPostgresRepository,
                        @Qualifier("userMongoRepository") IMongoUserRepository userMongoRepository,
                        GroupMemberRepository groupMemberRepository,
                        ProjectMapper projectMapper,
                        @Value("${app.active-db}") String activeDb) {
        this.groupRepository = groupRepository;
        this.projectRepository = projectRepository;
        this.userPostgresRepository = userPostgresRepository;
        this.userMongoRepository = userMongoRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.projectMapper = projectMapper;
        this.activeDb = activeDb;
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
        log.info("group with id {} delete", id);
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
        if ("postgres".equalsIgnoreCase(activeDb)) {
            return userPostgresRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found in Postgres"));
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            return userMongoRepository.findById(userId.toString())
                    .map(UserMongoMapper::toUserEntity)
                    .orElseThrow(() -> new IllegalArgumentException("User not found in MongoDB"));
        } else {
            throw new IllegalStateException("Unknown database type: " + activeDb);
        }
    }
}
