package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.builder.RoleEntityBuilder;
import com.EffortlyTimeTracker.builder.TodoNodeEntityBuilder;
import com.EffortlyTimeTracker.builder.UserEntityBuilder;
import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.enums.Status;
import com.EffortlyTimeTracker.repository.ITodoRepository;
import com.EffortlyTimeTracker.repository.postgres.RoleRepository;
import com.EffortlyTimeTracker.repository.postgres.UserPostgresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.repository.CrudRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class TodoServiceIntegrationTest {
    @Autowired
    private RoleRepository repository;

    @Autowired
    private ITodoRepository todoRepository;

    @Autowired
    private UserPostgresRepository userRepository;

    private TodoNodeEntity todo;
    private UserEntity user;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        // Создаем пользователя и сохраняем в базу данных
        RoleEntity roleEntity = new RoleEntityBuilder()
                .withRole(Role.ADMIN)
                .build();
        roleEntity = roleRepository.save(roleEntity);

        user = new UserEntityBuilder()
                .withUserId(1)
                .withUserName("TestUser")
                .withUserSecondName("TestSecondName")
                .withEmail("teweft@example.com")
                .withPasswordHash("password")
                .withRole(roleEntity)
                .build();
        ((CrudRepository<UserEntity, Integer>) userRepository).save(user);
        userRepository.flush();

        todo = new TodoNodeEntityBuilder()
                .withId(1)
                .withContent("Test Todo")
                .withUser(user)
                .withStatus(Status.NO_ACTIVE)
                .build();
        todoRepository.save(todo);
    }

    @Test
    void addTodoIntegrationTest() {
        TodoNodeEntity savedTodo = todoRepository.save(todo);

        assertNotNull(savedTodo);
        assertEquals("Test Todo", savedTodo.getContent());

        TodoNodeEntity foundTodo = todoRepository.findById(savedTodo.getTodoId()).orElse(null);

        assertNotNull(foundTodo);
        assertEquals(todo.getTodoId(), foundTodo.getTodoId());
    }
























//
//    @Test
//    void getTodoByIdIntegrationTest() {
//        // Получаем Todo из базы данных
//        TodoNodeEntity foundTodo = todoRepository.findById(todo.getTodoId()).orElse(null);
//
//        // Проверяем, что объект не null и данные корректны
//        assertNotNull(foundTodo);
//        assertEquals(todo.getTodoId(), foundTodo.getTodoId());
//        assertEquals("Test Todo", foundTodo.getContent());
//    }
//
//    @Test
//    void deleteTodoIntegrationTest() {
//        // Удаляем Todo
//        todoRepository.deleteById(todo.getTodoId());
//
//        // Проверяем, что элемент удален
//        TodoNodeEntity deletedTodo = todoRepository.findById(todo.getTodoId()).orElse(null);
//        assertNull(deletedTodo);
//    }
}
