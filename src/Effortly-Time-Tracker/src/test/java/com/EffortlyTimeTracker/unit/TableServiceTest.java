package com.EffortlyTimeTracker.unit;

import com.EffortlyTimeTracker.builder.ProjectEntityBuilder;
import com.EffortlyTimeTracker.builder.TableEntityBuilder;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.enums.Status;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.exception.table.TableIsEmpty;
import com.EffortlyTimeTracker.exception.table.TableNotFoundException;
import com.EffortlyTimeTracker.repository.IProjectRepository;
import com.EffortlyTimeTracker.repository.ITableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private SequenceGeneratorService sequenceGeneratorService;

    @Mock
    private ITableRepository tableRepository;

    @Mock
    private IProjectRepository projectPostgresRepository;

    @InjectMocks
    private TableService tableService;

    private TableEntity table;
    private ProjectEntity project;

    @BeforeEach
    void setUp() {
        project = new ProjectEntityBuilder()
                .withName("Test Project")
                .withProjectId(1)
                .build();

        table = new TableEntityBuilder()
                .withTableId(1)
                .withName("Project Table")
                .withDescription("Table description")
                .withStatus(Status.ACTIVE)
                .withProject(project) // где project — это объект ProjectEntity
                .build();
    }

    @Test
    void addTableTest() {
        when(sequenceGeneratorService.generateSequence(anyString())).thenReturn(1L);
        project.setProjectId(1);
        table.setProject(project);
        table.setProjectId(1);

        when(projectPostgresRepository.findById(1)).thenReturn(Optional.of(project));
        when(tableRepository.save(any(TableEntity.class))).thenReturn(table);

        TableEntity savedTable = tableService.addTable(table);

        assertNotNull(savedTable);
        assertEquals("Project Table", savedTable.getName());
        assertEquals(1, savedTable.getProject().getProjectId());

        verify(tableRepository).save(table);
    }

    @Test
    void deleteTableByIdTestExists() {
        when(tableRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(tableRepository).deleteById(anyInt());

        tableService.delTableById(1);

        verify(tableRepository).deleteById(1);
    }

    @Test
    void deleteTableByIdTestNotExists() {
        when(tableRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(TableNotFoundException.class, () -> tableService.delTableById(1));
    }

    @Test
    void getTableByIdTestFound() {
        when(tableRepository.findById(anyInt())).thenReturn(Optional.of(table));

        TableEntity foundTable = tableService.getTableById(1);

        assertNotNull(foundTable);
        assertEquals("Project Table", foundTable.getName());
    }

    @Test
    void getTableByIdTestNotFound() {
        when(tableRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(TableNotFoundException.class, () -> tableService.getTableById(1));
    }


    @Test
    void getAllTableByIdProjectTestFound() {
        when(projectPostgresRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(tableRepository.findByProjectId(anyInt())).thenReturn(Arrays.asList(table));

        List<TableEntity> tables = tableService.getAllTableByIdProject(1);
        assertFalse(tables.isEmpty());
        assertEquals(1, tables.size());
        assertEquals("Project Table", tables.get(0).getName());
    }


    @Test
    void getAllTableByIdProjectTestNotFound() {
        when(projectPostgresRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> tableService.getAllTableByIdProject(1));
    }

    @Test
    void getAllTableByIdProjectTestEmpty() {
        when(projectPostgresRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(tableRepository.findByProjectId(anyInt())).thenReturn(Collections.emptyList());

        assertThrows(TableIsEmpty.class, () -> tableService.getAllTableByIdProject(1));
    }

    @Test
    void delAllTableByIdProjectTest() {
        when(projectPostgresRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(tableRepository.findByProjectId(anyInt())).thenReturn(Arrays.asList(table));

        tableService.delAllTablleByIdProject(1);

        verify(tableRepository).deleteAll(anyList());
    }

    @Test
    void delAllTableByIdProjectTestNoTablesFound() {
        when(projectPostgresRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(tableRepository.findByProjectId(anyInt())).thenReturn(Arrays.asList());

        tableService.delAllTablleByIdProject(1);

        verify(tableRepository, never()).deleteAll(anyList());
    }

}