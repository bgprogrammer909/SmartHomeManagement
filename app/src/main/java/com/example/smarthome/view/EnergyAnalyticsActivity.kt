package com.example.smarthome.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.R
import com.example.smarthome.model.EnergyModel
import com.example.smarthome.model.EnergyPoint
import com.example.smarthome.viewmodel.EnergyViewModel
import com.example.smarthome.viewmodel.EnergyViewModelFactory

import com.example.smarthome.viewmodel.SecurityViewModel


class EnergyAnalyticsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userId = intent.getStringExtra("USER_ID") ?: return

        setContent {
            val viewModel: EnergyViewModel = viewModel(
                factory = EnergyViewModelFactory(userId)
            )
            EnergyAnalyticsScreen(viewModel = viewModel, onBack = { finish() })
        }

    }
}



@Composable

fun EnergyAnalyticsScreen(
    viewModel: EnergyViewModel,
    securityViewModel: SecurityViewModel? = null,
    onBack: () -> Unit
)
 {
     val state by viewModel.state
     val showMotionAlert by remember {
         derivedStateOf { securityViewModel?.showMotionAlert?.value == true }
     }

     var showDialog by remember { mutableStateOf(false) }

     LaunchedEffect(showMotionAlert) {
         if (showMotionAlert) {
             showDialog = true
         }
     }

     var selectedTab by remember { mutableStateOf("Week") }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF0A1A2F), Color(0xFF05101F)))
            )
            .statusBarsPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            Text("← Back", color = Color.White, modifier = Modifier.clickable { onBack() })
        }

        item {
            Text("Energy Analytics", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }

        item {
            TotalUsageCard(state.totalKwh, state.graphData.size)
        }

        item {
            GraphCard(state.graphData)
        }

        item {
            if (state.graphData.isEmpty()) {
                NoDataListCard()
            } else {
                DataListCard(state.graphData)
            }
        }

        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}

@Composable
fun TotalUsageCard(totalKwh: Float, dataPoints: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1A5A)),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Total Usage", color = Color.White.copy(0.6f))
            Text("${"%.2f".format(totalKwh)} kWh", color = Color.White, fontSize = 32.sp)
            Text("$dataPoints data points", color = Color(0xFF76FF7A))
        }
    }
}

@Composable
fun GraphCard(data: List<Pair<String, Float>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14203D)),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Energy Consumption", color = Color.White)

            Spacer(Modifier.height(16.dp))

            when {
                data.isEmpty() -> NoDataGraph()
                data.size == 1 -> SinglePointGraph(data.first())
                else -> EnergyLineGraph(data)
            }
        }
    }
}

@Composable
fun SinglePointGraph(point: Pair<String, Float>) {
    Canvas(Modifier.fillMaxWidth().height(220.dp)) {
        val center = Offset(size.width / 2, size.height / 2)

        drawCircle(Color(0xFF764CFF), 20f, center)
        drawCircle(Color.White, 8f, center)

        drawContext.canvas.nativeCanvas.drawText(
            "${"%.2f".format(point.second)} kWh",
            center.x,
            center.y + 60,
            android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 36f
                textAlign = android.graphics.Paint.Align.CENTER
            }
        )
    }
}

@Composable
fun EnergyLineGraph(data: List<Pair<String, Float>>) {
    Canvas(Modifier.fillMaxWidth().height(220.dp)) {
        val padding = 50f
        val max = data.maxOf { it.second }
        val min = data.minOf { it.second }.coerceAtMost(max - 1)

        val points = data.mapIndexed { i, (_, v) ->
            Offset(
                padding + i * (size.width - padding * 2) / (data.size - 1),
                size.height - padding - (v - min) / (max - min) * (size.height - padding * 2)
            )
        }

        val path = Path().apply {
            points.forEachIndexed { i, p -> if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
        }

        drawPath(path, Color(0xFFB388FF), style = Stroke(6f))

        points.forEach {
            drawCircle(Color(0xFF764CFF), 10f, it)
            drawCircle(Color.White, 5f, it)
        }
    }
}

@Composable
fun NoDataGraph() {
    Box(Modifier.fillMaxWidth().height(220.dp), contentAlignment = Alignment.Center) {
        Text("No energy data", color = Color.White.copy(0.5f))
    }
}

@Composable
fun DataListCard(data: List<Pair<String, Float>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14203D)),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Daily Breakdown", color = Color.White)
            Spacer(Modifier.height(12.dp))
            data.forEach { (date, kwh) ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(date, color = Color.White.copy(0.8f))
                    Text("${"%.2f".format(kwh)} kWh", color = Color(0xFF4CC3FF))
                }
            }
        }
    }
}

@Composable
fun NoDataListCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2538)),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("📄", fontSize = 36.sp)
            Text("No records available", color = Color.White)
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@androidx.compose.ui.tooling.preview.Preview(showSystemUi = true)
@Composable
fun PreviewEnergyAnalytics() {
    EnergyAnalyticsScreen(viewModel = EnergyViewModel(), onBack = {})
}
