package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.ProjectDTO;
import com.EffortlyTimeTracker.DTO.TableDTO;
import com.EffortlyTimeTracker.entity.Project;
import com.EffortlyTimeTracker.entity.TableProject;
import com.EffortlyTimeTracker.service.ProjectService;
import com.EffortlyTimeTracker.service.TableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public TableProject addTable(@Valid @RequestBody TableDTO tableDTO) {
        return tableService.addTable(tableDTO);
    }

    @Operation(summary = "Dell table by id",
            description = "need id")
    @DeleteMapping("/del")
    public void deleteTable(@RequestParam(required = true) Integer id_table) {
        tableService.delTableById(id_table);
    }

}
