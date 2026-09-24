package kh.com.exercise.todoapp.todo

import java.util.Date

fun getLabelTodo(){
    val todoList = mutableListOf<Todo>(
        Todo(1, "First todo", "Description 1", Date()),
        Todo(2, "Second todo", "Description 2", Date()),
        Todo(3, "Third todo", "Description 3", Date()),
    )
}