//package com.EffortlyTimeTracker.Integration;
//
//
//import org.junit.jupiter.api.*;
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
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class ProjectControllerIntegrationTest extends AbstractContainerBaseTest {
//    @Test
//    @Order(1)
//    public void testAddProject() throws Exception {
//        testAddUser();
//        String jsonBody = "{\"userProject\": 1, \"name\": \"New Project\"}";
//        mockMvc.perform(post("/api/project/add")
//                        .contentType("application/json")
//                        .content(jsonBody))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value("New Project"));
//    }
//
//
//    @Test
//    @Order(2)
//    public void testGetProject() throws Exception {
//        mockMvc.perform(get("/api/project/get")
//                        .param("projectId", "1"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @Order(3)
//    public void testGetAllProjects() throws Exception {
//        mockMvc.perform(get("/api/project/get-all"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray());
//    }
//
//    @Test
//    @Order(4)
//    public void testGetAllProjectsByUserId() throws Exception {
//        mockMvc.perform(get("/api/project/get-all-by-user-id")
//                        .param("id", "1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray());
//    }
//
//    @Test
//    @Order(5)
//    public void testDeleteAllProjectsByUserId() throws Exception {
//        mockMvc.perform(delete("/api/project/del-by-user-id")
//                        .param("userId", "1"))
//                .andExpect(status().isNoContent());
//    }
//
//
//
////    @Test
////    @Order(6)
////    public void testDeleteProject() throws Exception {
////        testAddProject();
////        mockMvc.perform(delete("/api/project/del")
////                        .param("projectId", "1"))
////                .andExpect(status().isNoContent());
////    }
//}