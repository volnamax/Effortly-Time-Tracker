package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.project.ProjectCreateDTO;
import com.EffortlyTimeTracker.DTO.project.ProjectDTO;
import com.EffortlyTimeTracker.DTO.project.ProjectResponseDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;

import com.EffortlyTimeTracker.mapper.ProjectMapper;
import com.EffortlyTimeTracker.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@Tag(name = "Project-controller")
@RestController
@RequestMapping("api/project")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    @Autowired
    public ProjectController(ProjectService projectService, ProjectMapper projectMapper) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }

    @Operation(summary = "Add project", description = "need user id and name")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProjectResponseDTO> addProject(@Valid @RequestBody ProjectCreateDTO projectDTO) {
        log.info("api/project/add");

        log.info("in projectDTO = {}", projectDTO);
        ProjectEntity projectEntity = projectMapper.toEntity(projectDTO);
        log.info("projectEntity = {}", projectEntity);

        ProjectEntity newProjctEntity = projectService.addProject(projectEntity);
        log.info("newProjctEntity = {}", newProjctEntity);

        ProjectResponseDTO responsProjectDto = projectMapper.toResponseDTO(newProjctEntity);
        log.info("responsProjectDto = {}", responsProjectDto);

        return new ResponseEntity<>(responsProjectDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Dell proj by id",
            description = "need id")
    @DeleteMapping("/del")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delProject(@RequestParam(required = true) Integer projectId) {
        log.info("api/project/del");

        log.info("in projectId = {}", projectId);
        projectService.delProjectById(projectId);
        log.info("del project ok");
    }


    @Operation(summary = "Get proj by id",
            description = "need id")
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public ProjectResponseDTO getProject(@RequestParam(required = true) Integer projectId) {
        log.info("api/project/get");

        log.info("in projectId = {}", projectId);
        ProjectEntity projectEntity = projectService.getProjectsById(projectId);
        log.info("projectEntity = {}", projectEntity);

        ProjectResponseDTO responsProjectDto = projectMapper.toResponseDTO(projectEntity);
        log.info("responsProjectDto = {}", responsProjectDto);
        return responsProjectDto;
    }


    @Operation(summary = "Get all proj")
    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectResponseDTO> getProjects() {
        log.info("api/project/get-all");
        List<ProjectEntity> resProjectEntity = projectService.getAllProject();
        log.info("resProjectEntity = {}", resProjectEntity);
        return projectMapper.toResponseDTO(resProjectEntity);
    }

    @Operation(summary = "Dell all proj by user id", description = "need user id")
    @DeleteMapping("/del-by-user-id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delAllProjBuUserID(@RequestParam(required = true) Integer userId) {
        log.info("api/project/del-by-user-id");
        log.info("in projectId = {}", userId);
        projectService.delAllProjectByIdUser(userId);
    }

    @Operation(summary = "Get all project by id user", description = "need id user")
    @GetMapping("/get-all-by-user-id")
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectResponseDTO> getProjectAll(Integer id) {
        log.info("api/project/get-all-by-user-id");
        log.info("in projectId = {}", id);

        List<ProjectEntity> resProjectEntity = projectService.getAllProjectByIdUser(id);
        log.info("resProjectEntity = {}", resProjectEntity);

        return projectMapper.toResponseDTO(resProjectEntity);
    }
}


