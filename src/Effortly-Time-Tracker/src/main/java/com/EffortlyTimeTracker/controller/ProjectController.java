package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.project.ProjectAnalyticsDTO;
import com.EffortlyTimeTracker.DTO.project.ProjectCreateDTO;
import com.EffortlyTimeTracker.DTO.project.ProjectResponseDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.mapper.ProjectMapper;
import com.EffortlyTimeTracker.service.ProjectService;
import com.EffortlyTimeTracker.service.middlewareOwn.project.CheckProjectOwner;
import com.EffortlyTimeTracker.service.middlewareOwn.project.CheckUserIdMatchesCurrentUserProject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Project-controller")
@RestController
@RequestMapping("api/projects")
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST', 'ROLE_ADMIN')")
    @CheckUserIdMatchesCurrentUserProject
    public ResponseEntity<ProjectResponseDTO> addProject(@Valid @RequestBody ProjectCreateDTO projectDTO) {
        log.info("api/project/add");
        log.info("in projectDTO = {}", projectDTO);

        // Проверяем, существует ли пользователь
            if (projectService.getAllProjectByIdUser(projectDTO.getUserProject()) == null) {
            log.warn("User with id {} not found", projectDTO.getUserProject());
            throw new RuntimeException("User not found");
        }

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @DeleteMapping("/del")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckProjectOwner
    public void delProject(@RequestParam(required = true) Integer projectId) {
        log.info("api/project/del");

        log.info("in projectId = {}", projectId);
        projectService.delProjectById(projectId);
        log.info("del project ok");
    }


    @Operation(summary = "Get proj by id",
            description = "need id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    @CheckProjectOwner
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content)
    })
    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ProjectResponseDTO> getProjects() {
        log.info("api/project/get-all");
        List<ProjectEntity> resProjectEntity = projectService.getAllProject();
        log.info("resProjectEntity = {}", resProjectEntity);
        return projectMapper.toResponseDTO(resProjectEntity);
    }

    @Operation(summary = "Dell all proj by user id", description = "need user id")
    @DeleteMapping("/del-by-user-id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Projects successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found or no projects to delete", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckUserIdMatchesCurrentUserProject
    public void delAllProjBuUserID(@RequestParam(required = true) Integer userId) {
        log.info("api/project/del-by-user-id");
        log.info("in projectId = {}", userId);
        projectService.delAllProjectByIdUser(userId);
    }

    @Operation(summary = "Get all project by id user", description = "need id user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found or no projects available", content = @Content)
    })
    @GetMapping("/get-all-by-user-id")
    @ResponseStatus(HttpStatus.OK)
    @CheckUserIdMatchesCurrentUserProject
    public List<ProjectResponseDTO> getProjectAll(Integer id) {
        log.info("api/project/get-all-by-user-id");
        log.info("in projectId = {}", id);

        List<ProjectEntity> resProjectEntity = projectService.getAllProjectByIdUser(id);
        log.info("resProjectEntity = {}", resProjectEntity);

        return projectMapper.toResponseDTO(resProjectEntity);
    }


    @Operation(summary = "Get project analytics", description = "Returns all tasks related to the project with time spent and start/end dates, including average, max, min, and median time spent on tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project analytics successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectAnalyticsDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @GetMapping("/analytics")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST', 'ROLE_ADMIN')")
    @CheckProjectOwner
    public ProjectAnalyticsDTO getProjectAnalytics(@RequestParam Integer projectId) {
        log.info("api/project/analytics");

        log.info("in projectId = {}", projectId);
        ProjectAnalyticsDTO analyticsDTO = projectService.getProjectAnalytics(projectId);
        log.info("analyticsDTO = {}", analyticsDTO);
        return analyticsDTO;
    }

}


