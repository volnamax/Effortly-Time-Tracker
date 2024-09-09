package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.exception.table.TableNotFoundException;
import com.EffortlyTimeTracker.repository.postgres.ProjectRepository;
import com.EffortlyTimeTracker.repository.postgres.TableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private TableRepository tableRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TableService tableService;

    private TableEntity table;
    private ProjectEntity project;

    @BeforeEach
    void setUp() {
        project = new ProjectEntity();
        project.setProjectId(1);
        project.setName("Test Project");

        table = new TableEntity();
        table.setTableId(1);
        table.setProject(project);
        table.setName("Test Table");
    }

    @Test
    void addTableTest() {
        when(tableRepository.save(any(TableEntity.class))).thenReturn(table);

        TableEntity savedTable = tableService.addTable(table);
        assertNotNull(savedTable);
        assertEquals("Test Table", savedTable.getName());

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
        assertEquals("Test Table", foundTable.getName());
    }

    @Test
    void getTableByIdTestNotFound() {
        when(tableRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(TableNotFoundException.class, () -> tableService.getTableById(1));
    }


    @Test
    void getAllTableByIdProjectTestFound() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(tableRepository.findByProjectId(anyInt())).thenReturn(Arrays.asList(table));

        List<TableEntity> tables = tableService.getAllTableByIdProject(1);
        assertFalse(tables.isEmpty());
        assertEquals(1, tables.size());
        assertEquals("Test Table", tables.get(0).getName());
    }


    @Test
    void getAllTableByIdProjectTestNotFound() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> tableService.getAllTableByIdProject(1));
    }

//    @Test
//    void getAllTableByIdProjectTestEmpty() {
//        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
//        when(tableRepository.findByProjectId(anyInt())).thenReturn(Arrays.asList());
//
//        assertThrows(ProjectIsEmpty.class, () -> tableService.getAllTableByIdProject(1));
//    }

    @Test
    void delAllTableByIdProjectTest() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(tableRepository.findByProjectId(anyInt())).thenReturn(Arrays.asList(table));

        tableService.delAllTablleByIdProject(1);

        verify(tableRepository).deleteAll(anyList());
    }

    @Test
    void delAllTableByIdProjectTestNoTablesFound() {
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
        when(tableRepository.findByProjectId(anyInt())).thenReturn(Arrays.asList());

        tableService.delAllTablleByIdProject(1);

        verify(tableRepository, never()).deleteAll(anyList());
    }

}