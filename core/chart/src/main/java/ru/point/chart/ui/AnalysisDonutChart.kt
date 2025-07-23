package ru.point.chart.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import ru.point.api.model.CategorySummary

@Composable
fun AnalysisDonutChart(
    summaries: List<CategorySummary>,
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        Color(0xFFFCE300), Color(0xFF2AE881),
        Color(0xFFE46962), Color(0xFF306A42),
        Color(0xFF795548), Color(0xFF607D8B)
    )
) {

    val displayData = remember(summaries) { prepareForPie(summaries) }

    val totalPercent = remember(displayData) {
        displayData.fold(BigDecimal.ZERO) { acc, cat ->
            acc + cat.percentage
        }
    }


    Box(modifier) {
        Canvas(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.Center)
        ) {
            var startAngle = -90f
            val thickness = 24f
            val diameter = size.minDimension
            val radius = diameter / 2f
            val topLeft = Offset(center.x - radius, center.y - radius)
            var accumulatedSweep = 0f

            displayData.forEachIndexed { index, cat ->
                val sweepAngle = if (index == displayData.lastIndex) {
                    360f - accumulatedSweep
                } else {
                    (cat.percentage.toFloat() / totalPercent.toFloat() * 360f)
                }

                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(diameter, diameter),
                    style = Stroke(width = thickness)
                )

                startAngle += sweepAngle
                accumulatedSweep += sweepAngle
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.Start
        ) {
            displayData.forEachIndexed { i, cat ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Box(
                        Modifier
                            .size(8.dp)
                            .background(colors[i % colors.size], CircleShape)
                    )
                    Text(
                        text = "${cat.emoji} ${cat.percentage}% ${cat.categoryName}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 8.sp
                        ),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

private fun prepareForPie(
    original: List<CategorySummary>,
    minPercent: BigDecimal = BigDecimal(5),
    maxSlices: Int = 4
): List<CategorySummary> {
    if (original.isEmpty()) return emptyList()

    val sorted = original.sortedByDescending { it.percentage }

    val majors = sorted.filter { it.percentage >= minPercent }

    val visibleMajors = majors.take(maxSlices)

    val others = (sorted - visibleMajors.toSet())
    val otherPercent = others.fold(BigDecimal.ZERO) { acc, s -> acc + s.percentage }
    val otherAmount = others.fold(BigDecimal.ZERO) { acc, s -> acc + s.totalAmount }

    return buildList {
        addAll(visibleMajors)
        if (otherPercent > BigDecimal.ZERO) {
            add(
                CategorySummary(
                    categoryId = -1,
                    categoryName = "Прочее",
                    emoji = "📦",
                    totalAmount = otherAmount,
                    percentage = otherPercent
                )
            )
        }
    }
}