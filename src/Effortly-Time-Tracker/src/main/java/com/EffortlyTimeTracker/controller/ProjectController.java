package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.ProjectDTO;
import com.EffortlyTimeTracker.entity.Project;
import com.EffortlyTimeTracker.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.List;
@Tag(name = "Project-controller") // documentation
@RestController
@RequestMapping("api/project")
public class ProjectController {
    private final ProjectService projectService;


    // todo
    @Operation(summary = "Добавляем проект определенного юзера",
            description = "more info")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Project addProject(@RequestBody ProjectDTO projectDTO) {
        return projectService.addProject(projectDTO);
    }
    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.findAll();
    }
}
