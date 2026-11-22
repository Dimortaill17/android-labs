package com.example.subtrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.subtrack.R
import com.example.subtrack.data.utils.categoryTitle
import com.example.subtrack.data.utils.localizedStringResource
import com.example.subtrack.domain.Categories
import com.example.subtrack.domain.Category

@Composable
fun CategoryDialog(
    category: Category?,
    defaultColor: Int,
    onDismiss: () -> Unit,
    onSave: (Category) -> Unit,
    onDelete: (Category) -> Unit
) {
    val initialTitle = categoryTitle(category)
    var name by remember { mutableStateOf(initialTitle) }
    var color by remember { mutableIntStateOf(category?.color ?: defaultColor) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = localizedStringResource(if (category == null) R.string.category_new else R.string.category_edit))
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { if (it.length <= 30) name = it },
                    label = { Text(text = localizedStringResource(R.string.category_name)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = localizedStringResource(R.string.category_color),
                    style = MaterialTheme.typography.titleSmall
                )
                Categories.colors.chunked(6).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { value ->
                            ColorItem(
                                color = Color(value),
                                selected = value == color,
                                onClick = { color = value }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank(),
                onClick = {
                    val title = name.trim()
                    val titleKey = if (category != null && title == initialTitle) category.titleKey else null
                    onSave(
                        Category(
                            id = category?.id ?: 0,
                            name = if (titleKey != null && category != null) category.name else title,
                            titleKey = titleKey,
                            color = color
                        )
                    )
                }
            ) {
                Text(text = localizedStringResource(R.string.action_save))
            }
        },
        dismissButton = {
            Row {
                if (category != null && category.id != Categories.OTHER_ID) {
                    TextButton(onClick = { onDelete(category) }) {
                        Text(
                            text = localizedStringResource(R.string.action_delete),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text(text = localizedStringResource(R.string.action_cancel))
                }
            }
        }
    )
}

@Composable
private fun ColorItem(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryDialogPreview() {
    PreviewWrapper {
        CategoryDialog(
            category = sampleCategories.first(),
            defaultColor = Categories.colors.first(),
            onDismiss = {},
            onSave = {},
            onDelete = {}
        )
    }
}
