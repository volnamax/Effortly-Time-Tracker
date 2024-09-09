package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.exception.table.TableIsEmpty;
import com.EffortlyTimeTracker.exception.table.TableNotFoundException;
import com.EffortlyTimeTracker.repository.IProjectRepository;
import com.EffortlyTimeTracker.repository.postgres.ProjectPostgresRepository;
import com.EffortlyTimeTracker.repository.postgres.TableRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TableService {
    private final TableRepository tableRepository;
    private final IProjectRepository postgresRepository;

    @Autowired
    public TableService(TableRepository tableRepository, IProjectRepository postgresRepository) {
        this.tableRepository = tableRepository;
        this.postgresRepository = postgresRepository;
    }

    public TableEntity addTable(@NonNull TableEntity tableEntity) {
        return tableRepository.save(tableEntity);
    }


    public void delTableById(Integer tableId) {
        if (!tableRepository.existsById(tableId)) {
            throw new TableNotFoundException(tableId);
        }
        tableRepository.deleteById(tableId);
        log.info("Table with id {} deleted", tableId);
    }

    public TableEntity getTableById(Integer id) {
        return tableRepository.findById(id).orElseThrow(() -> new TableNotFoundException(id));
    }

    public List<TableEntity> getAllTable() {
        return tableRepository.findAll();
    }

    public List<TableEntity> getAllTableByIdProject(Integer projectId) {
        ProjectEntity project = postgresRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        List<TableEntity> tableEntities = tableRepository.findByProjectId(projectId);

        if (tableEntities.isEmpty()) {
            log.info("No todos found for project with id {}", project);
            throw new TableIsEmpty();
        }
        return tableEntities;
    }

    public void delAllTablleByIdProject(Integer projectId) {
        ProjectEntity project = postgresRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        List<TableEntity> tableEntities = tableRepository.findByProjectId(projectId);

        if (tableEntities.isEmpty()) {
            log.info("No todos found for project with id {}", project);
            return;
        }
        tableRepository.deleteAll(tableEntities);
        log.info("All todos for project with id {} deleted", project);
    }


}
