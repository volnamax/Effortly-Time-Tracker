package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Status;
import com.EffortlyTimeTracker.exception.todo.TodoNodeIsEmpty;
import com.EffortlyTimeTracker.exception.todo.TodoNotFoudException;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.repository.TodoRepository;
import com.EffortlyTimeTracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public TodoNodeEntity addTodo(TodoNodeEntity todoNodeEntity) {
          return todoRepository.save(todoNodeEntity);
    }

    public void delTodoById(Integer id) {
        if (!todoRepository.existsById(id)) {
            throw new TodoNotFoudException(id);
        }
        todoRepository.deleteById(id);
    }
    public TodoNodeEntity getTodoById(Integer id) {
        if (!todoRepository.existsById(id)) {
            throw new TodoNotFoudException(id);
        }
        return todoRepository.findById(id).orElseThrow(() -> new TodoNotFoudException(id));
    }


    public void delAllTodoByIdUser(Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoudException(userId));

        List<TodoNodeEntity> userTodos = todoRepository.findByUserId(userId);

        if (userTodos.isEmpty()) {
            log.info("No todos found for user with id {}", userId);
            return;
        }
        todoRepository.deleteAll(userTodos);
    }


    public List<TodoNodeEntity> getAllTodo() {
        return todoRepository.findAll();
    }

    public List<TodoNodeEntity> getAllTodoByIdUser(Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoudException(userId));

        List<TodoNodeEntity> userTodos = todoRepository.findByUserId(userId);

        if (userTodos.isEmpty()) {
            log.info("No todos found for user with id {}", userId);
            throw new TodoNodeIsEmpty();
        }
        return userTodos;
    }

    public TodoNodeEntity updateTodoStatus(Integer id, Status status) {
        TodoNodeEntity todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoudException(id));

        todo.setStatus(status);  // Обновляем статус
        return todoRepository.save(todo);  // Сохраняем изменения
    }
}
