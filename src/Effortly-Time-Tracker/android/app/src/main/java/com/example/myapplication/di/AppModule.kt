package com.example.myapplication.di


import TodoViewModel
import com.example.myapplication.data.repository.AuthRepositoryImpl
import com.example.myapplication.data.repository.ProjectRepositoryImpl
import com.example.myapplication.data.repository.TableRepositoryImpl
import com.example.myapplication.data.repository.TaskRepositoryImpl
import com.example.myapplication.data.repository.TodoRepositoryImpl
import com.example.myapplication.data.repository.UserRepositoryImpl
import com.example.myapplication.datasource.remote.api.RetrofitClient
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.repository.ProjectRepository
import com.example.myapplication.domain.repository.TableRepository
import com.example.myapplication.domain.repository.TaskRepository
import com.example.myapplication.domain.repository.TodoRepository
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.domain.usecase.project.AddProjectUseCase
import com.example.myapplication.domain.usecase.todo.GetTodosUseCase
import com.example.myapplication.domain.usecase.LoginUseCase
import com.example.myapplication.domain.usecase.RegisterUseCase
import com.example.myapplication.domain.usecase.table.AddTableUseCase
import com.example.myapplication.domain.usecase.table.GetTablesByProjectIdUseCase
import com.example.myapplication.domain.usecase.todo.AddTodoUseCase
import com.example.myapplication.domain.usecase.todo.UpdateTodoStatusUseCase
import com.example.myapplication.presentation.screen.auth.AuthViewModel
import com.example.myapplication.presentation.screen.project.ProjectViewModel
import com.example.myapplication.presentation.screen.profile.ProfileViewModel
import com.example.myapplication.presentation.screen.project.ProjectDetailViewModel
import com.example.myapplication.presentation.screen.table.TableDetailScreen
import com.example.myapplication.presentation.screen.table.TableDetailViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(RetrofitClient.createAuthService(androidContext())) }
    single<TodoRepository> { TodoRepositoryImpl(RetrofitClient.createTodoService(androidContext())) }
    single<UserRepository> { UserRepositoryImpl(RetrofitClient.createUserService(androidContext())) }
    single<ProjectRepository> {
        ProjectRepositoryImpl(
            RetrofitClient.createProjectService(
                androidContext()
            )
        )
    }
    single<TableRepository> { TableRepositoryImpl(RetrofitClient.createTableService(androidContext())) }
    single<TaskRepository> { TaskRepositoryImpl(RetrofitClient.createTaskService(androidContext())) }

    // Use Cases
    single { RegisterUseCase(get()) }
    single { LoginUseCase(get()) }
    single { GetTodosUseCase(get()) }
    single { AddTodoUseCase(get()) }
    factory { UpdateTodoStatusUseCase(get()) }
    single { AddProjectUseCase(get()) }
    factory { GetTablesByProjectIdUseCase(get()) }
    factory { AddTableUseCase(get()) }


    // ViewModels
    viewModel { AuthViewModel(get(), get()) } // Login and Register UseCase
    viewModel { TodoViewModel(get(), get(), get()) } // GetTodos, AddTodo, UpdateTodoStatus UseCase
    viewModel { ProfileViewModel(get()) } // GetTodos, AddTodo, UpdateTodoStatus UseCase
    viewModel { ProjectViewModel(get()) }
    viewModel { ProjectDetailViewModel(get(), get()) }

    viewModel { TableDetailViewModel(get()) }

    // Services
    single { RetrofitClient.createUserService(androidContext()) }

}
