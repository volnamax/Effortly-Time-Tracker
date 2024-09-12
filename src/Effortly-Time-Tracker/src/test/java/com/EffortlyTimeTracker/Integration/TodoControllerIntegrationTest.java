package com.EffortlyTimeTracker.Integration;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoControllerIntegrationTest extends AbstractContainerBaseTest {




    @Test
    @Order(1)
    public void testAddTodo() throws Exception {
        testAddUser();
        String jsonBody = "{\"userID\": \"1\", \"content\": \"New task\", \"status\": \"ACTIVE\", \"priority\": \"IMPORTANT_URGENTLY\"}";
        mockMvc.perform(post("/api/todo/add")
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("New task"));
    }

    @Test
    @Order(2)
    public void testGetAllTodosByUserId() throws Exception {
        mockMvc.perform(get("/api/todo/get-all-by-user-id")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(3)
    public void testGetAllTodos() throws Exception {
        mockMvc.perform(get("/api/todo/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(4)
    public void testDeleteTodoById() throws Exception {
        mockMvc.perform(delete("/api/todo/del")
                        .param("TodoId", "1")) // Assuming this ID exists or is mocked accordingly
                .andExpect(status().isNoContent());
    }


}