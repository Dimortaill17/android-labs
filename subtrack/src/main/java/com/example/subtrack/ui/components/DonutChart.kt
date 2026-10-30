package com.example.subtrack.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.subtrack.ui.theme.ChartColors
import com.example.subtrack.ui.theme.ChartEmpty

@Composable
fun DonutChart(
    shares: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 28.dp
) {
    Canvas(modifier = modifier) {
        val strokePx = strokeWidth.toPx()
        val topLeft = Offset(strokePx / 2, strokePx / 2)
        val arcSize = Size(size.width - strokePx, size.height - strokePx)

        if (shares.isEmpty()) {
            drawArc(
                color = ChartEmpty,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx)
            )
            return@Canvas
        }

        var startAngle = -90f
        shares.forEachIndexed { index, share ->
            val sweep = share * 360f
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx)
            )
            startAngle += sweep
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DonutChartPreview() {
    PreviewWrapper {
        DonutChart(
            shares = listOf(0.5f, 0.3f, 0.2f),
            colors = ChartColors,
            modifier = Modifier.size(160.dp)
        )
    }
}
