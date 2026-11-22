package com.example.subtrack.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.subtrack.R
import com.example.subtrack.data.utils.categoryTitle
import com.example.subtrack.data.utils.localizedStringResource
import com.example.subtrack.domain.Categories
import com.example.subtrack.domain.Category
import com.example.subtrack.ui.components.CategoryDialog
import com.example.subtrack.ui.components.PreviewWrapper
import com.example.subtrack.ui.components.sampleCategories

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    categories: List<Category>,
    onBack: () -> Unit,
    onSaveCategory: (Category) -> Unit,
    onDeleteCategory: (Category) -> Unit
) {
    var editedCategory by remember { mutableStateOf<Category?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var deletedCategory by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = localizedStringResource(R.string.categories_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = localizedStringResource(R.string.form_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editedCategory = null
                    showDialog = true
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = localizedStringResource(R.string.categories_add)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = localizedStringResource(R.string.categories_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.forEachIndexed { index, category ->
                    if (index > 0) {
                        HorizontalDivider()
                    }
                    CategoryItem(
                        category = category,
                        onClick = {
                            editedCategory = category
                            showDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        CategoryDialog(
            category = editedCategory,
            defaultColor = Categories.nextColor(categories.map { it.color }),
            onDismiss = { showDialog = false },
            onSave = { category ->
                onSaveCategory(category)
                showDialog = false
            },
            onDelete = { category ->
                deletedCategory = category
                showDialog = false
            }
        )
    }

    deletedCategory?.let { category ->
        val otherTitle = categoryTitle(categories.find { it.id == Categories.OTHER_ID })
        AlertDialog(
            onDismissRequest = { deletedCategory = null },
            title = { Text(text = localizedStringResource(R.string.category_delete_title)) },
            text = {
                Text(text = localizedStringResource(R.string.category_delete_text, categoryTitle(category), otherTitle))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteCategory(category)
                        deletedCategory = null
                    }
                ) {
                    Text(
                        text = localizedStringResource(R.string.action_delete),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { deletedCategory = null }) {
                    Text(text = localizedStringResource(R.string.action_cancel))
                }
            }
        )
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(category.color))
            )
        },
        headlineContent = {
            Text(
                text = categoryTitle(category),
                fontWeight = FontWeight.Bold
            )
        },
        trailingContent = {
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoriesScreenPreview() {
    PreviewWrapper {
        CategoriesScreen(
            categories = sampleCategories,
            onBack = {},
            onSaveCategory = {},
            onDeleteCategory = {}
        )
    }
}
