package com.EffortlyTimeTracker;

import com.EffortlyTimeTracker.entity.*;
import com.EffortlyTimeTracker.enums.Priority;
import com.EffortlyTimeTracker.enums.Status;
import com.EffortlyTimeTracker.service.*;
import org.hibernate.collection.spi.PersistentSet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;


@Component
public class ConsoleApp implements CommandLineRunner {

    private final UserService userService;
    private final TodoService todoService;
    private final ProjectService projectService;
    private final TableService tableService;
    private final TaskService taskService;
    private final TagService tagService;
    private final GroupService groupService;

    public ConsoleApp(UserService userService, TodoService todoService, ProjectService projectService, TableService tableService, TaskService taskService, TagService tagService, GroupService groupService) {
        this.userService = userService;
        this.todoService = todoService;
        this.projectService = projectService;
        this.tableService = tableService;
        this.taskService = taskService;
        this.tagService = tagService;
        this.groupService = groupService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choose an entity type to manage: ");
            System.out.println("1. User");
            System.out.println("2. Todo");
            System.out.println("3. Project");
            System.out.println("4. Table");
            System.out.println("5. Task");
            System.out.println("6. Tag");
            System.out.println("7. Group");
            System.out.println("8. Exit");
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
                        manageProjects(scanner);
                        break;
                    case "4":
                        manageTables(scanner);
                        break;
                    case "5":
                        manageTasks(scanner);
                        break;
                    case "6":
                        manageTags(scanner);
                        break;
                    case "7":
                        manageGroups(scanner);
                        break;
                    case "8":
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

    private void manageGroups(Scanner scanner) {
        while (true) {
            System.out.println("Choose a group action: ");
            System.out.println("1. Add Group");
            System.out.println("2. Delete Group by ID");
            System.out.println("3. Get Group by ID");
            System.out.println("4. Get All Groups");
            System.out.println("5. Add User to Group");
            System.out.println("6. Remove User from Group");
            System.out.println("7. Back to Main Menu");
            String groupChoice = scanner.nextLine();

            try {
                switch (groupChoice) {
                    case "1":
                        addGroup(scanner);
                        break;
                    case "2":
                        deleteGroupById(scanner);
                        break;
                    case "3":
                        getGroupById(scanner);
                        break;
                    case "4":
                        getAllGroups(scanner);
                        break;
                    case "5":
                        addUserToGroup(scanner);
                        break;
                    case "6":
                        removeUserFromGroup(scanner);
                        break;
                    case "7":
                        return;
                    default:
                        System.out.println("Invalid choice, try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Returning to group management menu...");
            }
        }
    }

    private void addGroup(Scanner scanner) {
        try {
            System.out.print("Enter project ID: ");
            Integer projectId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter group name: ");
            String name = scanner.nextLine();

            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setProject(projectService.getProjectsById(projectId));
            groupEntity.setName(name);

            groupService.addGroup(groupEntity);
            System.out.println("Group added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding group: " + e.getMessage());
        }
    }

    private void deleteGroupById(Scanner scanner) {
        try {
            System.out.print("Enter group ID to delete: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            groupService.delGroupById(id);
            System.out.println("Group deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting group: " + e.getMessage());
        }
    }

    private void getGroupById(Scanner scanner) {
        try {
            System.out.print("Enter group ID: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            GroupEntity group = groupService.getGroupById(id);
            System.out.println("Group found: " + group);
        } catch (Exception e) {
            System.out.println("Error fetching group: " + e.getMessage());
        }
    }

    private void getAllGroups(Scanner scanner) {
        try {
            groupService.getAllGroup().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error fetching groups: " + e.getMessage());
        }
    }

    private void addUserToGroup(Scanner scanner) {
        try {
            System.out.print("Enter group ID: ");
            Integer groupId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter user ID: ");
            Integer userId = Integer.parseInt(scanner.nextLine());

            groupService.addUserToGroup(groupId, userId);
            System.out.println("User added to group successfully.");
        } catch (Exception e) {
            System.out.println("Error adding user to group: " + e.getMessage());
        }
    }

    private void removeUserFromGroup(Scanner scanner) {
        try {
            System.out.print("Enter group ID: ");
            Integer groupId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter user ID: ");
            Integer userId = Integer.parseInt(scanner.nextLine());

            groupService.removeUserFromGroup(groupId, userId);
            System.out.println("User removed from group successfully.");
        } catch (Exception e) {
            System.out.println("Error removing user from group: " + e.getMessage());
        }
    }

    private void manageTags(Scanner scanner) {
        while (true) {
            System.out.println("Choose a tag action: ");
            System.out.println("1. Add Tag");
            System.out.println("2. Delete Tag by ID");
            System.out.println("3. Get Tag by ID");
            System.out.println("4. Get All Tags");
            System.out.println("5. Back to Main Menu");
            String tagChoice = scanner.nextLine();

            try {
                switch (tagChoice) {
                    case "1":
                        addTag(scanner);
                        break;
                    case "2":
                        deleteTagById(scanner);
                        break;
                    case "3":
                        getTagById(scanner);
                        break;
                    case "4":
                        getAllTags(scanner);
                        break;
                    case "5":
                        return;
                    default:
                        System.out.println("Invalid choice, try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Returning to tag management menu...");
            }
        }
    }

    private void addTag(Scanner scanner) {
        try {
            System.out.print("Enter project ID: ");
            Integer projectId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter tag name: ");
            String name = scanner.nextLine();

            TagEntity tagEntity = new TagEntity();
            tagEntity.setProject(projectService.getProjectsById(projectId));
            tagEntity.setName(name);

            tagService.addTag(tagEntity, null);
            System.out.println("Tag added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding tag: " + e.getMessage());
        }
    }

    private void deleteTagById(Scanner scanner) {
        try {
            System.out.print("Enter tag ID to delete: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            tagService.delTagById(id);
            System.out.println("Tag deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting tag: " + e.getMessage());
        }
    }

    private void getTagById(Scanner scanner) {
        try {
            System.out.print("Enter tag ID: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            TagEntity tag = tagService.getTagkById(id);
            System.out.println("Tag found: " + tag);
        } catch (Exception e) {
            System.out.println("Error fetching tag: " + e.getMessage());
        }
    }

    private void getAllTags(Scanner scanner) {
        try {
            tagService.getAllTag().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error fetching tags: " + e.getMessage());
        }
    }


    private void manageTasks(Scanner scanner) {
        while (true) {
            System.out.println("Choose a task action: ");
            System.out.println("1. Add Task");
            System.out.println("2. Delete Task by ID");
            System.out.println("3. Get Task by ID");
            System.out.println("4. Get All Tasks by Table ID");
            System.out.println("5. Delete All Tasks by Table ID");
            System.out.println("6. Start Task Timer");
            System.out.println("7. Stop Task Timer");
            System.out.println("8. Complete Task");
            System.out.println("9. Back to Main Menu");
            String taskChoice = scanner.nextLine();

            try {
                switch (taskChoice) {
                    case "1":
                        addTask(scanner);
                        break;
                    case "2":
                        deleteTaskById(scanner);
                        break;
                    case "3":
                        getTaskById(scanner);
                        break;
                    case "4":
                        getAllTasksByTableId(scanner);
                        break;
                    case "5":
                        deleteAllTasksByTableId(scanner);
                        break;
                    case "6":
                        startTaskTimer(scanner);
                        break;
                    case "7":
                        stopTaskTimer(scanner);
                        break;
                    case "8":
                        completeTask(scanner);
                        break;
                    case "9":
                        return;
                    default:
                        System.out.println("Invalid choice, try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Returning to task management menu...");
            }
        }
    }

    private void addTask(Scanner scanner) {
        try {
            System.out.print("Enter table ID: ");
            Integer tableId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter task name: ");
            String name = scanner.nextLine();
            System.out.print("Enter task status (ACTIVE, NO_ACTIVE): ");
            Status status = Status.valueOf(scanner.nextLine().toUpperCase());

            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setTable(tableService.getTableById(tableId));
            taskEntity.setName(name);
            taskEntity.setStatus(status);

            taskService.addTask(taskEntity);
            System.out.println("Task added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding task: " + e.getMessage());
        }
    }

    private void deleteTaskById(Scanner scanner) {
        try {
            System.out.print("Enter task ID to delete: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            taskService.delTaskById(id);
            System.out.println("Task deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting task: " + e.getMessage());
        }
    }

    private void getTaskById(Scanner scanner) {
        try {
            System.out.print("Enter task ID: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            TaskEntity task = taskService.getTaskById(id);
            System.out.println("Task found: " + task);
        } catch (Exception e) {
            System.out.println("Error fetching task: " + e.getMessage());
        }
    }

    private void getAllTasksByTableId(Scanner scanner) {
        try {
            System.out.print("Enter table ID: ");
            Integer tableId = Integer.parseInt(scanner.nextLine());
            taskService.getAllTaskByIdTable(tableId).forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error fetching tasks: " + e.getMessage());
        }
    }

    private void deleteAllTasksByTableId(Scanner scanner) {
        try {
            System.out.print("Enter table ID: ");
            Integer tableId = Integer.parseInt(scanner.nextLine());
            taskService.delAllTaskByIdTable(tableId);
            System.out.println("All tasks for the table deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting tasks: " + e.getMessage());
        }
    }

    private void startTaskTimer(Scanner scanner) {
        try {
            System.out.print("Enter task ID: ");
            Integer taskId = Integer.parseInt(scanner.nextLine());
            TaskEntity task = taskService.startTaskTimer(taskId);
            System.out.println("Timer started for task: " + task);
        } catch (Exception e) {
            System.out.println("Error starting task timer: " + e.getMessage());
        }
    }

    private void stopTaskTimer(Scanner scanner) {
        try {
            System.out.print("Enter task ID: ");
            Integer taskId = Integer.parseInt(scanner.nextLine());
            TaskEntity task = taskService.stopTaskTimer(taskId);
            System.out.println("Timer stopped for task: " + task);
        } catch (Exception e) {
            System.out.println("Error stopping task timer: " + e.getMessage());
        }
    }

    private void completeTask(Scanner scanner) {
        try {
            System.out.print("Enter task ID: ");
            Integer taskId = Integer.parseInt(scanner.nextLine());
            TaskEntity task = taskService.completeTask(taskId);
            System.out.println("Task completed: " + task);
        } catch (Exception e) {
            System.out.println("Error completing task: " + e.getMessage());
        }
    }

    private void manageTables(Scanner scanner) {
        while (true) {
            System.out.println("Choose a table action: ");
            System.out.println("1. Add Table");
            System.out.println("2. Delete Table by ID");
            System.out.println("3. Get Table by ID");
            System.out.println("4. Get All Tables by Project ID");
            System.out.println("5. Delete All Tables by Project ID");
            System.out.println("6. Back to Main Menu");
            String tableChoice = scanner.nextLine();

            try {
                switch (tableChoice) {
                    case "1":
                        addTable(scanner);
                        break;
                    case "2":
                        deleteTableById(scanner);
                        break;
                    case "3":
                        getTableById(scanner);
                        break;
                    case "4":
                        getAllTablesByProjectId(scanner);
                        break;
                    case "5":
                        deleteAllTablesByProjectId(scanner);
                        break;
                    case "6":
                        return;
                    default:
                        System.out.println("Invalid choice, try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Returning to table management menu...");
            }
        }
    }

    private void addTable(Scanner scanner) {
        try {
            System.out.print("Enter project ID: ");
            Integer projectId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter table name: ");
            String name = scanner.nextLine();
            System.out.print("Enter table status (ACTIVE, NO_ACTIVE): ");
            Status status = Status.valueOf(scanner.nextLine().toUpperCase());

            TableEntity tableEntity = new TableEntity();
            tableEntity.setProject(projectService.getProjectsById(projectId));
            tableEntity.setName(name);
            tableEntity.setStatus(status);

            tableService.addTable(tableEntity);
            System.out.println("Table added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding table: " + e.getMessage());
        }
    }

    private void deleteTableById(Scanner scanner) {
        try {
            System.out.print("Enter table ID to delete: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            tableService.delTableById(id);
            System.out.println("Table deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting table: " + e.getMessage());
        }
    }

    private void getTableById(Scanner scanner) {
        try {
            System.out.print("Enter table ID: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            TableEntity table = tableService.getTableById(id);
            System.out.println("Table found: " + table);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching table: " + e.getMessage());
        }
    }

    private void getAllTablesByProjectId(Scanner scanner) {
        try {
            System.out.print("Enter project ID: ");
            Integer projectId = Integer.parseInt(scanner.nextLine());
            tableService.getAllTableByIdProject(projectId).forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error fetching tables: " + e.getMessage());
        }
    }

    private void deleteAllTablesByProjectId(Scanner scanner) {
        try {
            System.out.print("Enter project ID: ");
            Integer projectId = Integer.parseInt(scanner.nextLine());
            tableService.delAllTablleByIdProject(projectId);
            System.out.println("All tables for the project deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting tables: " + e.getMessage());
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

    private void manageProjects(Scanner scanner) {
        while (true) {
            System.out.println("Choose a project action: ");
            System.out.println("1. Add Project");
            System.out.println("2. Delete Project by ID");
            System.out.println("3. Get Project by ID");
            System.out.println("4. Get All Projects by User ID");
            System.out.println("5. Get Project Analytics");
            System.out.println("6. Back to Main Menu");
            String projectChoice = scanner.nextLine();

            try {
                switch (projectChoice) {
                    case "1":
                        addProject(scanner);
                        break;
                    case "2":
                        deleteProjectById(scanner);
                        break;
                    case "3":
                        getProjectById(scanner);
                        break;
                    case "4":
                        getAllProjectsByUserId(scanner);
                        break;
                    case "5":
                        getProjectAnalytics(scanner);
                        break;
                    case "6":
                        return;
                    default:
                        System.out.println("Invalid choice, try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Returning to project management menu...");
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

    private void addProject(Scanner scanner) {
        try {
            System.out.print("Enter user ID: ");
            Integer userId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter project name: ");
            String name = scanner.nextLine();

            ProjectEntity projectEntity = new ProjectEntity();
            projectEntity.setUserProject(userService.getUserById(userId));
            projectEntity.setName(name);

            projectService.addProject(projectEntity);
            System.out.println("Project added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding project: " + e.getMessage());
        }
    }

    private void deleteProjectById(Scanner scanner) {
        try {
            System.out.print("Enter project ID to delete: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            projectService.delProjectById(id);
            System.out.println("Project deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting project: " + e.getMessage());
        }
    }

    private void getProjectById(Scanner scanner) {
        try {
            System.out.print("Enter project ID: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            ProjectEntity project = projectService.getProjectsById(id);
            System.out.println("Project found: " + project);
        } catch (Exception e) {
            System.out.println("Error fetching project: " + e.getMessage());
        }
    }

    private void getAllProjectsByUserId(Scanner scanner) {
        try {
            System.out.print("Enter user ID: ");
            Integer userId = Integer.parseInt(scanner.nextLine());
            projectService.getAllProjectByIdUser(userId).forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error fetching projects: " + e.getMessage());
        }
    }

    private void getProjectAnalytics(Scanner scanner) {
        try {
            System.out.print("Enter project ID: ");
            Integer projectId = Integer.parseInt(scanner.nextLine());
            var analytics = projectService.getProjectAnalytics(projectId);
            System.out.println("Project Analytics: " + analytics);
        } catch (Exception e) {
            System.out.println("Error fetching project analytics: " + e.getMessage());
        }
    }
}