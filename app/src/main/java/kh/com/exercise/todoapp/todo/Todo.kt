package kh.com.exercise.todoapp.todo

import java.util.Date

data class Todo (
    var id: Int,
    var title: String,
    var description: String,
    var date: Date,
)

