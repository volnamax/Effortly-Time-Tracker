package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.exception.project.ProjectIsEmpty;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.repository.ProjectRepository;
import com.EffortlyTimeTracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }


    public ProjectEntity addProject(@NotNull ProjectEntity projectEntity) {
        return projectRepository.save(projectEntity);
    }

    public void delProjectById(Integer id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException(id);
        }
        projectRepository.deleteById(id);
        log.info("Project with id {} deleted", id);
    }

    public ProjectEntity getProjectsById(Integer id) {
        ProjectEntity proj = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        log.info("Get = " + proj);

        return proj;
    }
    public List<ProjectEntity>getAllProject () {
        return projectRepository.findAll();
    }


//    @Transactional(readOnly = true)
//    public List<ProjectDTO> getAllProject() {
//        return projectRepository.findAll().stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }
////
//    private ProjectDTO convertToDto(ProjectEntity projectEntity) {
//        ProjectDTO projectDTO = new ProjectDTO();
//        projectDTO.setName(projectEntity.getName());
//        projectDTO.setDescription(projectEntity.getDescription());
//        projectDTO.setUserProject(projectEntity.getUserProject());
//        projectDTO.setGroupProject(projectEntity.getGroup());
//        return projectDTO;
//    }

    public List<ProjectEntity> getAllProjectByIdUser(Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoudException(userId));

        List<ProjectEntity> userTodos = projectRepository.findByUserId(userId);

        if (userTodos.isEmpty()) {
            log.info("No todos found for user with id {}", userId);
            throw new ProjectIsEmpty();
        }
        return userTodos;
    }

    public void delAllProjectByIdUser(Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoudException(userId));

        List<ProjectEntity> userTodos = projectRepository.findByUserId(userId);

        if (userTodos.isEmpty()) {
            log.info("No todos found for user with id {}", userId);
            return;
        }

        projectRepository.deleteAll(userTodos);
        log.info("All todos for user with id {} deleted", userId);
    }


}
