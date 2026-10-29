package com.example.subtrack.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.subtrack.R
import com.example.subtrack.data.utils.Formats
import com.example.subtrack.data.utils.serviceColor
import com.example.subtrack.domain.Categories
import com.example.subtrack.domain.Reminders
import com.example.subtrack.domain.Subscription
import com.example.subtrack.ui.components.DateField
import com.example.subtrack.ui.components.DropdownField
import com.example.subtrack.ui.components.FormTextField
import com.example.subtrack.ui.components.OptionCard
import com.example.subtrack.ui.theme.CardBorder
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionFormScreen(
    subscription: Subscription?,
    onBack: () -> Unit,
    onSave: (Subscription) -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(subscription?.name.orEmpty()) }
    var price by remember { mutableStateOf(subscription?.let { Formats.plain(it.price) }.orEmpty()) }
    var note by remember { mutableStateOf(subscription?.note.orEmpty()) }
    var category by remember { mutableStateOf(subscription?.category ?: Categories.list.first()) }
    var yearly by remember { mutableStateOf(subscription?.yearly ?: false) }
    var startDate by remember { mutableStateOf(subscription?.startDate ?: LocalDate.now()) }
    var endDate by remember { mutableStateOf(subscription?.endDate) }
    var remind by remember { mutableStateOf(subscription?.remind ?: true) }
    var remindDays by remember { mutableIntStateOf(subscription?.remindDays ?: 3) }

    fun save() {
        val trimmedName = name.trim()
        val priceValue = price.replace(',', '.').toDoubleOrNull()
        val end = endDate

        if (trimmedName.isEmpty()) {
            Toast.makeText(context, "Введите название сервиса", Toast.LENGTH_SHORT).show()
            return
        }
        if (priceValue == null || priceValue <= 0.0) {
            Toast.makeText(context, "Введите стоимость", Toast.LENGTH_SHORT).show()
            return
        }
        if (end != null && end.isBefore(startDate)) {
            Toast.makeText(context, "Дата окончания раньше даты первого платежа", Toast.LENGTH_SHORT).show()
            return
        }

        onSave(
            Subscription(
                id = subscription?.id ?: System.currentTimeMillis(),
                name = trimmedName,
                category = category,
                price = priceValue,
                yearly = yearly,
                startDate = startDate,
                endDate = endDate,
                remind = remind,
                remindDays = remindDays,
                note = note.trim(),
                color = subscription?.color ?: serviceColor(trimmedName)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (subscription == null) "Добавить подписку" else "Редактировать подписку") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FormTextField(
                value = name,
                onValueChange = { name = it },
                label = "Название сервиса",
                placeholder = "Например, Netflix",
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DropdownField(
                    label = "Категория",
                    value = category,
                    options = Categories.list,
                    onSelect = { category = Categories.list[it] },
                    modifier = Modifier.weight(1f)
                )

                FormTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = "Стоимость",
                    placeholder = "299",
                    suffix = { Text(text = "₽", fontWeight = FontWeight.Bold) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.width(140.dp)
                )
            }

            Text(
                text = "Периодичность оплаты",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PeriodCard(
                    label = "Ежемесячно",
                    selected = !yearly,
                    onClick = { yearly = false },
                    modifier = Modifier.weight(1f)
                )
                PeriodCard(
                    label = "Ежегодно",
                    selected = yearly,
                    onClick = { yearly = true },
                    modifier = Modifier.weight(1f)
                )
            }

            DateField(
                label = "Дата первого / ближайшего платежа",
                date = startDate,
                onDateChange = { date -> if (date != null) startDate = date },
                modifier = Modifier.fillMaxWidth()
            )

            DateField(
                label = "Повторять до (необязательно)",
                date = endDate,
                onDateChange = { endDate = it },
                clearable = true,
                modifier = Modifier.fillMaxWidth()
            )

            ReminderCard(
                remind = remind,
                remindDays = remindDays,
                onRemindChange = { remind = it },
                onRemindDaysChange = { remindDays = it }
            )

            FormTextField(
                value = note,
                onValueChange = { note = it },
                label = "Заметка (необязательно)",
                placeholder = "Например, семейная подписка",
                singleLine = false,
                minLines = 3,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { save() },
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(56.dp)
            ) {
                Text(
                    text = "Сохранить подписку",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PeriodCard(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    OptionCard(
        selected = selected,
        onClick = onClick,
        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.height(88.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_calendar),
            contentDescription = null,
            tint = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = color,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun ReminderCard(
    remind: Boolean,
    remindDays: Int,
    onRemindChange: (Boolean) -> Unit,
    onRemindDaysChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bell),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = "Напоминание",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Box(modifier = Modifier.padding(top = 4.dp)) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable(enabled = remind) { expanded = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Reminders.label(remindDays),
                            style = MaterialTheme.typography.labelLarge,
                            color = if (remind) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron_down),
                            contentDescription = null,
                            tint = if (remind) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(14.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        Reminders.labels.forEachIndexed { index, label ->
                            DropdownMenuItem(
                                text = { Text(text = label) },
                                onClick = {
                                    onRemindDaysChange(Reminders.days[index])
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Switch(
                checked = remind,
                onCheckedChange = onRemindChange
            )
        }
    }
}
