package com.EffortlyTimeTracker.controller.v2;

import com.EffortlyTimeTracker.DTO.table.TableCreateDTO;
import com.EffortlyTimeTracker.DTO.table.TableResponseDTO;
import com.EffortlyTimeTracker.DTO.task.TaskResponseDTO;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.mapper.TableMapper;
import com.EffortlyTimeTracker.mapper.TaskMapper;
import com.EffortlyTimeTracker.unit.TableService;
import com.EffortlyTimeTracker.unit.TaskService;
import com.EffortlyTimeTracker.unit.middlewareOwn.table.CheckTableOwner;
import com.EffortlyTimeTracker.unit.middlewareOwn.table.CheckUserIdMatchesCurrentUserTable;
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
@Tag(name = "Table-V2-controller")
@RestController
@RequestMapping("api/v2/tables")
public class TableControllerV2 {
    private final TableService tableService;
    private final TableMapper tableMapper;
    private  final TaskService taskService;
    private final TaskMapper taskMapper;

    @Autowired
    public TableControllerV2(TableService tableService, TableMapper tableMapper, TaskService taskService, TaskMapper taskMapper) {
        this.tableService = tableService;
        this.tableMapper = tableMapper;
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @Operation(summary = "Add table", description = "need: projectId,  name, status = ACTIVE, NO_ACTIVE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Table successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TableResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')  or hasRole('ROLE_GUEST')")
    @CheckUserIdMatchesCurrentUserTable
    public ResponseEntity<TableResponseDTO> addTable(@Valid @RequestBody TableCreateDTO tableDTO) {
        log.info("api/table/add");
        log.info("Adding table dto: {}", tableDTO);

        TableEntity tableEntity = tableMapper.toEntity(tableDTO);
        TableEntity newTableEntity = tableService.addTable(tableEntity);
        TableResponseDTO responseDto = tableMapper.toResponseDTO(newTableEntity);

        log.info("Table successfully created: {}", responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    @Operation(summary = "Delete table by id", description = "Delete table by table id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Table successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Table not found", content = @Content)
    })

    @DeleteMapping("/{id_table}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_GUEST')")
    @CheckTableOwner
    public void deleteTable(@PathVariable("id_table") Integer idTable) {
        log.info("api/tables/{}", idTable);
        log.info("Deleting table by id: {}", idTable);

        // Проверяем, существует ли таблица
        if (tableService.getTableById(idTable) == null) {
            log.warn("Table with id {} not found", idTable);
            throw new RuntimeException("Table not found");
        }

        tableService.delTableById(idTable);
    }

    @Operation(summary = "Get table by id", description = "Retrieve table by table id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Table successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TableResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Table not found", content = @Content)
    })
    @GetMapping("/{tableId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_GUEST')")
    @CheckTableOwner
    public TableResponseDTO getTable(@PathVariable("tableId") Integer tableId) {
        log.info("api/tables/{}", tableId);
        log.info("Getting table by id: {}", tableId);

        TableEntity tableEntity = tableService.getTableById(tableId);
        if (tableEntity == null) {
            log.warn("Table with id {} not found", tableId);
            throw new RuntimeException("Table not found");
        }

        TableResponseDTO responseDto = tableMapper.toResponseDTO(tableEntity);
        log.info("Successfully retrieved table: {}", responseDto);
        return responseDto;
    }

    @Operation(summary = "Get all table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tables successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TableResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content)
    })
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TableResponseDTO> getAllTable() {
        log.info("api/table/get-all");

        List<TableEntity> resEntity = tableService.getAllTable();
        log.info("Table entity: {}", resEntity);
        List<TableResponseDTO> responseDTO = tableMapper.toResponseDTO(resEntity);
        log.info("Getting all table respons dto: {}", responseDTO);

        return responseDTO;

    }


    @Operation(summary = "Get all tasks by table id", description = "Retrieve all tasks associated with a specific table by table id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Table not found", content = @Content)
    })
    @GetMapping("/{tableId}/tasks")
    @ResponseStatus(HttpStatus.OK)
    @CheckTableOwner
    public List<TaskResponseDTO> getAllTasksByTableId(@PathVariable("tableId") Integer tableId) {
        log.info("api/tables/{}/tasks", tableId);

        // Проверка существования таблицы
        if (taskService.getAllTaskByIdTable(tableId) == null) {
            log.warn("Table with id {} not found", tableId);
            throw new RuntimeException("Table not found");
        }

        // Получение задач, связанных с таблицей
        List<TaskEntity> tasks = taskService.getAllTaskByIdTable(tableId);
        return taskMapper.toResponseDTO(tasks);
    }

    @Operation(summary = "Delete all tasks by table id", description = "Delete all tasks associated with a specific table by table id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tasks successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Table not found", content = @Content)
    })
    @DeleteMapping("/{tableId}/tasks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckTableOwner
    public void deleteAllTasksByTableId(@PathVariable("tableId") Integer tableId) {
        log.info("api/tables/{}/tasks", tableId);

        // Проверка существования таблицы
        if (taskService.getAllTaskByIdTable(tableId) == null) {
            log.warn("Table with id {} not found", tableId);
            throw new RuntimeException("Table not found");
        }

        // Удаление всех задач, связанных с таблицей
        taskService.delAllTaskByIdTable(tableId);
    }

}
