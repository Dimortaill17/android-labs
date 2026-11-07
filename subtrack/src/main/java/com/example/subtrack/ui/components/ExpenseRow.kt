package com.example.subtrack.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.subtrack.data.utils.Formats
import com.example.subtrack.domain.Subscription
import java.time.LocalDate

@Composable
fun ExpenseRow(
    subscription: Subscription,
    date: LocalDate,
    maxPrice: Double,
    modifier: Modifier = Modifier
) {
    val fraction = if (maxPrice > 0) (subscription.price / maxPrice).toFloat() else 0f

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ServiceIcon(
            name = subscription.name,
            color = subscription.color,
            size = 36.dp
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = subscription.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = Formats.longDate(date),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LinearProgressIndicator(
            progress = { fraction.coerceIn(0.1f, 1f) },
            gapSize = 0.dp,
            drawStopIndicator = {},
            trackColor = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .width(64.dp)
        )

        Text(
            text = Formats.money(subscription.price),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpenseRowPreview() {
    PreviewWrapper {
        ExpenseRow(
            subscription = sampleSubscription,
            date = sampleSubscription.startDate,
            maxPrice = 699.0
        )
    }
}
