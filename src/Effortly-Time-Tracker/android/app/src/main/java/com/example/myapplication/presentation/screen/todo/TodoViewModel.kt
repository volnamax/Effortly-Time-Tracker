import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.datasource.remote.model.StatusEnum
import com.example.myapplication.datasource.remote.model.TodoNodeDTO
import com.example.myapplication.domain.usecase.todo.AddTodoUseCase
import com.example.myapplication.domain.usecase.todo.GetTodosUseCase
import com.example.myapplication.domain.usecase.todo.UpdateTodoStatusUseCase
import com.example.myapplication.domain.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoViewModel(
    private val getTodosUseCase: GetTodosUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val updateTodoStatusUseCase: UpdateTodoStatusUseCase
) : ViewModel() {

    private val _todoState = MutableStateFlow<List<Todo>>(emptyList())
    val todoState: StateFlow<List<Todo>> = _todoState

    // Function to load all todos for a user
    fun loadTodos(userId: Int) {
        viewModelScope.launch {
            try {
                val todos = getTodosUseCase(userId)
                _todoState.value = todos // Directly assign domain model `Todo` to the state
            } catch (e: Exception) {
                Log.e("APP", "Error loading todos: ${e.message}")
            }
        }
    }

    // Function to add a new todo
    fun addTodo(content: String, priority: String, userId: Int) {
        viewModelScope.launch {
            try {
                val newTodoDTO = TodoNodeDTO(
                    content = content,
                    priority = priority,
                    userID = userId
                )
                addTodoUseCase(newTodoDTO)
                loadTodos(userId) // Reload todos after adding
            } catch (e: Exception) {
                Log.e("APP", "Error adding todo: ${e.message}")
            }
        }
    }

    // Function to update the status of a todo
    fun updateTodoStatus(todoId: Long, newStatus: StatusEnum, userId: Int) {
        viewModelScope.launch {
            try {

                updateTodoStatusUseCase(todoId, newStatus)
                loadTodos(userId) // Reload todos after updating the status
            } catch (e: Exception) {
                Log.e("APP", "Error updating todo status: ${e.message}")
            }
        }
    }
}
