//package com.EffortlyTimeTracker.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Testcontainers
//public class AbstractContainerBaseTest {
//    @Container
//    static final PostgreSQLContainer<?> postgres;
//
////    static {
////        postgres = new PostgreSQLContainer<>("postgres:latest")
////                .withDatabaseName("testdb")
////                .withUsername("test")
////                .withPassword("test")
////                .withInitScript("sql/CreateTables.sql");
////        postgres.start();
////    }
//////
//    @Container
//    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("testdb")
//            .withUsername("test")
//            .withPassword("test")
//            .withInitScript("sql/CreateTables.sql");
//
//
//    @DynamicPropertySource
//    static void properties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.password", postgres::getPassword);
//        registry.add("spring.datasource.username", postgres::getUsername);
//    }
//
//    @Autowired
//    public MockMvc mockMvc;
//
//
//    public void testAddUser() throws Exception {
//        String jsonBody = "{\"userName\": \"Test\", \"userSecondname\": \"User\", \"email\": \"test@example.com\", \"passwordHash\": \"123456\", \"role\": \"ADMIN\"}";
//        mockMvc.perform(post("/api/user/add")
//                        .contentType("application/json")
//                        .content(jsonBody))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.userName").value("Test"))
//                .andExpect(jsonPath("$.email").value("test@example.com"));
//    }
//
//    public void testAddProject() throws Exception {
//        testAddUser();
//        String jsonBody = "{\"userProject\": 1, \"name\": \"New Project\"}";
//        mockMvc.perform(post("/api/project/add")
//                        .contentType("application/json")
//                        .content(jsonBody))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value("New Project"));
//    }
//}
