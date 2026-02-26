package com.dashboard.tasks.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dashboard.tasks.domain.model.Task
import com.dashboard.tasks.domain.model.TaskPriority
import com.dashboard.tasks.domain.model.TaskStatus

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(status = task.status)
            }
            if (task.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            PriorityChip(priority = task.priority)
        }
    }
}

@Composable
fun StatusBadge(status: TaskStatus) {
    val (label, containerColor) = when (status) {
        TaskStatus.TODO -> "Pendiente" to MaterialTheme.colorScheme.surfaceVariant
        TaskStatus.IN_PROGRESS -> "En progreso" to MaterialTheme.colorScheme.secondaryContainer
        TaskStatus.DONE -> "Completada" to MaterialTheme.colorScheme.tertiaryContainer
    }
    Surface(
        color = containerColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun PriorityChip(priority: TaskPriority) {
    val (label, color) = when (priority) {
        TaskPriority.LOW -> "Baja" to MaterialTheme.colorScheme.outline
        TaskPriority.MEDIUM -> "Media" to MaterialTheme.colorScheme.secondary
        TaskPriority.HIGH -> "Alta" to MaterialTheme.colorScheme.error
    }
    Surface(color = color.copy(alpha = 0.12f), shape = MaterialTheme.shapes.extraSmall) {
        Text(
            text = "● $label",
            color = color,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}
