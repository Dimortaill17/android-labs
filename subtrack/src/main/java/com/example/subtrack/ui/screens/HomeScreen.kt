package com.example.subtrack.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.subtrack.R
import com.example.subtrack.SubscriptionFormActivity
import com.example.subtrack.data.repository.SubscriptionRepository
import com.example.subtrack.data.utils.Formats
import com.example.subtrack.domain.Subscription
import com.example.subtrack.ui.components.SectionHeader
import com.example.subtrack.ui.components.StatCard
import com.example.subtrack.ui.components.SubscriptionCard
import com.example.subtrack.ui.theme.HeaderSubtitle
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Composable
fun HomeScreen(
    repository: SubscriptionRepository,
    refreshKey: Int
) {
    val context = LocalContext.current
    var reloadKey by remember { mutableIntStateOf(0) }
    val subscriptions = remember(reloadKey, refreshKey) { repository.getSubscriptions() }
    val today = remember(reloadKey, refreshKey) { LocalDate.now() }
    val hour = remember(reloadKey, refreshKey) { LocalTime.now().hour }

    var sortDescending by remember { mutableStateOf(true) }

    val activeSubscriptions = subscriptions.filter { it.nextPayment(today) != null }
    val yearStart = LocalDate.of(today.year, 1, 1)
    val yearEnd = LocalDate.of(today.year, 12, 31)
    val yearTotal = subscriptions.sumOf { it.price * it.paymentsBetween(yearStart, yearEnd) }
    val nearestSubscriptions = activeSubscriptions.sortedBy { it.nextPayment(today) }.take(3)
    val daysUntilPayment = nearestSubscriptions.firstOrNull()?.nextPayment(today)?.let {
        ChronoUnit.DAYS.between(today, it)
    }
    val nextPaymentText = when (daysUntilPayment) {
        null -> "—"
        0L -> "Сегодня"
        else -> "$daysUntilPayment дн."
    }
    val sortedSubscriptions = if (sortDescending) {
        subscriptions.sortedByDescending { it.price }
    } else {
        subscriptions.sortedBy { it.price }
    }
    val greeting = when (hour) {
        in 5..11 -> "Доброе утро!"
        in 12..17 -> "Добрый день!"
        in 18..22 -> "Добрый вечер!"
        else -> "Доброй ночи!"
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val allSubscriptionsIndex = 2 + maxOf(nearestSubscriptions.size, 1)

    fun openForm(subscription: Subscription?) {
        val intent = Intent(context, SubscriptionFormActivity::class.java)
        if (subscription != null) {
            intent.putExtra(SubscriptionFormActivity.EXTRA_SUBSCRIPTION_ID, subscription.id)
        }
        context.startActivity(intent)
    }

    Scaffold { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Box {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(bottom = 76.dp)
                            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )

                    Column {
                        HomeHeader(
                            greeting = greeting,
                            onAddClick = { openForm(null) }
                        )

                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatCard(
                                label = "Активные подписки",
                                value = activeSubscriptions.size.toString(),
                                icon = R.drawable.ic_wallet,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "Годовые расходы · ${today.year}",
                                value = Formats.money(yearTotal),
                                icon = R.drawable.ic_chart,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "Ближайшее списание",
                                value = nextPaymentText,
                                icon = R.drawable.ic_calendar,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            item {
                SectionHeader(
                    title = "Ближайшие списания",
                    action = "См. все",
                    onActionClick = {
                        coroutineScope.launch { listState.animateScrollToItem(allSubscriptionsIndex) }
                    }
                )
            }

            if (nearestSubscriptions.isEmpty()) {
                item { EmptyListText() }
            } else {
                items(nearestSubscriptions) { subscription ->
                    SubscriptionCard(
                        subscription = subscription,
                        today = today,
                        onClick = { openForm(subscription) },
                        onLongClick = {},
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            item {
                SectionHeader(
                    title = "Все подписки",
                    action = if (sortDescending) "Сначала дороже" else "Сначала дешевле",
                    onActionClick = { sortDescending = !sortDescending }
                )
            }

            if (sortedSubscriptions.isEmpty()) {
                item { EmptyListText() }
            } else {
                items(sortedSubscriptions) { subscription ->
                    SubscriptionCard(
                        subscription = subscription,
                        today = today,
                        onClick = { openForm(subscription) },
                        onLongClick = {},
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(
    greeting: String,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 20.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "SUBTRACK",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 2.sp
            )
            Text(
                text = greeting,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "Ваши подписки всегда под контролем",
                style = MaterialTheme.typography.bodyMedium,
                color = HeaderSubtitle,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        IconButton(
            onClick = onAddClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White.copy(alpha = 0.15f),
                contentColor = Color.White
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = "Добавить подписку"
            )
        }
    }
}

@Composable
private fun EmptyListText() {
    Text(
        text = "Подписок пока нет. Нажмите «+», чтобы добавить.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
    )
}
