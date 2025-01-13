package com.EffortlyTimeTracker.unit;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.exception.table.TableIsEmpty;
import com.EffortlyTimeTracker.exception.table.TableNotFoundException;
import com.EffortlyTimeTracker.repository.IProjectRepository;
import com.EffortlyTimeTracker.repository.ITableRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TableService {
    private final ITableRepository tableRepository;
    private final IProjectRepository postgresRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public TableService(ITableRepository tableRepository, IProjectRepository postgresRepository,  @Autowired(required = false) SequenceGeneratorService sequenceGeneratorService) {
        this.tableRepository = tableRepository;
        this.postgresRepository = postgresRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }


    public TableEntity addTable(@NonNull TableEntity tableEntity) {
        ProjectEntity project = postgresRepository.findById(tableEntity.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(tableEntity.getProjectId()));
        tableEntity.setProject(project);


        if (sequenceGeneratorService != null) {
            // Если активен профиль 'mongo', используем SequenceGeneratorService
            tableEntity.setTableId((int) sequenceGeneratorService.generateSequence(TableEntity.class.getSimpleName()));
        } else {
            // Если активен профиль 'postgres', полагаемся на автоинкремент в БД
            tableEntity.setTableId(null); // Или не устанавливаем значение вручную
        }
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
