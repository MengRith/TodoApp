package kh.com.exercise.todoapp.todo

import android.R.attr.description
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kh.com.exercise.todoapp.R
import kh.com.exercise.todoapp.ui.theme.TodoAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageTodo(
    viewModel: TodoViewModel
) {
    val todoList by viewModel.todoList.observeAsState(emptyList())
    var inputText by remember { mutableStateOf("") }
    var editingTodo by remember { mutableStateOf<Todo?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { }
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.img_mouse),
                            contentDescription = "Profile"
                        )
                    }
                },
                title = { Text(text = "Todo App") },
                actions = {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_notifications),
                            contentDescription = "Notifications"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        bottomBar = {
            BottomAppBar { }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = inputText,
                    onValueChange = {
                        inputText = it
                    },
                    label = { Text("New todo") },
                    placeholder = { Text("What needs to be done?") },
                    singleLine = true
                )
                Button(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.addTodo(inputText)
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                ) {
                    Text("Add")
                }
            }
//            LazyColumn(
//                modifier = Modifier.padding(8.dp)
//            ) {
//                itemsIndexed(todoList) { _, item ->
//                    TodoItem(
//                        todo = item,
//                        onDelete = {
//                            viewModel.deleteTodo(item.id)
//                        }
//                    )
//                }
//            }
            if (todoList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No todos yet, add one above",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)

                ) {
                    itemsIndexed(
                        items = todoList,
                        key = { _, item -> item.id }
                    ) { _, item ->
                        TodoItem(
                            todo = item,
                            onDelete = {
                                viewModel.deleteTodo(item.id)
                            },
                            onEdit = {
                                editingTodo = item
                            }
                        )
                    }
                }
            }
        }
    }
    editingTodo?.let { todo ->
        EditTodoDialog(
            todo = todo,
            onDismiss = { editingTodo = null },
            onConfirm = { newTitle, newDescription ->
                viewModel.editTodo(todo.id, newTitle, newDescription)
                editingTodo = null
            }
        )
    }
}

@Composable
fun EditTodoDialog(
    todo: Todo,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit)
{
    var title by remember { mutableStateOf(todo.title) }
    var description by remember { mutableStateOf(todo.description) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Todo") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
                {
                 OutlinedTextField(
                     value = title,
                     onValueChange = { title = it },
                     label = { Text("Title") },
                     singleLine = true
                 )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        singleLine = true
                    )
                }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()){

                    onConfirm(title, description)
                    }
                }
            ) {
                Text("Save")
                }
        }
    )

}

@Composable
fun TodoItem(
    todo: Todo,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
//            .clip(RoundedCornerShape(16.dp))
//            .background(MaterialTheme.colorScheme.background),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = SimpleDateFormat(
                        "HH:mm:a, dd/MM/yyyy",
                        Locale.ENGLISH
                    ).format(todo.date),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                Text(
                    text = todo.title,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (todo.description.isNotBlank()) {
                    Text(
                        text = todo.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
            IconButton(onClick = onEdit) {

                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun PageTodoPreview() {
    TodoAppTheme() {
        TodoItem(
            todo = Todo(
                id = 1,
                title = "Go to school",
                description = "Learn database",
                date = Date()
            ),
            onDelete = {},
            onEdit = {}
        )
    }
}