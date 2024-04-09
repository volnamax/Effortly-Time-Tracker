package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.GroupDTO;
import com.EffortlyTimeTracker.entity.GroupEntity;
import com.EffortlyTimeTracker.entity.GroupMermberEntity;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.exception.group.GroupNotFoundException;
import com.EffortlyTimeTracker.repository.GroupRepository;
import com.EffortlyTimeTracker.repository.ProjectRepository;
import com.EffortlyTimeTracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    @Autowired
    public GroupService(@NonNull GroupRepository groupRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public GroupEntity addGroup(GroupDTO groupDTO) {
        // Создание новой группы из DTO
        GroupEntity group = new GroupEntity();
        group.setName(groupDTO.getName());
        group.setDescription(groupDTO.getDescription());

        // Загрузка и установка проекта для группы
        ProjectEntity project = projectRepository.findById(groupDTO.getProject().getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
//        group.setProject(project);

        // Инициализация набора пользователей в группе
        Set<GroupMermberEntity> groupUsers = new HashSet<>();

        // Для каждого пользователя из DTO добавляем его в группу
        for (UserEntity user : groupDTO.getUsersGroup()) {
            // Здесь мы предполагаем, что объекты User уже существуют в базе данных.
            // Если это не так, вам потребуется их предварительно сохранить или обновить логику.
            UserEntity existingUser = userRepository.findById(user.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + user.getUserId()));

            GroupMermberEntity groupUser = new GroupMermberEntity();
            groupUser.setGroup(group);
            groupUser.setUser(existingUser);
            groupUsers.add(groupUser);
        }

        // Устанавливаем пользователей в группу
//        group.setUserGroups(groupUsers);

        // Сохраняем группу (вместе с пользователями) в базе данных
        return groupRepository.save(group);
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


