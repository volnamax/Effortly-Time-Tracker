package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.table.TableCreateDTO;
import com.EffortlyTimeTracker.DTO.table.TableDTO;
import com.EffortlyTimeTracker.DTO.table.TableResponseDTO;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.mapper.TableMapper;
import com.EffortlyTimeTracker.service.TableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@Tag(name = "Table-controller")
@RestController
@RequestMapping("api/table")
public class TableController {
    private final TableService tableService;
    private final TableMapper tableMapper;

    @Autowired
    public TableController(TableService tableService, TableMapper tableMapper) {
        this.tableService = tableService;
        this.tableMapper = tableMapper;
    }

    @Operation(summary = "Add table", description = "need projectId, and name, status = ACTIVE, NO_ACTIVE")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TableResponseDTO> addTable(@Valid @RequestBody TableCreateDTO tableDTO) {
        log.info("api/table/add");
        log.info("Adding table dto: {}", tableDTO);
        TableEntity tableEntity = tableMapper.toEntity(tableDTO);
        log.info("Adding table entity: {}", tableEntity);

        TableEntity newTableEntity1 = tableService.addTable(tableEntity);
        log.info("Adding  table newentity: {}", newTableEntity1);

        TableResponseDTO responsDto = tableMapper.toResponseDTO(newTableEntity1);
        log.info("Adding table respons dto: {}", responsDto);
        return new ResponseEntity<>(responsDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Dell table by id", description = "need id table")
    @DeleteMapping("/del")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTable(@RequestParam(required = true) Integer id_table) {
        log.info("api/table/del");
        log.info("Deleting table by id: {}", id_table);

        tableService.delTableById(id_table);
    }

    @Operation(summary = "Get table by id", description = "need id table")
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public TableResponseDTO getTable(@RequestParam(required = true) Integer tableId) {
        log.info("api/table/get");
        log.info("Getting table by id: {}", tableId);

        TableEntity tableEntity = tableService.getTableById(tableId);
        log.info("Table entity: {}", tableEntity);

        TableResponseDTO responsDto = tableMapper.toResponseDTO(tableEntity);
        log.info("Getting table respons dto: {}", responsDto);

        return responsDto;
    }

    @Operation(summary = "Get all table")
    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    public List<TableResponseDTO> getAllTable() {
        log.info("api/table/get-all");

        List<TableEntity> resEntity = tableService.getAllTable();
        log.info("Table entity: {}", resEntity);
        List<TableResponseDTO> responseDTO = tableMapper.toResponseDTO(resEntity);
        log.info("Getting all table respons dto: {}", responseDTO);

        return responseDTO;

    }

    @Operation(summary = "Get all table by id project", description = "need proj id")
    @GetMapping("/get-all-by-project-id")
    @ResponseStatus(HttpStatus.OK)
    public List<TableResponseDTO> getTableAllByProjectId(Integer id) {
        log.info("api/table/get-all-by-project-id");
        log.info("Getting all table by id project: {}", id);

        List<TableEntity> resEntity = tableService.getAllTableByIdProject(id);
        log.info("Table entity: {}", resEntity);

        List<TableResponseDTO> tableResponseDTOS= tableMapper.toResponseDTO(resEntity);
        log.info("Getting all table respons dto: {}", tableResponseDTOS);

        return tableResponseDTOS;
    }

    @Operation(summary = "Dell all table by project id", description = "need proj id")
    @DeleteMapping("/del-by-project-id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delAllTableByProjectId(@RequestParam(required = true) Integer id) {
        log.info("api/table/del-by-project-id");
        log.info("Deleting table by project id: {}", id);
        tableService.delAllTablleByIdProject(id);
    }
}
