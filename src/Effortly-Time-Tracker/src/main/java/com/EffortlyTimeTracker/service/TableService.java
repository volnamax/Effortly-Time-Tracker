package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.TableDTO;
import com.EffortlyTimeTracker.entity.TableProject;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.repository.TableRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TableService {
    private final TableRepository tableRepository;

    @Autowired
    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public TableProject addTable(@NonNull TableDTO tableDTO) {
        TableProject table = tableRepository.save(TableProject.builder()
                .name(tableDTO.getName())
                .description(tableDTO.getDescription())
                .status(TableProject.Status.valueOf(tableDTO.getStatus()))
                .project(tableDTO.getProject())
                .tasks(tableDTO.getTasks())
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

}
