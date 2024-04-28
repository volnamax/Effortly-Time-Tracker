package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.TableDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.exception.project.ProjectIsEmpty;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.exception.table.TableNotFoundException;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.repository.ProjectRepository;
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
    private final ProjectRepository projectRepository;

    @Autowired
    public TableService(TableRepository tableRepository, ProjectRepository projectRepository) {
        this.tableRepository = tableRepository;
        this.projectRepository = projectRepository;
    }

    public TableEntity addTable(@NonNull TableEntity tableEntity) {
        log.info("Добавление table: ");
        TableEntity table = tableRepository.save(tableEntity);
        log.info("Table успешно добавлен: {}", table);
        return table;
    }


    public void delTableById(Integer tableId) {
        if (!tableRepository.existsById(tableId)) {
            throw new TableNotFoundException(tableId);
        }
        tableRepository.deleteById(tableId);
        log.info("Table with id {} deleted", tableId);
    }

    public TableEntity getTableById(Integer id) {
        TableEntity table = tableRepository.findById(id).orElseThrow(() -> new TableNotFoundException(id));
        log.info("Get = " + table);

        return table;
    }

    public List<TableEntity> getAllTable() {
        List<TableEntity> tableProjects = tableRepository.findAll();
        log.info("GetALL = " + tableProjects);
        return tableProjects;
    }

    public List<TableEntity> getAllTableByIdProject(Integer projectId) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        List<TableEntity> tableEntities = tableRepository.findByProjectId(projectId);

        if (tableEntities.isEmpty()) {
            log.info("No todos found for project with id {}", project);
            throw new ProjectIsEmpty();
        }
        return tableEntities;
    }

    public void delAllTablleByIdProject(Integer projectId) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        List<TableEntity> tableEntities = tableRepository.findByProjectId(projectId);

        if (tableEntities.isEmpty()) {
            log.info("No todos found for project with id {}", project);
            return;
        }
        tableRepository.deleteAll(tableEntities);
        log.info("All todos for project with id {} deleted", project);
    }


}
