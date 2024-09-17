//package com.EffortlyTimeTracker.ownerShip;
//
//import com.EffortlyTimeTracker.builder.TodoNodeEntityBuilder;
//import com.EffortlyTimeTracker.builder.UserEntityBuilder;
//import com.EffortlyTimeTracker.entity.TodoNodeEntity;
//import com.EffortlyTimeTracker.entity.UserEntity;
//import com.EffortlyTimeTracker.enums.Status;
//import com.EffortlyTimeTracker.service.TodoService;
//import com.EffortlyTimeTracker.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.Optional;
//
//import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//public class OwnershipTodoAspectTest {
//
//    @Autowired
//    private TodoService todoService;
//
//    @Autowired
//    private UserService userService;
//
//    @Test
//    @WithMockUser(username = "test@example.com", roles = {"USER"})
//    public void checkTaskOwnership_UserIsOwner_ShouldAllow() {
//        // Подготовка данных
//        UserEntity user = new UserEntityBuilder()
//                .withUserId(1)
//                .withUserName("Test User")
//                .withEmail("test@example.com")
//                .build();
//
//        TodoNodeEntity todo = new TodoNodeEntityBuilder()
//                .withId(1)
//                .withUser(user)
//                .withStatus(Status.ACTIVE)
//                .build();
//
//        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));
//        when(todoService.getTodoById(1)).thenReturn(todo);
//
//        // Выполнение метода с аспектом
//        boolean isOwner = todoService.isTaskOwner(1);
//
//        // Проверка
//        assertTrue(isOwner);
//        verify(todoService, times(1)).getTodoById(1);
//    }
//
//    @Test
//    @WithMockUser(username = "test@example.com", roles = {"USER"})
//    public void checkTaskOwnership_UserIsNotOwner_ShouldDeny() {
//        // Подготовка данных
//        UserEntity owner = new UserEntityBuilder()
//                .withUserId(2)
//                .withUserName("Other User")
//                .withEmail("other@example.com")
//                .build();
//
//        TodoNodeEntity todo = new TodoNodeEntityBuilder()
//                .withId(1)
//                .withUser(owner)
//                .withStatus(Status.ACTIVE)
//                .build();
//
//        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(new UserEntityBuilder().withUserId(1).build()));
//        when(todoService.getTodoById(1)).thenReturn(todo);
//
//        // Проверка, что будет выброшено исключение AccessDeniedException
//        assertThrows(AccessDeniedException.class, () -> {
//            todoService.is(1);
//        });
//
//        verify(todoService, times(1)).getTodoById(1);
//    }
//}
