package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.table.TableCreateDTO;
import com.EffortlyTimeTracker.DTO.table.TableResponseDTO;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.mapper.TableMapper;
import com.EffortlyTimeTracker.service.TableService;
import com.EffortlyTimeTracker.service.middlewareOwn.project.CheckProjectOwner;
import com.EffortlyTimeTracker.service.middlewareOwn.table.CheckTableOwner;
import com.EffortlyTimeTracker.service.middlewareOwn.table.CheckUserIdMatchesCurrentUserTable;
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
@Tag(name = "Table-controller")
@RestController
@RequestMapping("api/tables")
public class TableController {
    private final TableService tableService;
    private final TableMapper tableMapper;

    @Autowired
    public TableController(TableService tableService, TableMapper tableMapper) {
        this.tableService = tableService;
        this.tableMapper = tableMapper;
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
    @PostMapping("/add")
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

    @Operation(summary = "Dell table by id", description = "need id table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Table successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Table not found", content = @Content)
    })
    @DeleteMapping("/del")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    //todo not need autth controll
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')  or hasRole('ROLE_GUEST')")
    @CheckTableOwner
    public void deleteTable(@RequestParam(required = true) Integer id_table) {
        log.info("api/table/del");
        log.info("Deleting table by id: {}", id_table);

        // Проверяем, существует ли таблица
        if (tableService.getTableById(id_table) == null) {
            log.warn("Table with id {} not found", id_table);
            throw new RuntimeException("Table not found");
        }

        tableService.delTableById(id_table);
    }

    @Operation(summary = "Get table by id", description = "need id table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Table successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TableResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Table not found", content = @Content)
    })
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')  or hasRole('ROLE_GUEST')")
    @CheckTableOwner
    public TableResponseDTO getTable(@RequestParam(required = true) Integer tableId) {
        log.info("api/table/get");
        log.info("Getting table by id: {}", tableId);

        TableEntity tableEntity = tableService.getTableById(tableId);
        if (tableEntity == null) {
            log.warn("Table with id {} not found", tableId);
            throw new RuntimeException("Table not found");
        }

        TableResponseDTO responsDto = tableMapper.toResponseDTO(tableEntity);
        log.info("Successfully retrieved table: {}", responsDto);
        return responsDto;
    }

    @Operation(summary = "Get all table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tables successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TableResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content)
    })
    @GetMapping("/get-all")
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

    @Operation(summary = "Get all table by id project", description = "need proj id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tables successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TableResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @GetMapping("/get-all-by-project-id")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')  or hasRole('ROLE_GUEST')")
    @CheckProjectOwner
    public List<TableResponseDTO> getTableAllByProjectId(Integer id) {
        log.info("api/table/get-all-by-project-id");

        if (tableService.getAllTableByIdProject(id) == null) {
            log.warn("Project with id {} not found", id);
            throw new RuntimeException("Project not found");
        }

        List<TableEntity> resEntity = tableService.getAllTableByIdProject(id);
        return tableMapper.toResponseDTO(resEntity);
    }

    @Operation(summary = "Dell all table by project id", description = "need proj id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tables successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @DeleteMapping("/del-by-project-id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')  or hasRole('ROLE_GUEST')")
    @CheckProjectOwner
    public void delAllTableByProjectId(@RequestParam(required = true) Integer id) {
        log.info("api/table/del-by-project-id");

        if (tableService.getAllTableByIdProject(id) == null) {
            log.warn("Project with id {} not found", id);
            throw new RuntimeException("Project not found");
        }

        tableService.delAllTablleByIdProject(id);
    }
}
