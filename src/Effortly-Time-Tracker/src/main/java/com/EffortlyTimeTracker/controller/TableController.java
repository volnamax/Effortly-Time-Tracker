package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.TableDTO;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.mapper.TableMapper;
import com.EffortlyTimeTracker.service.TableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "Add table")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TableDTO> addTable(@Valid @RequestBody TableDTO tableDTO) {
        TableEntity tableEntity = tableMapper.dtoToEntity(tableDTO);
        TableEntity newTableEntity1 = tableService.addTable(tableEntity);
        TableDTO responsDto = tableMapper.entityToDto(newTableEntity1);

        return new ResponseEntity<>(responsDto, HttpStatus.CREATED);
    }


    @Operation(summary = "Dell table by id",
            description = "need id")
    @DeleteMapping("/del")
    public void deleteTable(@RequestParam(required = true) Integer id_table) {
        tableService.delTableById(id_table);
    }

    @Operation(summary = "Get table by id",
            description = "need id")
    @GetMapping("/get")
    public TableDTO getTable(@RequestParam(required = true) Integer tableId) {
        TableEntity tableEntity = tableService.getTableById(tableId);
        return tableMapper.entityToDto(tableEntity);
    }

    @Operation(summary = "Get all table")
    @GetMapping("/get-all")
    public List<TableDTO> getProjects() {
        List<TableEntity> resEntity = tableService.getAllTable();
        return tableMapper.entityListToDtoList(resEntity);

    }


}
