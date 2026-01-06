package com.example.smarthome.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.model.EnergyModel
import com.example.smarthome.viewmodel.EnergyViewModel

class EnergyAnalyticsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: EnergyViewModel = viewModel()
            EnergyAnalyticsScreen(viewModel) { finish() }
        }
    }
}

@Composable
fun EnergyAnalyticsScreen(viewModel: EnergyViewModel, onBack: () -> Unit) {
    val state by viewModel.state
    var selectedTab by remember { mutableStateOf("Week") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0A1A2F), Color(0xFF05101F))
                )
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            Text(
                "← Back",
                color = Color.White,
                modifier = Modifier.clickable { onBack() }
            )
        }

        item {
            Text(
                "Energy Analytics",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item { GraphCardWithChart(selectedTab, state) }

        item {
            Button(
                onClick = {
                    when (selectedTab) {
                        "Day" -> viewModel.addDayValue((20..100).random().toFloat())
                        "Week" -> viewModel.addWeekValue((80..120).random().toFloat())
                        "Month" -> viewModel.addMonthValue((60..110).random().toFloat())
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Random Value (Live Update)")
            }
        }
    }
}

@Composable
fun GraphCardWithChart(selectedTab: String, state: EnergyModel) {

    val data = when (selectedTab) {
        "Day" -> state.dayData
        "Week" -> state.weekData
        "Month" -> state.monthData
        else -> state.weekData
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14203D))
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                "Energy Consumption",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(16.dp))
            AnimatedLineGraph(data)
        }
    }
}

@Composable
fun AnimatedLineGraph(data: List<Float>) {

    val animatedData = data.map {
        animateFloatAsState(
            targetValue = it,
            animationSpec = tween(600)
        ).value
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {

        val width = size.width
        val height = size.height
        val maxY = (animatedData.maxOrNull() ?: 1f) * 1.2f
        val xGap = width / animatedData.size

        drawRoundRect(
            color = Color(0xFF20304D),
            size = size,
            cornerRadius = CornerRadius(20f)
        )

        val points = animatedData.mapIndexed { index, value ->
            Offset(
                x = xGap * index + xGap / 2,
                y = height - (value / maxY * height)
            )
        }

        val path = Path()
        points.forEachIndexed { i, p ->
            if (i == 0) path.moveTo(p.x, p.y)
            else path.lineTo(p.x, p.y)
        }

        drawPath(
            path = path,
            color = Color(0xFFB388FF),
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )

        points.forEach {
            drawCircle(
                color = Color(0xFFB388FF),
                radius = 9f,
                center = it
            )
        }
    }
}
