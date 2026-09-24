package kh.com.exercise.todoapp.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodoViewModel : ViewModel() {
    private val _todoList = MutableLiveData<List<Todo>>()
    val todoList: LiveData<List<Todo>> = _todoList

    init {

        getAllTodo()
    }
    fun getAllTodo(){
        _todoList.value = TodoRepository.getAllTodo().reversed()
    }

    fun addTodo(
        title: String
    ){
        TodoRepository.addTodo(title)
        getAllTodo()
    }

    fun deleteTodo(
        id: Int
    ){
        TodoRepository.deleteTodo(id)
        getAllTodo()
    }
}
