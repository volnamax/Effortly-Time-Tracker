package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.TableDTO;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.exception.table.TableNotFoundException;
import com.EffortlyTimeTracker.repository.TableRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TableService {
    private final TableRepository tableRepository;

    @Autowired
    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public TableEntity addTable(@NonNull TableDTO tableDTO) {
        TableEntity table = tableRepository.save(TableEntity.builder()
                .name(tableDTO.getName())
                .description(tableDTO.getDescription())
                .status(TableEntity.Status.valueOf(tableDTO.getStatus()))
                .project(tableDTO.getProject())
//                .tasks(tableDTO.getTasks())
                .build());

        return table;
    }

    public void delTableById(Integer tableId) {
        if (!tableRepository.existsById(tableId)) {
            throw new ProjectNotFoundException(tableId);
        }
        tableRepository.deleteById(tableId);
        log.info("Table with id {} deleted", tableId);
    }
    public TableEntity getTableById(Integer id) {
        TableEntity table = tableRepository.findById(id)
                .orElseThrow(() -> new TableNotFoundException(id));
        log.info("Get = " + table);

        return table;
    }

    public List<TableEntity> getAllTable() {
        List<TableEntity> tableProjects = tableRepository.findAll();
        log.info("GetALL = " + tableProjects);
        return tableProjects;
    }
}
