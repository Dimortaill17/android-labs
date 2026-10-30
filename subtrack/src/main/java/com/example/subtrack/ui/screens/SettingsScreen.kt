package com.example.subtrack.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.subtrack.R
import com.example.subtrack.data.repository.SettingsRepository
import com.example.subtrack.ui.components.OptionCard
import com.example.subtrack.ui.theme.CardBorder

private data class ThemeItem(
    val value: String,
    val label: String,
    @DrawableRes val icon: Int
)

private data class LanguageItem(
    val value: String,
    val code: String,
    val label: String
)

private val themeItems = listOf(
    ThemeItem(SettingsRepository.THEME_LIGHT, "Светлая", R.drawable.ic_sun),
    ThemeItem(SettingsRepository.THEME_DARK, "Тёмная", R.drawable.ic_moon),
    ThemeItem(SettingsRepository.THEME_SYSTEM, "Системная", R.drawable.ic_monitor)
)

private val languageItems = listOf(
    LanguageItem(SettingsRepository.LANGUAGE_RU, "RU", "Русский"),
    LanguageItem(SettingsRepository.LANGUAGE_EN, "EN", "English")
)

@Composable
fun SettingsScreen(repository: SettingsRepository) {
    var theme by remember { mutableStateOf(repository.getTheme()) }
    var language by remember { mutableStateOf(repository.getLanguage()) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Настройки",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            SectionTitle(text = "Внешний вид")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                themeItems.forEach { item ->
                    val selected = item.value == theme
                    val color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

                    OptionCard(
                        selected = selected,
                        onClick = {
                            theme = item.value
                            repository.setTheme(item.value)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                    ) {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = null,
                            tint = color
                        )
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.bodySmall,
                            color = color,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
            }

            SectionTitle(text = "Язык приложения")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                languageItems.forEach { item ->
                    val selected = item.value == language

                    OptionCard(
                        selected = selected,
                        onClick = {
                            language = item.value
                            repository.setLanguage(item.value)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(62.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = item.code,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                    }
                }
            }

            SectionTitle(text = "О приложении")
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                SettingsItem(
                    icon = R.drawable.ic_info,
                    text = "О приложении",
                    color = MaterialTheme.colorScheme.onSurface,
                    onClick = { showAboutDialog = true }
                )
                HorizontalDivider()
                SettingsItem(
                    icon = R.drawable.ic_trash,
                    text = "Удалить все данные",
                    color = MaterialTheme.colorScheme.error,
                    onClick = {}
                )
            }
        }
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text(text = "О приложении") },
            text = { Text(text = "SubTrack. Учёт и контроль платных подписок") },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text(text = "OK")
                }
            }
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun SettingsItem(
    @DrawableRes icon: Int,
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        leadingContent = {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        headlineContent = {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                color = color
            )
        },
        trailingContent = {
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }
    )
}
