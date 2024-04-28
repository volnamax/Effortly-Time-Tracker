package com.EffortlyTimeTracker.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("sql/CreateTables.sql");


    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.username", postgres::getUsername);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddUserWithInvalidData() throws Exception {
        String jsonBody = "{\"userName\": \"\", \"userSecondname\": \"\", \"email\": \"notanemail\", \"passwordHash\": \"123\", \"role\": \"UNKNOWN\"}";
        mockMvc.perform(post("/api/user/add")
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAddUser() throws Exception {
        String jsonBody = "{\"userName\": \"Test\", \"userSecondname\": \"User\", \"email\": \"test@example.com\", \"passwordHash\": \"123456\", \"role\": \"ADMIN\"}";
        mockMvc.perform(post("/api/user/add")
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("Test"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
    @Test
    public void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/user/get")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userName").isNotEmpty());
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/user/del")
                        .param("id", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/user/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").isNotEmpty());
    }
    @Test
    public void testGetUserUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/get")
                        .param("id", "1"))
                .andExpect(status().isUnauthorized());
    }
}