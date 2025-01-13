package com.EffortlyTimeTracker.controller.v2;

import com.EffortlyTimeTracker.DTO.project.ProjectResponseDTO;
import com.EffortlyTimeTracker.DTO.todo.TodoNodeResponseDTO;
import com.EffortlyTimeTracker.DTO.user.UserCreateDTO;
import com.EffortlyTimeTracker.DTO.user.UserResponseDTO;
import com.EffortlyTimeTracker.entity.ProjectEntity;
import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.mapper.ProjectMapper;
import com.EffortlyTimeTracker.mapper.TodoNodeMapper;
import com.EffortlyTimeTracker.mapper.UserMapper;
import com.EffortlyTimeTracker.unit.ProjectService;
import com.EffortlyTimeTracker.unit.TodoService;
import com.EffortlyTimeTracker.unit.UserService;
import com.EffortlyTimeTracker.unit.middlewareOwn.project.CheckUserIdMatchesCurrentUserProject;
import com.EffortlyTimeTracker.unit.middlewareOwn.todo.CheckOwner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Tag(name = "User-V2-controller")
@Slf4j //loginig
@RestController
@RequestMapping("api/v2/users")
public class UserControllerV2 {
    private final UserService userService;
    private final UserMapper userMapper;
    private final TodoNodeMapper todoNodeMapper;
    private final TodoService todoService;
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    @Autowired
    public UserControllerV2(UserService userService, UserMapper userMapper, TodoService todoService, TodoNodeMapper todoNodeMapper, ProjectService projectService, ProjectMapper projectMapper) {
        this.todoNodeMapper = todoNodeMapper;
        this.todoService = todoService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add user for dto obj",
            description = "need:  name , sname, email, password, role (ADMIN, MANAGER, USER)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully added",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error - Failed to add user",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        log.info("Add user: {}", userCreateDTO);

        UserEntity userEntity = userMapper.toEntity(userCreateDTO);
        log.info("UserEntity: {}", userEntity);

        UserEntity newUser = userService.addUser(userEntity);
        UserResponseDTO responsUserDto = userMapper.toDTOResponse(newUser);
        log.info("Respons: {}", responsUserDto);
        return new ResponseEntity<>(responsUserDto, HttpStatus.CREATED);
    }


    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user by id",
            description = "Deletes a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(@PathVariable("userId") Integer userId) {
        log.info("Delete user by id: {}", userId);
        userService.delUserById(userId);
    }


    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user by id",
            description = "need id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDTO getUser(@PathVariable Integer userId) {  // Используем @PathVariable для id
        log.info("Get user by id: {}", userId);

        UserResponseDTO userResponseDTO = userMapper.toDTOResponse(userService.getUserById(userId));
        log.info("UserResponseDTO: {}", userResponseDTO);
        return userResponseDTO;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get users with optional email filter",
            description = "If email is provided, the user with that email will be returned. Otherwise, all users are retrieved.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserResponseDTO> getUsers(@RequestParam(required = false) String email) {
        if (email != null && !email.isEmpty()) {
            log.info("Get user by email: {}", email);

            Optional<UserEntity> userEntity = userService.getUserByEmail(email);
            if (userEntity.isEmpty()) {
                log.warn("User with email {} not found", email);
                throw new RuntimeException("User not found");
            }

            UserResponseDTO userResponseDTO = userMapper.toDTOResponse(userEntity.get());
            log.info("UserResponseDTO: {}", userResponseDTO);
            return List.of(userResponseDTO); // Возвращаем список с одним пользователем
        } else {
            log.info("Get all users");
            List<UserResponseDTO> userResponseDTOS = userMapper.toDtoListResponse(userService.getAllUsers());
            log.info("UserResponseDTOS: {}", userResponseDTOS);
            return userResponseDTOS;
        }
    }

    // Удалить все todos для пользователя по userId
    @Operation(summary = "Delete all todos by user id", description = "need user id")
    @DeleteMapping("/{userId}/todos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Todos successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "400", description = "User not found or no todos to delete", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST')")
    @CheckOwner
    public void delAllTodoBuUserID(@PathVariable Integer userId) {
        log.info("Deleting all todos for user with id: {}", userId);
        List<TodoNodeEntity> todos = todoService.getAllTodoByIdUser(userId);

        if (todos == null || todos.isEmpty()) {
            log.warn("No todos found for user with id: {}", userId);
            throw new RuntimeException("User not found or no todos to delete");
        }

        todoService.delAllTodoByIdUser(userId);
        log.info("Successfully deleted all todos for user with id: {}", userId);
    }

    // Получить все todos по userId
    @Operation(summary = "Get all todo by user id")
    @GetMapping("/{userId}/todos")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_GUEST')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoNodeResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "400", description = "User not found or Todo list is empty", content = @Content)
    })
    @CheckOwner
    public List<TodoNodeResponseDTO> getTodoAll(@PathVariable Integer userId) {
        log.info("Get todos by user id: {}", userId);
        List<TodoNodeEntity> resTodoNodeEntity = todoService.getAllTodoByIdUser(userId);

        if (resTodoNodeEntity == null || resTodoNodeEntity.isEmpty()) {
            log.warn("No todos found for user with id: {}", userId);
            throw new RuntimeException("User not found or Todo list is empty");
        }

        return todoNodeMapper.toDtoResponse(resTodoNodeEntity);
    }

    @Operation(summary = "Get all projects by user id", description = "Retrieve all projects associated with a specific user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found or no projects available", content = @Content)
    })
    @GetMapping("/{userId}/projects")
    @ResponseStatus(HttpStatus.OK)
    @CheckUserIdMatchesCurrentUserProject
    public List<ProjectResponseDTO> getProjectsByUserId(@PathVariable("userId") Integer userId) {
        log.info("{}/projects", userId);
        log.info("Fetching all projects for userId = {}", userId);

        List<ProjectEntity> resProjectEntity = projectService.getAllProjectByIdUser(userId);
        log.info("resProjectEntity = {}", resProjectEntity);

        return projectMapper.toResponseDTO(resProjectEntity);
    }

    @Operation(summary = "Delete all projects by user id", description = "Delete all projects associated with a specific user by their ID")
    @DeleteMapping("/{userId}/projects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Projects successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have sufficient permissions", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found or no projects to delete", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckUserIdMatchesCurrentUserProject
    public void delAllProjByUserId(@PathVariable("userId") Integer userId) {
        log.info("api/users/{}/projects", userId);
        log.info("Deleting all projects for userId = {}", userId);
        projectService.delAllProjectByIdUser(userId);
    }


}

