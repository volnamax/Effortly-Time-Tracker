package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.exception.BaseException;
import com.EffortlyTimeTracker.exception.group.GroupNotFoundException;
import com.EffortlyTimeTracker.exception.group.InvalidGroupException;
import com.EffortlyTimeTracker.exception.project.InvalidProjectException;
import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.exception.table.InvalidTableException;
import com.EffortlyTimeTracker.exception.table.TableNotFoundException;
import com.EffortlyTimeTracker.exception.tag.InvalidTagException;
import com.EffortlyTimeTracker.exception.tag.TagNotFoundException;
import com.EffortlyTimeTracker.exception.task.InvalidTaskException;
import com.EffortlyTimeTracker.exception.task.TaskNotFoundException;
import com.EffortlyTimeTracker.exception.todo.InvalidTodoException;
import com.EffortlyTimeTracker.exception.todo.TodoNodeIsEmpty;
import com.EffortlyTimeTracker.exception.todo.TodoNotFoudException;
import com.EffortlyTimeTracker.exception.user.InvalidUserException;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.exception.user.UserNotRoleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

//todo after add all exeption
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    // middleware err
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    // Add handlers for user-related exceptions

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>("Error: The provided role must be an integer.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>("Error: Invalid input data format.", HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<?> handleInvalidUserException(InvalidUserException ex) {
        log.error("Invalid user exception", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoudException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoudException ex) {
        log.error("User not found exception", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Add handlers for project-related exceptions
    @ExceptionHandler(InvalidProjectException.class)
    public ResponseEntity<?> handleInvalidProjectException(InvalidProjectException ex) {
        log.error("Invalid project exception", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<?> handleProjectNotFoundException(ProjectNotFoundException ex) {
        log.error("Project not found exception", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Add handlers for table-related exceptions
    @ExceptionHandler(InvalidTableException.class)
    public ResponseEntity<?> handleInvalidTableException(InvalidTableException ex) {
        log.error("Invalid table exception", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TableNotFoundException.class)
    public ResponseEntity<?> handleTableNotFoundException(TableNotFoundException ex) {
        log.error("Table not found exception", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


    // Add a general handler for any other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleBaseException(BaseException ex) {
        log.error("Base exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    // Add handlers for tag-related exceptions
    @ExceptionHandler(InvalidTagException.class)
    public ResponseEntity<?> handleInvalidTagException(InvalidTagException ex) {
        log.error("Invalid tag exception", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<?> handleTagNotFoundException(TagNotFoundException ex) {
        log.error("Tag not found exception", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Add handlers for task-related exceptions
    @ExceptionHandler(InvalidTaskException.class)
    public ResponseEntity<?> handleInvalidTaskException(InvalidTaskException ex) {
        log.error("Invalid task exception", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<?> handleTaskNotFoundException(TaskNotFoundException ex) {
        log.error("Task not found exception", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Add handlers for уtodo-related exceptions
    @ExceptionHandler(InvalidTodoException.class)
    public ResponseEntity<?> handleInvalidTodoException(InvalidTodoException ex) {
        log.error("Invalid todo exception", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TodoNotFoudException.class)
    public ResponseEntity<?> handleTodoNotFoundException(TodoNotFoudException ex) {
        log.error("Todo not found exception", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TodoNodeIsEmpty.class)
    public ResponseEntity<?> handleTodoNodeIsEmpty(TodoNodeIsEmpty ex) {
        log.error("Todo node is empty exception", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Add handlers for group-related exceptions
    @ExceptionHandler(InvalidGroupException.class)
    public ResponseEntity<?> handleInvalidGroupException(InvalidGroupException ex) {
        log.error("Invalid group exception", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<?> handleGroupNotFoundException(GroupNotFoundException ex) {
        log.error("Group not found exception", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Add handlers for user role-related exceptions
    @ExceptionHandler(UserNotRoleException.class)
    public ResponseEntity<?> handleUserNotRoleException(UserNotRoleException ex) {
        log.error("User not role exception", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
