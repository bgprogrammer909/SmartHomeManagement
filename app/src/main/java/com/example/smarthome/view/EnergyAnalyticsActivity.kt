package com.example.smarthome.view

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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.R
import com.example.smarthome.viewmodel.EnergyViewModel

class EnergyAnalyticsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: EnergyViewModel = viewModel()
            EnergyAnalyticsScreen(viewModel = viewModel, onBack = { finish() })
        }
    }
}

@Composable
fun EnergyAnalyticsScreen(viewModel: EnergyViewModel, onBack: () -> Unit) {
    val state by viewModel.state
    var selectedTab by remember { mutableStateOf("Week") }
    val context = LocalContext.current
    val activity = context as? ComponentActivity

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

        // BACK BUTTON
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onBack() }
            ) {
                Text("← Back", color = Color.White, fontSize = 16.sp)
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

        // TOTAL USAGE CARD
        item {
            TotalUsageCard(state)
        }

        item {
            TabRowSection(
                selectedTab = selectedTab,
                onTabChange = { selectedTab = it }
            )
        }

        item {
            GraphCardWithChart(selectedTab, state)
        }

        // USAGE ITEMS
        item { UsageItem("Lights", state.lightsUsage, Color(0xFFFFD740)) }
        item { UsageItem("AC", state.acUsage, Color(0xFF4CC3FF)) }
        item { UsageItem("Water Pump", state.waterPumpUsage, Color(0xFF3C6DFF)) }
        item { UsageItem("Others", state.othersUsage, Color(0xFFCE93D8)) }

        // BILL + TIPS
        item { EstimatedBillCard(state) }
        item { TipsCard() }

            item { Spacer(modifier = Modifier.height(50.dp)) }
        }
    }
}

/////////////////////////////////////////////////////////////////
// TOTAL USAGE CARD
////////////////////////////////////////////////////////////////
@Composable
fun TotalUsageCard(state: com.example.smarthome.model.EnergyModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1A5A))
    ) {
        Column(Modifier.padding(20.dp)) {

            Text("Total Usage", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${state.totalUsage.toInt()} kWh",
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF7A4FFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚡", fontSize = 24.sp)
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                "${state.percentageDiff}% less than week",
                color = Color(0xFF76FF7A),
                fontSize = 14.sp
            )

            Spacer(Modifier.height(18.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                MiniUsageBox("Today", "${state.todayUsage.toInt()} kWh")
                MiniUsageBox("This Week", "${state.weekUsage.toInt()} kWh")
                MiniUsageBox("This Month", "${state.monthUsage.toInt()} kWh")
            }
        }
    }
}

@Composable
fun MiniUsageBox(title: String, value: String) {
    Column(
        modifier = Modifier
            .width(95.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF3A2E63))
            .padding(14.dp)
    ) {
        Text(title, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
        Text(value, color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
    }
}

/////////////////////////////////////////////////////////////////
// TABS
////////////////////////////////////////////////////////////////
@Composable
fun TabRowSection(selectedTab: String, onTabChange: (String) -> Unit) {
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
fun TabChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (selected) Color(0xFF764CFF) else Color(0xFF1B2945)
            )
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Text(
            text,
            color = if (selected) Color.White else Color.White.copy(alpha = 0.8f),
            fontSize = 16.sp
        )
    }
}

/////////////////////////////////////////////////////////////////
// GRAPH + TITLE + CALENDAR ICON
////////////////////////////////////////////////////////////////
@Composable
fun GraphCardWithChart(selectedTab: String, state: com.example.smarthome.model.EnergyModel) {

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

            FakeLineGraph(data)
        }
    }
}

/////////////////////////////////////////////////////////////////
// FAKE GRAPH USING CANVAS
////////////////////////////////////////////////////////////////
@Composable
fun FakeLineGraph(data: List<Float>) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {

        val width = size.width
        val height = size.height

        drawRoundRect(
            color = Color(0xFF20304D),
            size = size,
            cornerRadius = CornerRadius(20f, 20f)
        )

        val xGap = width / data.size
        val maxY = 120f

        val points = data.mapIndexed { index, value ->
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

        points.forEach { p ->
            drawCircle(
                color = Color(0xFFB388FF),
                radius = 10f,
                center = p
            )
        }
    }
}

/////////////////////////////////////////////////////////////////
// USAGE ITEM
////////////////////////////////////////////////////////////////
@Composable
fun UsageItem(label: String, percent: Int, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1F33))
    ) {
        Column(Modifier.padding(18.dp)) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(50))
                            .background(color)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(label, color = Color.White, fontSize = 16.sp)
                }

                Text("$percent%", color = Color.White)
            }

            Spacer(Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = percent / 100f,
                color = color,
                trackColor = Color(0xFF3A4A63),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/////////////////////////////////////////////////////////////////
// ESTIMATED BILL
////////////////////////////////////////////////////////////////
@Composable
fun EstimatedBillCard(state: com.example.smarthome.model.EnergyModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0C3B2E))
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Estimated Bill", color = Color.White, fontSize = 14.sp)
            Text(
                "$${String.format("%.2f", state.estimatedBill)}",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text(
                "You're saving $${String.format("%.2f", state.savings)} this month compared to your average usage.",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
        }
    }
}

/////////////////////////////////////////////////////////////////
// TIPS
////////////////////////////////////////////////////////////////
@Composable
fun TipsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2538))
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Energy Saving Tips", color = Color.White, fontSize = 18.sp)
            Spacer(Modifier.height(10.dp))
            TipItem("Reduce AC usage during off-peak hours")
            TipItem("Turn off lights when not in use")
            TipItem("Schedule water pump during optimal times")
        }
    }
}

@Composable
fun TipItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF6CCAFF))
        )
        Spacer(Modifier.width(10.dp))
        Text(text, color = Color.White)
    }
}

/////////////////////////////////////////////////////////////////
// PREVIEW
////////////////////////////////////////////////////////////////
@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEnergyAnalytics() {
    val previewViewModel = EnergyViewModel()
    EnergyAnalyticsScreen(viewModel = previewViewModel, onBack = {})
}
