package com.EffortlyTimeTracker.service;


import com.EffortlyTimeTracker.entity.TableEntity;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.enums.Status;
import com.EffortlyTimeTracker.exception.table.TableIsEmpty;
import com.EffortlyTimeTracker.exception.table.TableNotFoundException;
import com.EffortlyTimeTracker.exception.task.TaskNotFoundException;
import com.EffortlyTimeTracker.repository.postgres.TableRepository;
import com.EffortlyTimeTracker.repository.postgres.TaskPostgresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskPostgresRepository taskRepository;

    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    private TaskService taskService;

    private TaskEntity task;
    private TableEntity table;
    private TaskEntity taskWithTimer;

    @BeforeEach
    void setUp() {
        table = new TableEntity();
        table.setTableId(1);

        task = new TaskEntity();
        task.setTaskId(1);
        task.setName("Test Task");
        task.setTable(table);

        taskWithTimer = new TaskEntity();
        taskWithTimer.setTaskId(2);
        taskWithTimer.setName("Test Task with Timer");
        taskWithTimer.setStatus(Status.ACTIVE);
        taskWithTimer.setStartTimer(LocalDateTime.now().minusMinutes(5));
        taskWithTimer.setSumTimer(0L);
    }

    @Test
    void addTaskTest() {
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);

        TaskEntity savedTask = taskService.addTask(task);
        assertNotNull(savedTask);
        assertEquals("Test Task", savedTask.getName());

        verify(taskRepository).save(task);
    }

    @Test
    void deleteTaskByIdTestExists() {
        when(taskRepository.existsById(anyInt())).thenReturn(true);

        taskService.delTaskById(1);

        verify(taskRepository).deleteById(1);
    }

    @Test
    void deleteTaskByIdTestNotExists() {
        when(taskRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.delTaskById(1));
    }

    @Test
    void getTaskByIdTestFound() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));

        TaskEntity foundTask = taskService.getTaskById(1);

        assertNotNull(foundTask);
        assertEquals("Test Task", foundTask.getName());
    }

    @Test
    void getTaskByIdTestNotFound() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1));
    }

    @Test
    void getAllTaskTest() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

        List<TaskEntity> tasks = taskService.getAllTask();
        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getName());
    }

    @Test
    void getAllTaskByIdTableTestFound() {
        when(tableRepository.findById(anyInt())).thenReturn(Optional.of(table));
        when(taskRepository.findByTableId(anyInt())).thenReturn(Arrays.asList(task));

        List<TaskEntity> tasks = taskService.getAllTaskByIdTable(1);
        assertFalse(tasks.isEmpty());
        assertEquals("Test Task", tasks.get(0).getName());
    }

    @Test
    void getAllTaskByIdTableTestTableNotFound() {
        when(tableRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(TableNotFoundException.class, () -> taskService.getAllTaskByIdTable(1));
    }

    @Test
    void getAllTaskByIdTableTestTasksEmpty() {
        when(tableRepository.findById(anyInt())).thenReturn(Optional.of(table));
        when(taskRepository.findByTableId(anyInt())).thenReturn(Arrays.asList());

        assertThrows(TableIsEmpty.class, () -> taskService.getAllTaskByIdTable(1));
    }

    @Test
    void delAllTaskByIdTableTest() {
        when(tableRepository.findById(anyInt())).thenReturn(Optional.of(table));
        when(taskRepository.findByTableId(anyInt())).thenReturn(Arrays.asList(task));

        taskService.delAllTaskByIdTable(1);

        verify(taskRepository).deleteAll(anyList());
    }

    @Test
    void delAllTaskByIdTableTestNoTasks() {
        when(tableRepository.findById(anyInt())).thenReturn(Optional.of(table));
        when(taskRepository.findByTableId(anyInt())).thenReturn(Arrays.asList());

        taskService.delAllTaskByIdTable(1);

        verify(taskRepository, never()).deleteAll(anyList());
    }

    @Test
    void startTaskTimerTest() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);

        TaskEntity updatedTask = taskService.startTaskTimer(1);

        assertNotNull(updatedTask.getStartTimer());
        verify(taskRepository).findById(1);
        verify(taskRepository).save(task);
    }

    @Test
    void startTaskTimerAlreadyRunningTest() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(taskWithTimer));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            taskService.startTaskTimer(2);
        });

        assertEquals("Timer is already running for this task.", exception.getMessage());
        verify(taskRepository).findById(2);
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void stopTaskTimerTest() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(taskWithTimer));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskWithTimer);

        TaskEntity updatedTask = taskService.stopTaskTimer(2);

        assertNull(updatedTask.getStartTimer());
        assertTrue(updatedTask.getSumTimer() > 0);
        verify(taskRepository).findById(2);
        verify(taskRepository).save(taskWithTimer);
    }

    @Test
    void stopTaskTimerNotRunningTest() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            taskService.stopTaskTimer(1);
        });

        assertEquals("Timer is not running for this task.", exception.getMessage());
        verify(taskRepository).findById(1);
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void completeTaskTest() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(taskWithTimer));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskWithTimer);

        TaskEntity updatedTask = taskService.completeTask(2);

        assertEquals(Status.NO_ACTIVE, updatedTask.getStatus());
        assertNotNull(updatedTask.getTimeEndTask());
        assertTrue(updatedTask.getSumTimer() > 0);
        verify(taskRepository).findById(2);
        verify(taskRepository).save(taskWithTimer);
    }

    @Test
    void completeTaskAlreadyCompletedTest() {
        task.setStatus(Status.NO_ACTIVE);
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            taskService.completeTask(1);
        });

        assertEquals("Task is already completed.", exception.getMessage());
        verify(taskRepository).findById(1);
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void completeTaskTest_TaskNotFound() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.completeTask(1);
        });

        assertEquals("Task with ID 1 not found", exception.getMessage());
        verify(taskRepository).findById(1);
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }



}