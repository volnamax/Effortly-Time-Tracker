package com.EffortlyTimeTracker.controller.v2;

import com.EffortlyTimeTracker.DTO.project.ProjectAnalyticsDTO;
import com.EffortlyTimeTracker.DTO.project.ProjectCreateDTO;
import com.EffortlyTimeTracker.DTO.project.ProjectResponseDTO;
import com.EffortlyTimeTracker.DTO.table.TableResponseDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.mapper.ProjectMapper;
import com.EffortlyTimeTracker.mapper.TableMapper;
import com.EffortlyTimeTracker.service.ProjectService;
import com.EffortlyTimeTracker.service.TableService;
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
@Tag(name = "Project-V2-controller")
@RestController
@RequestMapping("api/v2/projects")
public class ProjectControllerV2 {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final TableMapper tableMapper;
    private final TableService tableService;

    @Autowired
    public ProjectControllerV2(ProjectService projectService, ProjectMapper projectMapper, TableMapper tableMapper, TableService tableService) {
        this.tableMapper = tableMapper;
        this.tableService = tableService;
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }

    @Operation(summary = "Add project", description = "need user id and name")
    @PostMapping()
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


        ProjectEntity projectEntity = projectMapper.toEntity(projectDTO);
        log.info("projectEntity = {}", projectEntity);

        ProjectEntity newProjctEntity = projectService.addProject(projectEntity);
        log.info("newProjctEntity = {}", newProjctEntity);

        ProjectResponseDTO responsProjectDto = projectMapper.toResponseDTO(newProjctEntity);
        log.info("responsProjectDto = {}", responsProjectDto);

        return new ResponseEntity<>(responsProjectDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete project by id", description = "Delete project by project id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckProjectOwner
    public void delProject(@PathVariable("projectId") Integer projectId) {
        log.info("api/projects/{}", projectId);

        projectService.delProjectById(projectId);
        log.info("del project ok");
    }


    @Operation(summary = "Get project by id", description = "Retrieve project details by project id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @GetMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    @CheckProjectOwner
    public ProjectResponseDTO getProject(@PathVariable("projectId") Integer projectId) {
        log.info("api/projects/{}", projectId);

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
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ProjectResponseDTO> getProjects() {
        log.info("api/project/get-all");
        List<ProjectEntity> resProjectEntity = projectService.getAllProject();
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
    @GetMapping("/{projectId}/analytics")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST', 'ROLE_ADMIN')")
    @CheckProjectOwner
    public ProjectAnalyticsDTO getProjectAnalytics(@PathVariable("projectId") Integer projectId) {
        log.info("api/projects/{}/analytics", projectId);

        log.info("in projectId = {}", projectId);
        ProjectAnalyticsDTO analyticsDTO = projectService.getProjectAnalytics(projectId);
        log.info("analyticsDTO = {}", analyticsDTO);
        return analyticsDTO;
    }

    @Operation(summary = "Get all tables by project id", description = "Retrieve all tables for a specific project by project id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tables successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TableResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @GetMapping("/{projectId}/tables")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_GUEST')")
    @CheckProjectOwner
    public List<TableResponseDTO> getTableAllByProjectId(@PathVariable("projectId") Integer projectId) {
        log.info("api/projects/{}/tables", projectId);
        log.info("Fetching all tables for project id: {}", projectId);

        if (tableService.getAllTableByIdProject(projectId) == null) {
            log.warn("Project with id {} not found", projectId);
            throw new RuntimeException("Project not found");
        }

        List<TableEntity> resEntity = tableService.getAllTableByIdProject(projectId);
        return tableMapper.toResponseDTO(resEntity);
    }

    @Operation(summary = "Delete all tables by project id", description = "Delete all tables associated with a specific project by project id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tables successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @DeleteMapping("/{projectId}/tables")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_GUEST')")
    @CheckProjectOwner
    public void delAllTableByProjectId(@PathVariable("projectId") Integer projectId) {
        log.info("api/projects/{}/tables", projectId);
        log.info("Deleting all tables for project id: {}", projectId);

        if (tableService.getAllTableByIdProject(projectId) == null) {
            log.warn("Project with id {} not found", projectId);
            throw new RuntimeException("Project not found");
        }

        tableService.delAllTablleByIdProject(projectId);
    }


}


