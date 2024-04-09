package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.TaskDTO;
import com.EffortlyTimeTracker.entity.TaskEntity;
import com.EffortlyTimeTracker.exception.task.TaskNotFoundException;
import com.EffortlyTimeTracker.repository.TaskRepository;
import com.EffortlyTimeTracker.service.TaskService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private TaskDTO taskDTO;
    private TaskEntity task;

    @BeforeEach
    void setUp() {
        taskDTO = new TaskDTO();
        taskDTO.setName("Test Task");
        taskDTO.setDescription("Test Description");
        taskDTO.setStatus("ACTIVE");
        taskDTO.setSumTimer(0L);
        taskDTO.setStartTimer(LocalDateTime.now());
        taskDTO.setTimeAddTask(LocalDateTime.now());
        taskDTO.setTimeEndTask(LocalDateTime.now());
        // Assuming setTags and setTable are handled appropriately here

        task = TaskEntity.builder()
                .taskId(1)
                .name(taskDTO.getName())
                .description(taskDTO.getDescription())
//                .status(taskDTO.getStatus())
                .sumTimer(taskDTO.getSumTimer())
                .startTimer(taskDTO.getStartTimer())
                .timeAddTask(taskDTO.getTimeAddTask())
                .timeEndTask(taskDTO.getTimeEndTask())
                // Assuming tags and table are set here
                .build();
    }

    @Test
    void addTask_ShouldReturnNewTask() {
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(task);

        TaskEntity createdTask = taskService.addTask(taskDTO);

        assertNotNull(createdTask);
        assertEquals(taskDTO.getName(), createdTask.getName());
        verify(taskRepository).save(any(TaskEntity.class));
    }

    @Test
    void delTaskById_ShouldDeleteTask() {
        when(taskRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(taskRepository).deleteById(anyInt());

        taskService.delTaskById(task.getTaskId());

        verify(taskRepository).deleteById(task.getTaskId());
    }

    @Test
    void getTaskById_ShouldReturnTask() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));

        TaskEntity foundTask = taskService.getTaskById(task.getTaskId());

        assertNotNull(foundTask);
        assertEquals(task.getTaskId(), foundTask.getTaskId());
        verify(taskRepository).findById(task.getTaskId());
    }

    @Test
    void getTaskById_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(999));

        verify(taskRepository).findById(999);
    }
    @Test
    void getAllTask_ShouldReturnListOfTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

        List<TaskEntity> tasks = taskService.getAllTask();

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        verify(taskRepository).findAll();
    }
    
}