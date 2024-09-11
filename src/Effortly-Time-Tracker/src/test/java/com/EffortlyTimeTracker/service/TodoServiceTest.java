package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.exception.todo.TodoNodeIsEmpty;
import com.EffortlyTimeTracker.exception.todo.TodoNotFoudException;
import com.EffortlyTimeTracker.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private TodoService todoService;

    private UserEntity user;
    private TodoNodeEntity todo;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setUserId(1);
        user.setUserName("TestUser");

        todo = new TodoNodeEntity();
        todo.setTodoId(1);
        todo.setContent("Test Todo");
        todo.setUser(user);
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
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(todoRepository.findByUserId(anyInt())).thenReturn(Arrays.asList(todo));

        todoService.delAllTodoByIdUser(1);

        verify(todoRepository).deleteAll(anyList());
    }

    @Test
    void deleteAllTodosByUserIdTestNoTodosFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(todoRepository.findByUserId(anyInt())).thenReturn(Arrays.asList());

        todoService.delAllTodoByIdUser(1);

        verify(todoRepository, never()).deleteAll(anyList());
    }

    @Test
    void getAllTodosByUserIdTestNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(todoRepository.findByUserId(anyInt())).thenReturn(Arrays.asList());  // No todos found

        Exception exception = assertThrows(TodoNodeIsEmpty.class, () -> {
            todoService.getAllTodoByIdUser(1);
        });

        assertEquals("Todo node is empty", exception.getMessage());
    }

}
