package com.EffortlyTimeTracker;

import com.EffortlyTimeTracker.entity.TodoNodeEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Priority;
import com.EffortlyTimeTracker.enums.Status;
import com.EffortlyTimeTracker.exception.user.UserNotFoudException;
import com.EffortlyTimeTracker.service.TodoService;
import com.EffortlyTimeTracker.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleApp implements CommandLineRunner {

    private final UserService userService;
    private final TodoService todoService;

    public ConsoleApp(UserService userService, TodoService todoService) {
        this.userService = userService;
        this.todoService = todoService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choose an entity type to manage: ");
            System.out.println("1. User");
            System.out.println("2. Todo");
            System.out.println("3. Exit");
            String entityChoice = scanner.nextLine();

            try {
                switch (entityChoice) {
                    case "1":
                        manageUsers(scanner);
                        break;
                    case "2":
                        manageTodos(scanner);
                        break;
                    case "3":
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice, try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Returning to main menu...");
            }
        }
    }

    private void manageUsers(Scanner scanner) {
        while (true) {
            System.out.println("Choose a user action: ");
            System.out.println("1. Add User");
            System.out.println("2. Delete User by ID");
            System.out.println("3. Get User by ID");
            System.out.println("4. Get All Users");
            System.out.println("5. Back to Main Menu");
            String userChoice = scanner.nextLine();

            try {
                switch (userChoice) {
                    case "1":
                        createUser(scanner);
                        break;
                    case "2":
                        deleteUser(scanner);
                        break;
                    case "3":
                        getUserById(scanner);
                        break;
                    case "4":
                        getAllUsers();
                        break;
                    case "5":
                        return;
                    default:
                        System.out.println("Invalid choice, try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Returning to user management menu...");
            }
        }
    }

    private void manageTodos(Scanner scanner) {
        while (true) {
            System.out.println("Choose a todo action: ");
            System.out.println("1. Add Todo");
            System.out.println("2. Delete Todo by ID");
            System.out.println("3. Get Todo by ID");
            System.out.println("4. Get All Todos by User ID");
            System.out.println("5. Back to Main Menu");
            String todoChoice = scanner.nextLine();

            try {
                switch (todoChoice) {
                    case "1":
                        addTodo(scanner);
                        break;
                    case "2":
                        deleteTodoById(scanner);
                        break;
                    case "3":
                        getTodoById(scanner);
                        break;
                    case "4":
                        getAllTodosByUserId(scanner);
                        break;
                    case "5":
                        return;
                    default:
                        System.out.println("Invalid choice, try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Returning to todo management menu...");
            }
        }
    }

    private void createUser(Scanner scanner) {
        try {
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter second name: ");
            String sname = scanner.nextLine();
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            System.out.print("Enter role: ");
            String role = scanner.nextLine();

            UserEntity userEntity = new UserEntity();
            userEntity.setUserName(name);
            userEntity.setUserSecondname(sname);
            userEntity.setEmail(email);
            userEntity.setPasswordHash(password);
            userEntity.setRole(userService.getRoleByName(role));

            userService.addUser(userEntity);
            System.out.println("User created successfully.");
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    private void deleteUser(Scanner scanner) {
        try {
            System.out.print("Enter user ID to delete: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            userService.delUserById(id);
            System.out.println("User deleted successfully.");
        } catch (UserNotFoudException e) {
            System.out.println("User not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private void getUserById(Scanner scanner) {
        try {
            System.out.print("Enter user ID: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            UserEntity user = userService.getUserById(id);
            System.out.println("User found: " + user);
        } catch (UserNotFoudException e) {
            System.out.println("User not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }
    }

    private void getAllUsers() {
        try {
            userService.getAllUsers().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
    }

    private void addTodo(Scanner scanner) {
        try {
            System.out.print("Enter user ID: ");
            Integer userId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter todo content: ");
            String content = scanner.nextLine();
            System.out.print("Enter todo status (NO_ACTIVE, ACTIVE): ");
            Status status = Status.valueOf(scanner.nextLine().toUpperCase());
            System.out.print("Enter todo priority (IMPORTANT_URGENTLY, NO_IMPORTANT_URGENTLY, IMPORTANT_NO_URGENTLY, NO_IMPORTANT_NO_URGENTLY): ");
            Priority priority = Priority.valueOf(scanner.nextLine().toUpperCase());

            TodoNodeEntity todoNode = new TodoNodeEntity();
            todoNode.setUser(userService.getUserById(userId));
            todoNode.setContent(content);
            todoNode.setStatus(status);
            todoNode.setPriority(priority);

            todoService.addTodo(todoNode);
            System.out.println("Todo added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding todo: " + e.getMessage());
        }
    }

    private void deleteTodoById(Scanner scanner) {
        try {
            System.out.print("Enter todo ID to delete: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            todoService.delTodoById(id);
            System.out.println("Todo deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting todo: " + e.getMessage());
        }
    }

    private void getTodoById(Scanner scanner) {
        try {
            System.out.print("Enter todo ID: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            TodoNodeEntity todo = todoService.getTodoById(id);
            System.out.println("Todo found: " + todo);
        } catch (Exception e) {
            System.out.println("Error fetching todo: " + e.getMessage());
        }
    }

    private void getAllTodosByUserId(Scanner scanner) {
        try {
            System.out.print("Enter user ID: ");
            Integer userId = Integer.parseInt(scanner.nextLine());
            todoService.getAllTodoByIdUser(userId).forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error fetching todos: " + e.getMessage());
        }
    }
}
