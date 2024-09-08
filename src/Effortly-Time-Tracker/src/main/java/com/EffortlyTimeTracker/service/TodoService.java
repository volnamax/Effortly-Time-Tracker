package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Status;
import com.EffortlyTimeTracker.exception.todo.TodoNodeIsEmpty;
import com.EffortlyTimeTracker.exception.todo.TodoNotFoudException;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.mapper.UserMongoMapper;
import com.EffortlyTimeTracker.repository.TodoRepository;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.mongo.IMongoUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;
    private final IUserRepository userPostgresRepository;
    private final IMongoUserRepository userMongoRepository;
    private final String activeDb;

    @Autowired
    public TodoService(
            TodoRepository todoRepository,
            @Qualifier("userPostgresRepository") IUserRepository userPostgresRepository,
            @Qualifier("userMongoRepository") IMongoUserRepository userMongoRepository,
            @Value("${app.active-db}") String activeDb) {

        this.todoRepository = todoRepository;
        this.userPostgresRepository = userPostgresRepository;
        this.userMongoRepository = userMongoRepository;
        this.activeDb = activeDb;
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
        UserEntity user = findUserById(userId);

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
        UserEntity user = findUserById(userId);

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

    // Метод для поиска пользователя с выбором репозитория на основе активной базы данных
    private UserEntity findUserById(Integer userId) {
        if ("postgres".equalsIgnoreCase(activeDb)) {
            return userPostgresRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoudException(userId));
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            return userMongoRepository.findById(userId.toString())
                    .map(UserMongoMapper::toUserEntity)
                    .orElseThrow(() -> new UserNotFoudException(userId));
        } else {
            throw new IllegalStateException("Unknown database type: " + activeDb);
        }
    }
}
