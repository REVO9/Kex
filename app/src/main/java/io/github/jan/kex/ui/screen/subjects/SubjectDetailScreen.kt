package io.github.jan.kex.ui.screen.subjects

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import io.github.jan.kex.R
import io.github.jan.kex.data.remote.Task
import io.github.jan.kex.ui.components.TaskCard
import io.github.jan.kex.ui.dialog.DeleteDialog
import io.github.jan.kex.ui.dialog.TaskCreateDialog
import io.github.jan.kex.ui.icons.EditIcon
import io.github.jan.kex.ui.icons.rememberDelete
import io.github.jan.kex.vm.SubjectViewModel
import io.github.jan.kex.vm.TaskViewModel
import kotlinx.datetime.Clock

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubjectDetailScreen(
    subjectId: Long,
    taskViewModel: TaskViewModel,
    subjectViewModel: SubjectViewModel,
    navController: NavController
) {
    val subjects by subjectViewModel.subjects.collectAsStateWithLifecycle(emptyList())
    val subject = remember(subjects) { subjects.firstOrNull { it.id == subjectId } }
    val tasks by taskViewModel.tasks.collectAsStateWithLifecycle(emptyList())
    val subjectTasks = remember(tasks, subject) {
        tasks.filter { it.subjectId == subjectId }.sortedBy { it.dueDate }
    }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showCreateTaskScreen by remember { mutableStateOf(false) }
    val creatingTask by taskViewModel.creatingTask.collectAsStateWithLifecycle()
    subject?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(it.name, fontSize = 40.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.tasks), fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(subjectTasks, { it.id }) {
                    TaskCard(
                        task = it,
                        modifier = Modifier.padding(8.dp).animateItemPlacement(),
                        onUpdate = { done, task, dueDate ->
                            taskViewModel.updateTask(
                                it,
                                task,
                                dueDate,
                                if (done) Clock.System.now() else null
                            )
                        }) {
                        taskViewModel.deleteTask(it.id)
                    }
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                FloatingActionButton(
                    onClick = { showDeleteDialog = true },
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ) {
                    Icon(rememberDelete(), contentDescription = null)
                }
                Spacer(modifier = Modifier.weight(1f))
                ExtendedFloatingActionButton(
                    onClick = { showCreateTaskScreen = true },
                    text = { Text(stringResource(R.string.create)) },
                    icon = {
                        Icon(EditIcon, contentDescription = null)
                    }
                )
            }
        }
    }
    if (showCreateTaskScreen) {
        TaskCreateDialog(
            task = null,
            onDismiss = { showCreateTaskScreen = false },
            onCreate = { task, dueDate -> taskViewModel.createTask(subjectId, task, dueDate) })
    }
    if (showDeleteDialog) {
        DeleteDialog(textId = R.string.delete_subject, onDelete = {
            subjectViewModel.deleteSubject(subjectId)
            showDeleteDialog = false;
            navController.navigate("subjects")
        }) {
            showDeleteDialog = false
        }
    }
}