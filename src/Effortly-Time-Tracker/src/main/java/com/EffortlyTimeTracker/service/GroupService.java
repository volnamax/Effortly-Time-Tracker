package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.GroupEntity;
import com.EffortlyTimeTracker.entity.GroupMermberEntity;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.exception.group.GroupNotFoundException;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.mapper.ProjectMapper;
import com.EffortlyTimeTracker.repository.GroupMemberRepository;
import com.EffortlyTimeTracker.repository.GroupRepository;
import com.EffortlyTimeTracker.repository.ProjectRepository;
import com.EffortlyTimeTracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ProjectMapper projectMapper;


    @Autowired
    public GroupService(@NonNull GroupRepository groupRepository, ProjectRepository projectRepository, UserRepository userRepository, GroupMemberRepository groupMemberRepository, ProjectMapper projectMapper) {
        this.groupRepository = groupRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.projectMapper = projectMapper;
    }
    @Transactional
    public GroupEntity addGroup(GroupEntity groupEntity) {
        ProjectEntity project = projectRepository.findById(groupEntity.getProject().getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(groupEntity.getProject().getProjectId()));

        // Устанавливаем связь с проектом
        groupEntity.setProject(project);
        // Сохраняем группу, которая теперь связана с проектом
        GroupEntity savedGroup = groupRepository.save(groupEntity);
        // Обязательно обновляем проект с новым group_id
        project.setGroup(savedGroup);
        projectRepository.save(project);

        GroupMermberEntity groupMermberEntity = new GroupMermberEntity();
        groupMermberEntity.setGroup(groupEntity);
        groupMermberEntity.setRole(Role.USER);
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
        GroupEntity group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));
        log.info("Get = " + group);

        return group;
    }

    public List<GroupEntity> getAllGroup() {
        List<GroupEntity> groups = groupRepository.findAll();
        return groups;
    }
}


