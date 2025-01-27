package io.github.jan.kex.ui.components

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jan.kex.data.remote.Task
import io.github.jan.kex.localizedDateString
import io.github.jan.kex.ui.dialog.TaskCreateDialog
import io.github.jan.kex.ui.icons.EditIcon
import io.github.jan.kex.ui.icons.rememberDelete
import kotlinx.datetime.Instant

@Composable
fun TaskCard(task: Task, modifier: Modifier = Modifier, onUpdate: (done: Boolean, task: String, dueDate: Instant) -> Unit, onDelete: () -> Unit) {
    ElevatedCard(modifier) {
        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            val done = task.doneDate != null
            var showEditDialog by remember { mutableStateOf(false) }
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(24.dp)) {
                CircularProgressIndicator(
                    Modifier
                        .alpha(if (task.loading) 1f else 0f)
                        .matchParentSize())
                Checkbox(checked = done, modifier = Modifier
                    .alpha(if (!task.loading) 1f else 0f)
                    .matchParentSize(), onCheckedChange = { onUpdate(!done, task.task, task.dueDate) })
            }
            Spacer(Modifier.width(8.dp))
            Column {
                Text(task.task)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Text(
                        text = task.dueDate.localizedDateString,
                        fontSize = 10.sp
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            if(done) {
                IconButton(onClick = onDelete, enabled = !task.loading) {
                    Icon(rememberDelete(), null)
                }
            } else {
                IconButton(onClick = { showEditDialog = true }, enabled = !task.loading) {
                    Icon(EditIcon, null)
                }
            }
            if(showEditDialog) {
                TaskCreateDialog(task = task, onDismiss = { showEditDialog = false }, onCreate = { task, dueDate ->
                    onUpdate(done, task, dueDate)
                    showEditDialog = false
                })
            }
        }
    }
}