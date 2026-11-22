package com.example.subtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.subtrack.R
import com.example.subtrack.data.utils.Formats
import com.example.subtrack.data.utils.LocalAppLocale
import com.example.subtrack.data.utils.categoryTitle
import com.example.subtrack.data.utils.localizedStringResource
import com.example.subtrack.data.utils.moneyText
import com.example.subtrack.domain.Category
import com.example.subtrack.domain.PercentCalculator
import com.example.subtrack.domain.Subscription
import com.example.subtrack.ui.components.BottomNavBar
import com.example.subtrack.ui.components.DonutChart
import com.example.subtrack.ui.components.ExpenseRow
import com.example.subtrack.ui.components.PreviewWrapper
import com.example.subtrack.ui.components.Screen
import com.example.subtrack.ui.components.SummaryCard
import com.example.subtrack.ui.components.sampleCategories
import com.example.subtrack.ui.components.sampleSubscriptions
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun StatisticsScreen(
    subscriptions: List<Subscription>,
    categories: List<Category>
) {
    var month by remember { mutableStateOf(YearMonth.now()) }

    val payments = subscriptions
        .mapNotNull { subscription -> subscription.paymentIn(month)?.let { subscription to it } }
        .sortedBy { it.second }
    val monthTotal = payments.sumOf { it.first.price }
    val yearStart = LocalDate.of(month.year, 1, 1)
    val yearEnd = LocalDate.of(month.year, 12, 31)
    val yearTotal = subscriptions.sumOf { it.price * it.paymentsBetween(yearStart, yearEnd) }

    val categoriesById = categories.associateBy { it.id }
    val categoryTotals = payments
        .groupBy { it.first.categoryId }
        .map { (categoryId, list) -> categoriesById[categoryId] to list.sumOf { it.first.price } }
        .sortedByDescending { it.second }
    val categoryColors = categoryTotals.map { (category, _) ->
        category?.let { Color(it.color) } ?: MaterialTheme.colorScheme.outline
    }
    val percents = PercentCalculator.calculate(categoryTotals.map { it.second })
    val shares = if (monthTotal > 0) {
        categoryTotals.map { (it.second / monthTotal).toFloat() }
    } else {
        emptyList()
    }

    Scaffold(
        bottomBar = { BottomNavBar(currentScreen = Screen.Statistics) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = localizedStringResource(R.string.statistics_title),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { month = month.minusMonths(1) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_left),
                        contentDescription = localizedStringResource(R.string.statistics_previous_month),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = Formats.monthTitle(month, LocalAppLocale.current),
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(112.dp)
                )
                IconButton(onClick = { month = month.plusMonths(1) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_right),
                        contentDescription = localizedStringResource(R.string.statistics_next_month),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier.size(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        DonutChart(
                            shares = shares,
                            colors = categoryColors,
                            modifier = Modifier.fillMaxSize()
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = moneyText(monthTotal),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = localizedStringResource(R.string.subscription_per_month),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (categoryTotals.isEmpty()) {
                        EmptyMonthText()
                    } else {
                        categoryTotals.forEachIndexed { index, (category, _) ->
                            LegendRow(
                                color = categoryColors[index],
                                category = category,
                                percent = percents[index]
                            )
                        }
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard(
                    icon = R.drawable.ic_wallet,
                    value = moneyText(monthTotal),
                    label = localizedStringResource(R.string.subscription_per_month),
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    icon = R.drawable.ic_calendar,
                    value = moneyText(yearTotal),
                    label = localizedStringResource(R.string.subscription_per_year),
                    modifier = Modifier.weight(1f)
                )
            }

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = localizedStringResource(R.string.statistics_expenses),
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (payments.isEmpty()) {
                        EmptyMonthText()
                    } else {
                        val maxPrice = payments.maxOf { it.first.price }
                        payments.forEach { (subscription, date) ->
                            ExpenseRow(
                                subscription = subscription,
                                date = date,
                                maxPrice = maxPrice
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticsScreenPreview() {
    PreviewWrapper {
        StatisticsScreen(
            subscriptions = sampleSubscriptions,
            categories = sampleCategories
        )
    }
}

@Composable
private fun LegendRow(
    color: Color,
    category: Category?,
    percent: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = categoryTitle(category),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        Text(
            text = "$percent%",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EmptyMonthText() {
    Text(
        text = localizedStringResource(R.string.statistics_empty),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}
