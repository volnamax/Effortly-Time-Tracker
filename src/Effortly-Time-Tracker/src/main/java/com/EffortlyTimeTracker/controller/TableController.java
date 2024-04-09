package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.TableDTO;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.service.TableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Table-controller")
@RestController
@RequestMapping("api/table")
public class TableController {
    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @Operation(summary = "Add table")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public TableEntity addTable(@Valid @RequestBody TableDTO tableDTO) {
        return tableService.addTable(tableDTO);
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
    public TableEntity getTable(@RequestParam(required = true) Integer  tableId) {
        return tableService.getTableById( tableId);
    }

    @Operation(summary = "Get all table")
    @GetMapping("/get-all")
    public List<TableEntity> getProjects() {
        return tableService.getAllTable();
    }


}
