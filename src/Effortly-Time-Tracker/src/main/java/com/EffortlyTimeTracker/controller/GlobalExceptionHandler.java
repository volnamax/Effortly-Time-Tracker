package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.exception.project.ProjectNotFoundException;
import com.EffortlyTimeTracker.exception.project.InvalidProjectException;
import com.EffortlyTimeTracker.exception.user.InvalidUserException;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /// user handler
    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<?> handlerInvalidUserNameException(InvalidUserException ex) {
        log.error("ERROR: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(UserNotFoudException.class)
    public ResponseEntity<?> handleUserNotFoudException(UserNotFoudException ex) {
        log.error("ERROR: ", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
//    //////
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<?> handleProjectNotFoundException(ProjectNotFoundException ex) {
        log.error("Ошибка: ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidProjectException.class)
    public ResponseEntity<?> handleInvalidProjectException(InvalidProjectException ex) {
        log.error("Ошибка: ", ex); // Изменено для логирования всего исключения
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }



//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleException(Exception ex) {
//        log.error("Неизвестная ошибка: ", ex.getMessage());
//        return ResponseEntity.internalServerError().body("Внутренняя ошибка сервера");
//    }
}
