package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.TodoNodeDTO;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.exception.todo.TodoNotFoudException;
import com.EffortlyTimeTracker.repository.TodoRepository;
import com.EffortlyTimeTracker.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void testAddTodo() {
        TodoNodeDTO todoNodeDTO = new TodoNodeDTO();
        todoNodeDTO.setContent("Test content");
        todoNodeDTO.setStatus("ACTIVE");
        todoNodeDTO.setPriority("IMPORTANT_URGENTLY");

        TodoNodeEntity expectedTodoNode = new TodoNodeEntity();
        expectedTodoNode.setContent(todoNodeDTO.getContent());
        expectedTodoNode.setStatus(TodoNodeEntity.Status.valueOf(todoNodeDTO.getStatus()));
        expectedTodoNode.setPriority(TodoNodeEntity.Priority.valueOf(todoNodeDTO.getPriority()));

        when(todoRepository.save(any(TodoNodeEntity.class))).thenReturn(expectedTodoNode);

        TodoNodeEntity resultTodoNode = todoService.addTodo(todoNodeDTO);

        assertNotNull(resultTodoNode);
        assertEquals(expectedTodoNode.getContent(), resultTodoNode.getContent());
        assertEquals(expectedTodoNode.getStatus(), resultTodoNode.getStatus());
        assertEquals(expectedTodoNode.getPriority(), resultTodoNode.getPriority());
        verify(todoRepository).save(any(TodoNodeEntity.class));
    }

    @Test
    void testDelTodoById_ExistingId() {
        Integer id = 1;
        when(todoRepository.existsById(id)).thenReturn(true);

        todoService.delTodoById(id);

        verify(todoRepository).deleteById(id);
    }

    @Test
    void testDelTodoById_NonExistingId() {
        Integer id = 1;
        when(todoRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(TodoNotFoudException.class, () -> todoService.delTodoById(id));
        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }


    @Test
    void testGetAllTodo() {
        List<TodoNodeEntity> expectedTodoNodes = Arrays.asList(new TodoNodeEntity(), new TodoNodeEntity());
        when(todoRepository.findAll()).thenReturn(expectedTodoNodes);

        List<TodoNodeEntity> resultTodoNodes = todoService.getAllTodo();

        assertNotNull(resultTodoNodes);
        assertEquals(2, resultTodoNodes.size());
        verify(todoRepository).findAll();
    }

}
