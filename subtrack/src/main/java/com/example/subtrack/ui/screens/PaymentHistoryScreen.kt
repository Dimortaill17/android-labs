package com.example.subtrack.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.subtrack.R
import com.example.subtrack.data.utils.Formats
import com.example.subtrack.data.utils.LocalAppLocale
import com.example.subtrack.data.utils.localizedStringResource
import com.example.subtrack.data.utils.moneyText
import com.example.subtrack.domain.Payment
import com.example.subtrack.domain.PaymentStatus
import com.example.subtrack.domain.Subscription
import com.example.subtrack.ui.components.PreviewWrapper
import com.example.subtrack.ui.components.sampleSubscription
import com.example.subtrack.ui.theme.Missed
import com.example.subtrack.ui.theme.Paid
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentHistoryScreen(
    subscription: Subscription,
    payments: List<Payment>,
    onBack: () -> Unit,
    onStatusChange: (Payment, PaymentStatus?) -> Unit
) {
    val paidCount = payments.count { it.status == PaymentStatus.PAID }
    val failedCount = payments.count { it.status == PaymentStatus.FAILED }
    val missedCount = payments.count { it.status == PaymentStatus.MISSED }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = localizedStringResource(R.string.history_title))
                        Text(
                            text = subscription.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
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
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = localizedStringResource(R.string.history_paid_total),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = moneyText(paidCount * subscription.price),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.padding(top = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CountItem(
                                label = localizedStringResource(R.string.history_count_paid),
                                count = paidCount,
                                color = Paid,
                                modifier = Modifier.weight(1f)
                            )
                            CountItem(
                                label = localizedStringResource(R.string.history_count_failed),
                                count = failedCount,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.weight(1f)
                            )
                            CountItem(
                                label = localizedStringResource(R.string.history_count_missed),
                                count = missedCount,
                                color = Missed,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = localizedStringResource(R.string.history_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(payments) { payment ->
                PaymentItem(
                    payment = payment,
                    price = subscription.price,
                    onStatusChange = onStatusChange
                )
            }
        }
    }
}

@Composable
private fun CountItem(
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.12f),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        }
    }
}

@Composable
private fun PaymentItem(
    payment: Payment,
    price: Double,
    onStatusChange: (Payment, PaymentStatus?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            ListItem(
                modifier = Modifier.clickable { expanded = true },
                headlineContent = {
                    Text(
                        text = Formats.longDate(payment.date, LocalAppLocale.current),
                        fontWeight = FontWeight.Bold
                    )
                },
                supportingContent = { Text(text = moneyText(price)) },
                trailingContent = { StatusLabel(status = payment.status) }
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = localizedStringResource(R.string.payment_mark_paid)) },
                onClick = {
                    onStatusChange(payment, PaymentStatus.PAID)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = localizedStringResource(R.string.payment_mark_failed)) },
                onClick = {
                    onStatusChange(payment, PaymentStatus.FAILED)
                    expanded = false
                }
            )
            if (payment.status == PaymentStatus.PAID || payment.status == PaymentStatus.FAILED) {
                DropdownMenuItem(
                    text = { Text(text = localizedStringResource(R.string.payment_mark_clear)) },
                    onClick = {
                        onStatusChange(payment, null)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun StatusLabel(status: PaymentStatus) {
    val color = when (status) {
        PaymentStatus.PAID -> Paid
        PaymentStatus.FAILED -> MaterialTheme.colorScheme.error
        PaymentStatus.MISSED -> Missed
        PaymentStatus.UPCOMING -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val text = when (status) {
        PaymentStatus.PAID -> R.string.payment_status_paid
        PaymentStatus.FAILED -> R.string.payment_status_failed
        PaymentStatus.MISSED -> R.string.payment_status_missed
        PaymentStatus.UPCOMING -> R.string.payment_status_upcoming
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.12f)
    ) {
        Text(
            text = localizedStringResource(text),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentHistoryScreenPreview() {
    PreviewWrapper {
        PaymentHistoryScreen(
            subscription = sampleSubscription,
            payments = listOf(
                Payment(sampleSubscription.id, LocalDate.now().plusDays(5), PaymentStatus.UPCOMING),
                Payment(sampleSubscription.id, LocalDate.now().minusMonths(1), PaymentStatus.PAID),
                Payment(sampleSubscription.id, LocalDate.now().minusMonths(2), PaymentStatus.FAILED),
                Payment(sampleSubscription.id, LocalDate.now().minusMonths(3), PaymentStatus.MISSED)
            ),
            onBack = {},
            onStatusChange = { _, _ -> }
        )
    }
}
