package com.EffortlyTimeTracker.unit;

import com.EffortlyTimeTracker.builder.TodoNodeEntityBuilder;
import com.EffortlyTimeTracker.builder.UserEntityBuilder;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Status;
import com.EffortlyTimeTracker.exception.todo.TodoNodeIsEmpty;
import com.EffortlyTimeTracker.exception.todo.TodoNotFoudException;
import com.EffortlyTimeTracker.repository.ITodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    @Mock
    private ITodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private TodoNodeEntity todo;

    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntityBuilder()
                .withUserId(1)
                .withUserName("TestUser")
                .withEmail("test@test.com")
                .build();

        todo = new TodoNodeEntityBuilder()
                .withId(1)
                .withContent("Test Todo")
                .withUser(user)
                .withStatus(Status.NO_ACTIVE)
                .build();
    }


    @Test
    void addTodoTest() {
        when(todoRepository.save(any(TodoNodeEntity.class))).thenReturn(todo);

        TodoNodeEntity savedTodo = todoService.addTodo(todo);
        assertNotNull(savedTodo);
        assertEquals("Test Todo", savedTodo.getContent());

        verify(todoRepository).save(todo);
    }

    @Test
    void deleteTodoByIdTestExists() {
        when(todoRepository.existsById(anyInt())).thenReturn(true);

        todoService.delTodoById(1);

        verify(todoRepository).deleteById(1);
    }

    @Test
    void deleteTodoByIdTestNotExists() {
        when(todoRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(TodoNotFoudException.class, () -> todoService.delTodoById(1));
    }

    @Test
    void deleteAllTodosByUserIdTest() {
        when(todoRepository.findByUserId(anyInt())).thenReturn(Arrays.asList(todo));

        todoService.delAllTodoByIdUser(1);

        verify(todoRepository).deleteAll(anyList());
    }


    @Test
    void deleteAllTodosByUserIdTestNoTodosFound() {
        when(todoRepository.findByUserId(anyInt())).thenReturn(Arrays.asList());

        todoService.delAllTodoByIdUser(1);

        verify(todoRepository, never()).deleteAll(anyList());
    }

    @Test
    void deleteTodoByIdWithInvalidId() {
        assertThrows(TodoNotFoudException.class, () -> todoService.delTodoById(null));
    }


    @Test
    void getAllTodosByUserIdTestNotFound() {
        when(todoRepository.findByUserId(anyInt())).thenReturn(Arrays.asList());  // No todos found

        Exception exception = assertThrows(TodoNodeIsEmpty.class, () -> {
            todoService.getAllTodoByIdUser(1);
        });

        assertEquals("Todo node is empty", exception.getMessage());
    }

    @Test
    void getTodoByIdTestFound() {
        when(todoRepository.existsById(anyInt())).thenReturn(true);
        when(todoRepository.findById(anyInt())).thenReturn(Optional.of(todo));

        TodoNodeEntity foundTodo = todoService.getTodoById(1);

        assertNotNull(foundTodo);
        assertEquals(todo.getTodoId(), foundTodo.getTodoId());
        assertEquals(todo.getContent(), foundTodo.getContent());
        verify(todoRepository, times(1)).findById(1);
    }

    @Test
    void getTodoByIdTestNotFound() {
        when(todoRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(TodoNotFoudException.class, () -> todoService.getTodoById(1));

        verify(todoRepository, never()).findById(1);
    }
    @Test
    void updateTodoStatusTestSuccess() {
        when(todoRepository.findById(anyInt())).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(TodoNodeEntity.class))).thenReturn(todo);

        TodoNodeEntity updatedTodo = todoService.updateTodoStatus(1, Status.ACTIVE);

        assertNotNull(updatedTodo);
        assertEquals(Status.ACTIVE, updatedTodo.getStatus());
        verify(todoRepository).save(updatedTodo);
    }

    @Test
    void updateTodoStatusTestNotFound() {
        when(todoRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(TodoNotFoudException.class, () -> todoService.updateTodoStatus(1, Status.ACTIVE));

        verify(todoRepository, never()).save(any());
    }
}
