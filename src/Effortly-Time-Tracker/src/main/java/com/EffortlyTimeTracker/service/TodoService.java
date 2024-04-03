package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.TodoListDTO;
import com.EffortlyTimeTracker.entity.TodoList;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.repository.TodoRepository;
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


    public List<TodoList> getAllTodo() {
        List<TodoList> todoLists = todoRepository.findAll();
        log.info("GetALL = " + todoLists);
        return todoLists;
    }
}
