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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.subtrack.R
import com.example.subtrack.SubscriptionFormActivity
import com.example.subtrack.data.utils.Formats
import com.example.subtrack.data.utils.categoryTitle
import com.example.subtrack.data.utils.localizedStringResource
import com.example.subtrack.domain.Category
import com.example.subtrack.domain.SortOrder
import com.example.subtrack.domain.Subscription
import com.example.subtrack.domain.SubscriptionSorter
import com.example.subtrack.ui.components.BottomNavBar
import com.example.subtrack.ui.components.PreviewWrapper
import com.example.subtrack.ui.components.Screen
import com.example.subtrack.ui.components.SectionHeader
import com.example.subtrack.ui.components.SortMenu
import com.example.subtrack.ui.components.StatCard
import com.example.subtrack.ui.components.SubscriptionCard
import com.example.subtrack.ui.components.sampleCategories
import com.example.subtrack.ui.components.sampleSubscriptions
import com.example.subtrack.ui.theme.HeaderSubtitle
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Composable
fun HomeScreen(
    subscriptions: List<Subscription>,
    categories: List<Category>,
    defaultSortOrder: SortOrder,
    onDeleteSubscription: (Subscription) -> Unit
) {
    val context = LocalContext.current
    val today = LocalDate.now()
    val hour = LocalTime.now().hour

    var sortOrder by remember(defaultSortOrder) { mutableStateOf(defaultSortOrder) }
    var subscriptionToDelete by remember { mutableStateOf<Subscription?>(null) }

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
        0L -> localizedStringResource(R.string.home_today)
        else -> localizedStringResource(R.string.home_days_short, daysUntilPayment.toInt())
    }
    val categoriesById = categories.associateBy { it.id }
    val categoryTitles = categories.associate { it.id to categoryTitle(it) }
    val sortedSubscriptions = SubscriptionSorter.sort(subscriptions, sortOrder, today, categoryTitles)
    val greeting = when (hour) {
        in 5..11 -> R.string.home_greeting_morning
        in 12..17 -> R.string.home_greeting_day
        in 18..22 -> R.string.home_greeting_evening
        else -> R.string.home_greeting_night
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

    Scaffold(
        bottomBar = { BottomNavBar(currentScreen = Screen.Home) }
    ) { innerPadding ->
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
                            greeting = localizedStringResource(greeting),
                            onAddClick = { openForm(null) }
                        )

                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatCard(
                                label = localizedStringResource(R.string.home_active_subscriptions),
                                value = activeSubscriptions.size.toString(),
                                icon = R.drawable.ic_wallet,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = localizedStringResource(R.string.home_year_expenses, today.year),
                                value = Formats.money(yearTotal),
                                icon = R.drawable.ic_chart,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = localizedStringResource(R.string.home_next_payment),
                                value = nextPaymentText,
                                icon = R.drawable.ic_calendar,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            item {
                SectionHeader(title = localizedStringResource(R.string.home_upcoming_payments)) {
                    TextButton(
                        onClick = {
                            coroutineScope.launch { listState.animateScrollToItem(allSubscriptionsIndex) }
                        }
                    ) {
                        Text(text = localizedStringResource(R.string.home_see_all))
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron_right),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            if (nearestSubscriptions.isEmpty()) {
                item { EmptyListText() }
            } else {
                items(nearestSubscriptions) { subscription ->
                    SubscriptionCard(
                        subscription = subscription,
                        category = categoriesById[subscription.categoryId],
                        today = today,
                        onClick = { openForm(subscription) },
                        onLongClick = { subscriptionToDelete = subscription },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            item {
                SectionHeader(title = localizedStringResource(R.string.home_all_subscriptions)) {
                    SortMenu(
                        selected = sortOrder,
                        onSelect = { sortOrder = it }
                    )
                }
            }

            if (sortedSubscriptions.isEmpty()) {
                item { EmptyListText() }
            } else {
                items(sortedSubscriptions) { subscription ->
                    SubscriptionCard(
                        subscription = subscription,
                        category = categoriesById[subscription.categoryId],
                        today = today,
                        onClick = { openForm(subscription) },
                        onLongClick = { subscriptionToDelete = subscription },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }

    subscriptionToDelete?.let { subscription ->
        AlertDialog(
            onDismissRequest = { subscriptionToDelete = null },
            title = { Text(text = localizedStringResource(R.string.home_delete_title)) },
            text = { Text(text = subscription.name) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteSubscription(subscription)
                        subscriptionToDelete = null
                    }
                ) {
                    Text(text = localizedStringResource(R.string.action_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { subscriptionToDelete = null }) {
                    Text(text = localizedStringResource(R.string.action_cancel))
                }
            }
        )
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
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                letterSpacing = 2.sp
            )
            Text(
                text = greeting,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = localizedStringResource(R.string.home_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = HeaderSubtitle,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        IconButton(
            onClick = onAddClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f),
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = localizedStringResource(R.string.home_add_subscription)
            )
        }
    }
}

@Composable
private fun EmptyListText() {
    Text(
        text = localizedStringResource(R.string.home_empty),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    PreviewWrapper {
        HomeScreen(
            subscriptions = sampleSubscriptions,
            categories = sampleCategories,
            defaultSortOrder = SortOrder.PRICE_DESC,
            onDeleteSubscription = {}
        )
    }
}
