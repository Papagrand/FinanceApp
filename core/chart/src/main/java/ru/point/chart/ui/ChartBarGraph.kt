package ru.point.chart.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import ru.point.api.model.ChartEntry
import ru.point.utils.extensionsAndParsers.formatAxisValue

@OptIn(ExperimentalTextApi::class)
@Composable
fun ChartBarGraph(
    entries: List<ChartEntry>,
    modifier: Modifier = Modifier,
) {
    if (entries.isEmpty()) return

    val maxAbs = entries.maxOf { it.totalAmount.abs() }
    val halfMax = maxAbs.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP)

    val labelsY = listOf(BigDecimal.ZERO, halfMax, maxAbs)

    val dens = LocalDensity.current
    val barW = with(dens) { 6.dp.toPx() }
    val radius = CornerRadius(with(dens) { 16.dp.toPx() })
    val leftPad = with(dens) { 40.dp.toPx() }
    val datePad = with(dens) { 2.dp.toPx() }

    val midLine = MaterialTheme.colorScheme.primary
    val positive = Color(0xFF2AE881)
    val neutral = MaterialTheme.colorScheme.tertiaryContainer
    val negative = MaterialTheme.colorScheme.error

    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
    val tm = rememberTextMeasurer()
    val df = DateTimeFormatter.ofPattern("MM.dd")

    val linesColor = MaterialTheme.colorScheme.onSurfaceVariant

    val labelColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {

        val graphH = size.height * 0.90f
        val stepY = graphH / (labelsY.size - 1)

        labelsY.forEachIndexed { i, v ->
            val y = graphH - i * stepY
            drawLine(
                color = if (i == 1) midLine
                else linesColor,
                start = Offset(leftPad, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx(),
                pathEffect = dashEffect
            )
            drawText(
                tm, formatAxisValue(v),
                topLeft = Offset(with(dens) { 4.dp.toPx() }, y - 6.sp.toPx()),
                style = androidx.compose.ui.text.TextStyle(
                    color = labelColor,
                    fontSize = 8.sp
                )
            )
        }

        val availableW = size.width - leftPad
        val gaps = entries.size - 1
        val space = if (gaps > 0) ((availableW - barW * entries.size) / gaps).coerceAtLeast(0f) else 0f


        entries.forEachIndexed { i, e ->
            val frac = e.totalAmount.abs().divide(maxAbs, 4, RoundingMode.HALF_UP).toFloat()
            val h = graphH * frac
            val xC = leftPad + i * (barW + space) + barW / 2f
            drawRoundRect(
                color = when (e.whatsMore) {
                    0 -> neutral
                    1 -> positive
                    2 -> negative
                    else -> neutral
                },
                topLeft = Offset(xC - barW / 2, graphH - h),
                size = Size(barW, h),
                cornerRadius = radius
            )
        }

        val total = entries.lastIndex
        val maxLabels = 4
        val step = ceil(total.toFloat() / (maxLabels - 1)).toInt()

        val ticks = (0..total step step).toMutableSet()
        ticks += total

        ticks.sorted().forEach { i ->
            val label = entries[i].date.format(df)
            val textW = tm.measure(label).size.width
            val xC = leftPad + i * (barW + space) + barW / 2f
            val xFix = (xC - textW / 2)
                .coerceIn(leftPad, size.width - textW)
            drawText(
                tm, label, topLeft = Offset(xFix, graphH + datePad),
                style = androidx.compose.ui.text.TextStyle(
                    color = labelColor,
                    fontSize = 8.sp
                )
            )
        }
    }
}