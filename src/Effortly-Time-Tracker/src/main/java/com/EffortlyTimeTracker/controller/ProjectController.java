package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.ProjectDTO;
import com.EffortlyTimeTracker.DTO.UserCreateDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.mapper.ProjectMapper;
import com.EffortlyTimeTracker.mapper.UserMapper;
import com.EffortlyTimeTracker.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "Add project")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public  ResponseEntity<ProjectDTO> addProject(@Valid @RequestBody ProjectDTO projectDTO) {
        ProjectEntity projectEntity =projectMapper.dtoToEntity(projectDTO);
        ProjectEntity newProjctEntity = projectService.addProject(projectEntity);
        ProjectDTO responsProjectDto = projectMapper.entityToDto(newProjctEntity);

        return new ResponseEntity<>(responsProjectDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Dell proj by id",
            description = "need id")
    @DeleteMapping("/del")
    public void delProject(@RequestParam(required = true) Integer projectId) {
        projectService.delProjectById(projectId);
    }
    @Operation(summary = "Get proj by id",
            description = "need id")
    @GetMapping("/get")
    public ProjectEntity getProject(@RequestParam(required = true) Integer  projectId) {
        return projectService.getProjectsById( projectId);
    }
    @Operation(summary = "Get all proj")
    @GetMapping("/get-all")
    public List<ProjectDTO> getProjects() {
        return projectService.getAllProject();
    }

}
