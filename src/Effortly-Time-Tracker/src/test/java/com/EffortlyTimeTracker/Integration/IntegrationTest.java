package com.EffortlyTimeTracker.Integration;

import org.junit.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTest {
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
    public MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String authToken;

    @BeforeEach
    public void setUp() throws Exception {
        // Generate a unique email address
        String uniqueEmail = "testuser_" + UUID.randomUUID() + "@example.com";

        // Create test user
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        jdbcTemplate.update("INSERT INTO user_app (email, password, user_name, user_secondname, role_id) VALUES (?, ?, ?, ?, ?)",
                uniqueEmail, encodedPassword, "Test", "User", 1);

        System.out.println("User created with email: " + uniqueEmail);
        System.out.println("Encoded password: " + encodedPassword);

        // Authenticate and get token
        String jsonBody = "{\"username\": \"" + uniqueEmail + "\", \"password\": \"" + rawPassword + "\"}";
        String response = mockMvc.perform(post("/api/authenticate")
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("Response from /api/authenticate: " + response);

        authToken = "Bearer " + response;
        System.out.println("Auth token: " + authToken);
    }


    @AfterEach
    void displayAllTablesContent() {
        System.out.println("Displaying all tables content after each test");
        List<String> tables = List.of("user_app", "roles", "todo_node", "project", "table_app", "task", "tag", "task_tag", "group_user", "group_member");
        tables.forEach(table -> {
            System.out.println("Contents of table " + table + ":");
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM " + table);
            rows.forEach(row -> System.out.println(row.toString()));
        });
    }

    @Test
    @Order(1)
    public void testAddUserWithInvalidData() throws Exception {
        String jsonBody = "{\"userName\": \"\", \"userSecondname\": \"\", \"email\": \"notanemail\", \"passwordHash\": \"123\", \"role\": \"UNKNOWN\"}";
        mockMvc.perform(post("/api/user/add")
                        .contentType("application/json")
                        .content(jsonBody)
                        .header("Authorization", authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Order(2)
    public void testAddUser() throws Exception {
        String jsonBody = "{\"userName\": \"Test\", \"userSecondname\": \"User\", \"email\": \"test@example.com\", \"passwordHash\": \"123456\", \"role\": \"ADMIN\"}";
        mockMvc.perform(post("/api/user/add")
                        .contentType("application/json")
                        .content(jsonBody)
                        .header("Authorization", authToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("Test"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @Order(3)
    public void testGetUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/user/get")
                        .param("id", "0")
                        .header("Authorization", authToken))  // Assuming this ID does not exist
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/user/get-all")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(5)
    public void testAddTodo() throws Exception {
        String jsonBody = "{\"userID\": \"1\", \"content\": \"New task\", \"status\": \"ACTIVE\", \"priority\": \"IMPORTANT_URGENTLY\"}";
        mockMvc.perform(post("/api/todo/add")
                        .contentType("application/json")
                        .content(jsonBody)
                        .header("Authorization", authToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("New task"));
    }

    @Test
    @Order(6)
    public void testGetAllTodosByUserId() throws Exception {
        mockMvc.perform(get("/api/todo/get-all-by-user-id")
                        .param("id", "1")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(7)
    public void testGetAllTodos() throws Exception {
        mockMvc.perform(get("/api/todo/get-all")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(8)
    public void testAddProject() throws Exception {
        String jsonBody = "{\"userProject\": 1, \"name\": \"New Project\"}";
        mockMvc.perform(post("/api/project/add")
                        .contentType("application/json")
                        .content(jsonBody)
                        .header("Authorization", authToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Project"));
    }

    @Test
    @Order(9)
    public void testGetProject() throws Exception {
        mockMvc.perform(get("/api/project/get")
                        .param("projectId", "1")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @Order(10)
    public void testGetAllProjects() throws Exception {
        mockMvc.perform(get("/api/project/get-all")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(11)
    public void testGetAllProjectsByUserId() throws Exception {
        mockMvc.perform(get("/api/project/get-all-by-user-id")
                        .param("id", "1")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(12)
    public void testAddTable() throws Exception {
        String jsonBody = "{\"name\": \"New Table\", \"projectId\": 1, \"status\": \"ACTIVE\"}";
        mockMvc.perform(post("/api/table/add")
                        .contentType("application/json")
                        .content(jsonBody)
                        .header("Authorization", authToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Table"));
    }

    @Test
    @Order(13)
    public void testGetTableById() throws Exception {
        mockMvc.perform(get("/api/table/get")
                        .param("tableId", "1")
                        .header("Authorization", authToken))  // Assuming this ID exists or is mocked accordingly
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tableId").value(1));
    }

    @Test
    @Order(14)
    public void testGetAllTables() throws Exception {
        mockMvc.perform(get("/api/table/get-all")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(15)
    public void testGetAllTablesByProjectId() throws Exception {
        mockMvc.perform(get("/api/table/get-all-by-project-id")
                        .param("id", "1")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // Add other tests similarly, ensuring each request includes the Authorization header with the authToken

    @Test
    @Order(16)
    public void testAddTask() throws Exception {
        String jsonBody = "{\"name\": \"New Task\", \"tableId\": 1, \"status\": \"ACTIVE\"}";
        mockMvc.perform(post("/api/task/add")
                        .contentType("application/json")
                        .content(jsonBody)
                        .header("Authorization", authToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Task"));
    }

    @Test
    @Order(17)
    public void testGetTaskById() throws Exception {
        mockMvc.perform(get("/api/task/get")
                        .param("taskId", "1")
                        .header("Authorization", authToken))  // Assuming this ID exists
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(1));
    }

    @Test
    @Order(18)
    public void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/api/task/get-all")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(19)
    public void testGetAllTasksByTableId() throws Exception {
        mockMvc.perform(get("/api/task/get-all-by-table-id")
                        .param("id", "1")
                        .header("Authorization", authToken)) // Assuming table ID is 1
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(20)
    public void testAddTag() throws Exception {
        String jsonBody = "{\"name\": \"Urgent\", \"projectId\": 1,  \"taskId\": 1  }";
        mockMvc.perform(post("/api/tag/add")
                        .contentType("application/json")
                        .content(jsonBody)
                        .header("Authorization", authToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Urgent"));
    }

    @Test
    @Order(21)
    public void testGetTagById() throws Exception {
        mockMvc.perform(get("/api/tag/get")
                        .param("tagId", "1")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagId").value(1));
    }

    @Test
    @Order(22)
    public void testGetAllTags() throws Exception {
        mockMvc.perform(get("/api/tag/get-all")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(23)
    public void testDeleteTagById() throws Exception {
        mockMvc.perform(delete("/api/tag/del")
                        .param("tagId", "1")
                        .header("Authorization", authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(24)
    public void testAddGroup() throws Exception {
        String jsonBody = "{\"name\": \"Development Team\", \"projectId\": 1}";
        mockMvc.perform(post("/api/group/add")
                        .contentType("application/json")
                        .content(jsonBody)
                        .header("Authorization", authToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Development Team"));
    }

    @Test
    @Order(25)
    public void testGetGroupById() throws Exception {
        mockMvc.perform(get("/api/group/get")
                        .param("id", "1")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Development Team"));
    }

    @Test
    @Order(26)
    public void testGetAllGroups() throws Exception {
        mockMvc.perform(get("/api/group/get-all")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(27)
    public void testDeleteGroupById() throws Exception {
        mockMvc.perform(delete("/api/group/del")
                        .param("id", "1")
                        .header("Authorization", authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(28)
    public void testStartTaskTimer() throws Exception {
        mockMvc.perform(post("/api/task/start-timer")
                        .param("taskId", "1")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTimer").isNotEmpty());
    }

    @Test
    @Order(29)
    public void testStopTaskTimer() throws Exception {
        mockMvc.perform(post("/api/task/stop-timer")
                        .param("taskId", "1")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTimer").isEmpty())
                .andExpect(jsonPath("$.sumTimer").isNotEmpty());
    }

    @Test
    @Order(30)
    public void testCompleteTask() throws Exception {
        mockMvc.perform(post("/api/task/complete")
                        .param("taskId", "1")
                        .header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("NO_ACTIVE"))
                .andExpect(jsonPath("$.timeEndTask").isNotEmpty());
    }

    @Test
    @Order(38)
    public void testDeleteAllTasksByTableId() throws Exception {
        mockMvc.perform(delete("/api/task/del-by-table-id")
                        .param("id", "1")
                        .header("Authorization", authToken)) // Assuming table ID is 1
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(40)
    public void testDeleteAllTablesByProjectId() throws Exception {
        mockMvc.perform(delete("/api/table/del-by-project-id")
                        .param("id", "1")
                        .header("Authorization", authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(41)
    public void testDeleteAllProjectsByUserId() throws Exception {
        mockMvc.perform(delete("/api/project/del-by-user-id")
                        .param("userId", "1")
                        .header("Authorization", authToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(42)
    public void testDeleteTodoById() throws Exception {
        mockMvc.perform(delete("/api/todo/del")
                        .param("TodoId", "1")
                        .header("Authorization", authToken)) // Assuming this ID exists or is mocked accordingly
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(43)
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/user/del")
                        .param("id", "1")
                        .header("Authorization", authToken))
                .andExpect(status().isNoContent());
    }
}
