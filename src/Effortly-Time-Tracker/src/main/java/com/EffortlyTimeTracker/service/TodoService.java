package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.DTO.TodoNodeDTO;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.exception.todo.TodoNotFoudException;
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
    public TodoNodeEntity addTodo(TodoNodeDTO todoNodeDTO) {
        TodoNodeEntity todo = todoRepository.save(TodoNodeEntity.builder()
                .content(todoNodeDTO.getContent())
                .userTodo(todoNodeDTO.getUserTodo())
                .dueData(todoNodeDTO.getDueData())
                .status(TodoNodeEntity.Status.valueOf(todoNodeDTO.getStatus()))
                .priority(TodoNodeEntity.Priority.valueOf(todoNodeDTO.getPriority()))
                .build());

        log.info("New" + todo);
        return todo;
    }

    public void delTodoById(Integer id) {
        if (!todoRepository.existsById(id)) {
            throw new TodoNotFoudException(id);
        }
        todoRepository.deleteById(id);
        log.info("todo with id {} delete", id);
    }

    public List<TodoNodeEntity> getAllTodo() {
        //        log.info("GetALL = " + todoNodes);
        return todoRepository.findAll();
    }
}
