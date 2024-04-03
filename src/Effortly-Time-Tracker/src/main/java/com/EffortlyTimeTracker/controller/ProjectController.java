package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.ProjectDTO;
import com.EffortlyTimeTracker.entity.Project;
import com.EffortlyTimeTracker.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Project-controller")
@RestController
@RequestMapping("api/project")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Add project")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Project addProject(@Valid @RequestBody ProjectDTO projectDTO) {
        return projectService.addProject(projectDTO);
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
    public Project getProject(@RequestParam(required = true) Integer  projectId) {
        return projectService.getProjectsById( projectId);
    }
    @Operation(summary = "Get all proj")
    @GetMapping("/get-all")
    public List<Project> getProjects() {
        return projectService.getAllProject();
    }

}
