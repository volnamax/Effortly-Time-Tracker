package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.TodoListDTO;
import com.EffortlyTimeTracker.DTO.UserDTO;
import com.EffortlyTimeTracker.entity.TodoList;
import com.EffortlyTimeTracker.entity.User;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.repository.TodoRepository;
import com.EffortlyTimeTracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
        // todo
    public TodoList addTodo(TodoListDTO todoListDTO) {
        TodoList todo = todoRepository.save(TodoList.builder()
                        .content(todoListDTO.getContent())
                        .userTodo(todoListDTO.getUserTodo())
                        .dueData(todoListDTO.getDueData())
                        .status(TodoList.Status.valueOf(todoListDTO.getStatus()))
                        .priority(TodoList.Priority.valueOf(todoListDTO.getPriority()))
                .build());

        log.info("New" + todo);
        return todo;
    }

    public void delTodoById(Integer id) {
        if (!todoRepository.existsById(id)) {
            throw new UserNotFoudException(id);
        }
        todoRepository.deleteById(id);
        log.info("user with id {} delete", id);
    }

    public User getTodoById(Integer id) {
        User user  = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoudException(id));
        log.info("Get = " + user);

        return user;
    }

    public List<User> getAllTodo() {
        List<User> users = userRepository.findAll();
        log.info("GetALL = " + users);
        return users;
    }
}
