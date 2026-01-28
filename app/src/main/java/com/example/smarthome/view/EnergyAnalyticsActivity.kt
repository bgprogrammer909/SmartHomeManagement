package com.example.smarthome.view

import android.R.attr.name
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.R
import com.example.smarthome.model.EnergyModel
import com.example.smarthome.model.EnergyPoint
import com.example.smarthome.viewmodel.EnergyViewModel
import androidx.compose.ui.platform.testTag


class EnergyAnalyticsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: EnergyViewModel = viewModel(
                factory = EnergyViewModelFactory()
            )

            val securityViewModel: SecurityViewModel = viewModel(
                factory = SecurityViewModelFactory(userId ?: "")

            )

            EnergyAnalyticsScreen(
                viewModel = viewModel,
                securityViewModel = securityViewModel,
                onBack = { finish() }
            )
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
            .testTag("energyLazyColumn")
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0A1A2F), Color(0xFF05101F))
                )
            )
            .statusBarsPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier .testTag("backButton").clickable { onBack() }
            ) {
                Text("← Back", color = Color.White, fontSize = 16.sp)
            }
        }

        item {
            Column {
                Text(
                    "Energy Analytics",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Track your power consumption",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
        }

        item {
            TotalUsageCard(state, selectedTab)
        }

        item {
            TabRowSection(selectedTab) { selectedTab = it }
        }

        item {
            GraphCardWithChart(selectedTab, state)
        }

        item { UsageItem("Lights", state.lightsUsage, Color(0xFFFFD740)) }
        item { UsageItem("AC", state.acUsage, Color(0xFF4CC3FF)) }
        item { UsageItem("Water Pump", state.waterPumpUsage, Color(0xFF3C6DFF)) }
        item { UsageItem("Others", state.othersUsage, Color(0xFFCE93D8)) }

        item { EstimatedBillCard(state) }
        item { TipsCard() }

        item { Spacer(Modifier.height(50.dp)) }
    }
     if (showDialog) {
         AlertDialog(
             onDismissRequest = {
                 showDialog = false
                 securityViewModel?.dismissMotionAlert()

             },
             confirmButton = {
                 TextButton(
                     onClick = {
                         showDialog = false
                         securityViewModel?.dismissMotionAlert()

                     }
                 ) {
                     Text("OK", color = Color(0xFF1FB7FF))
                 }
             },
             title = {
                 Text("⚠️ Motion Detected!", fontWeight = FontWeight.Bold)
             },
             text = {
                 Text("Motion has been detected in your home. Please check your security cameras.")
             },
             containerColor = Color(0xFF1C1C2E),
             titleContentColor = Color.White,
             textContentColor = Color(0xFF9AB3C8)
         )
     }


 }

@Composable
fun TotalUsageCard(state: EnergyModel, selectedTab: String) {

    val graphData = when (selectedTab) {
        "Day" -> state.dayData
        "Week" -> state.weekData
        "Month" -> state.monthData
        else -> state.weekData
    }

    Card(
        modifier = Modifier.fillMaxWidth() .testTag("totalUsageCard"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1A5A))
    ) {
        Column(Modifier.padding(20.dp)) {

            Text(
                "${state.totalUsage.toInt()} kWh",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            MiniUsageGraph(graphData)
        }
    }
}

@Composable
fun MiniUsageGraph(data: List<EnergyPoint>) {
    if (data.size < 2) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF3A2E63)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF3A2E63))
                .padding(12.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val maxY = data.maxOf { it.kw }.coerceAtLeast(1f)
                val xGap = size.width / (data.size - 1)

                val points = data.mapIndexed { index, point ->
                    Offset(
                        x = xGap * index,
                        y = size.height - (point.kw / maxY * size.height)
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
                    style = Stroke(width = 4f, cap = StrokeCap.Round)
                )

                points.forEach {
                    drawCircle(
                        color = Color(0xFFB388FF),
                        radius = 6f,
                        center = it
                    )
                }
            }
        }
    }
}

@Composable
fun TabRowSection(
    selectedTab: String,
    onTabChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TabChip("Day", selectedTab == "Day") { onTabChange("Day") }
        TabChip("Week", selectedTab == "Week") { onTabChange("Week") }
        TabChip("Month", selectedTab == "Month") { onTabChange("Month") }
    }
}
@Composable
fun TabChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .testTag("${text}Tab")
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (selected) Color(0xFF764CFF)
                else Color(0xFF1B2945)
            )
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Text(
            text,
            color = if (selected) Color.White
            else Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp
        )
    }
}
@Composable
fun GraphCardWithChart(
    selectedTab: String,
    state: EnergyModel
) {
    val data = when (selectedTab) {
        "Day" -> state.dayData
        "Week" -> state.weekData
        "Month" -> state.monthData
        else -> state.weekData
    }

    Card(
        modifier = Modifier.fillMaxWidth() .testTag("energyGraph"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF14203D)
        )
    ) {
        Column(Modifier.padding(20.dp)) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Energy Consumption",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text("📅", fontSize = 20.sp)
            }

            Spacer(Modifier.height(16.dp))

            RealLineGraph(data)
        }
    }
}
@Composable
fun RealLineGraph(data: List<EnergyPoint>) {

    if (data.size < 2) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        val maxY = data.maxOf { it.kw }.coerceAtLeast(1f)
        val xGap = size.width / (data.size - 1)

        val points = data.mapIndexed { index, point ->
            Offset(
                x = xGap * index,
                y = size.height - (point.kw / maxY * size.height)
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
                radius = 8f,
                center = it
            )
        }
    }
}
@Composable
fun UsageItem(label: String, percent: Int, color: Color) {

    val tag = label.replace(" ", "") + "UsageItem"

    Card(
        modifier = Modifier.fillMaxWidth() .testTag(tag),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F1F33)
        )
    ) {
        Column(Modifier.padding(18.dp)) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label, color = Color.White)
                Text("$safePercent%", color = Color.White)
            }

            Spacer(Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = safePercent / 100f,
                color = color,
                trackColor = Color(0xFF3A4A63),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
@Composable
fun EstimatedBillCard(state: EnergyModel) {
    Card(
        modifier = Modifier.fillMaxWidth() .testTag("estimatedBillCard"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0C3B2E)
        )
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Estimated Bill", color = Color.White)
            Text(
                "$${String.format("%.2f", state.estimatedBill)}",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
fun TipsCard() {
    Card(
        modifier = Modifier.fillMaxWidth() .testTag("energyTipsCard"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2538)
        )
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Energy Saving Tips", color = Color.White)
            Spacer(Modifier.height(8.dp))
            TipItem("Reduce AC usage during off-peak hours")
            TipItem("Turn off lights when not in use")
            TipItem("Schedule water pump during optimal times")
        }
    }
}
@Composable
fun TipItem(text: String) {
    Text(
        "• $text",
        color = Color.White.copy(alpha = 0.8f),
        fontSize = 14.sp
    )
}
