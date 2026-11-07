package com.example.subtrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ServiceIcon(
    name: String,
    color: Int,
    modifier: Modifier = Modifier,
    size: Dp = 46.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(color)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.trim().firstOrNull()?.uppercase() ?: "?",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ServiceIconPreview() {
    PreviewWrapper {
        ServiceIcon(
            name = sampleSubscription.name,
            color = sampleSubscription.color
        )
    }
}
