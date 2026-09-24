package kh.com.exercise.todoapp.todo

import java.util.Date


object TodoRepository {
    val todoList = mutableListOf<Todo>(
        Todo(1, "Go to school", "Learn database", Date()),
        Todo(2, "After school go cafe", "Drink coffee", Date()),
        Todo(3, "Play game", "Play with friends", Date()),
        Todo(4, "Read official document", "Read document", Date()),
    )

    fun getAllTodo(): List<Todo> {
        return todoList
    }

    fun addTodo(
        title: String,
        description: String = ""
    ) {
        todoList.add(
            Todo(
                id = System.currentTimeMillis().toInt(),
                title = title,
                description = description,
                date = Date()
            )
        )
    }

    fun editTodo(
        id: Int,
        title: String,
        description: String = ""
    ) {
        val index = todoList.indexOfFirst { it.id == id }
        if (index == -1) {
            todoList[index] = todoList[index].copy(
                title = title,
                description = description,
            )
        }

    }


    fun deleteTodo(
        id: Int
    ) {
        todoList.removeIf {
            it.id == id
        }
    }


}

