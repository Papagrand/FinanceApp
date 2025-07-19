package ru.point.transactions.analysis.ui.composable.composableFunctions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.jetchart.common.animation.fadeInAnimation
import io.jetchart.pie.PieChart
import io.jetchart.pie.Pies
import io.jetchart.pie.Slice
import io.jetchart.pie.renderer.FilledSliceDrawer
import java.math.BigDecimal
import ru.point.transactions.analysis.domain.model.CategorySummary

@Composable
fun AnalysisDonutChart(
    summaries: List<CategorySummary>,
    modifier: Modifier = Modifier,
    palette: List<Color> = listOf(
        Color(0xFFFCE300), Color(0xFF2AE881),
        Color(0xFFE46962), Color(0xFF306A42),
        Color(0xFF795548), Color(0xFF607D8B)
    )
) {

    val displayData = remember(summaries) { prepareForPie(summaries) }

    val slices = remember(displayData) {
        displayData.mapIndexed { idx, cat ->
            Slice(
                value = cat.percentage.toFloat(),
                color = palette[idx % palette.size]
            )
        }
    }

    Box(modifier) {

        PieChart(
            pies = Pies(slices),
            modifier = Modifier
                .size(180.dp)
                .rotate(-90f)
                .align(Alignment.Center),
            sliceDrawer = FilledSliceDrawer(thickness = 8f),
            animation = fadeInAnimation(0)
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.Start
        ) {
            displayData.forEachIndexed { i, cat ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(8.dp)
                            .background(palette[i % palette.size], CircleShape)
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

@Preview(
    showBackground = true,
    widthDp = 412,
    heightDp = 200,
    backgroundColor = 0xFFF0F0F0  // серый фон, как у system UI-Mode
)
@Composable
private fun CategoryDonutChartPreview() {
    // Примерные данные (₽ — любая валюта, BigDecimal «для красоты»)
    val sampleSummaries = listOf(
        CategorySummary(
            categoryId = 1,
            categoryName = "Еда",
            emoji = "🍔",
            totalAmount = BigDecimal(3500),
            percentage = BigDecimal("30.0")
        ),
        CategorySummary(
            categoryId = 2,
            categoryName = "Транспорт",
            emoji = "🚌",
            totalAmount = BigDecimal(2000),
            percentage = BigDecimal("20.0")
        ),
        CategorySummary(
            categoryId = 3,
            categoryName = "Развлечения",
            emoji = "🎬",
            totalAmount = BigDecimal(1500),
            percentage = BigDecimal("15.0")
        ),
        CategorySummary(
            categoryId = 4,
            categoryName = "Коммуналка",
            emoji = "🔧",
            totalAmount = BigDecimal(3000),
            percentage = BigDecimal("15.0")
        ),
        CategorySummary(
            categoryId = 4,
            categoryName = "w",
            emoji = "🔧",
            totalAmount = BigDecimal(3000),
            percentage = BigDecimal("6.0")
        ),
        CategorySummary(
            categoryId = 4,
            categoryName = "a",
            emoji = "🔧",
            totalAmount = BigDecimal(3000),
            percentage = BigDecimal("2.0")
        ),
        CategorySummary(
            categoryId = 4,
            categoryName = "s",
            emoji = "🔧",
            totalAmount = BigDecimal(3000),
            percentage = BigDecimal("4.0")
        ),
        CategorySummary(
            categoryId = 4,
            categoryName = "d",
            emoji = "🔧",
            totalAmount = BigDecimal(3000),
            percentage = BigDecimal("4.0")
        ),
        CategorySummary(
            categoryId = 4,
            categoryName = "f",
            emoji = "🔧",
            totalAmount = BigDecimal(3000),
            percentage = BigDecimal("4.0")
        )
    )

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnalysisDonutChart(
                summaries = sampleSummaries,
                modifier = Modifier.size(240.dp)
            )
        }
    }
}