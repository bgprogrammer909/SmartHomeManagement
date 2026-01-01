package com.example.smarthome.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.viewmodel.WaterViewModel
import com.example.smarthome.view.ui.theme.SmartHomeTheme
import com.example.smarthome.model.WaterModel

class WaterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartHomeTheme {
                val viewModel: WaterViewModel = viewModel()
                val state by viewModel.state.collectAsState()
                WaterBody(state = state, viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterBody(
    state: WaterModel,
    viewModel: WaterViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var triggerAlert by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(triggerAlert) {
        if (triggerAlert) {
            snackbarHostState.showSnackbar("Water did not reach expected level!")
            triggerAlert = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0B132B), Color(0xFF1C1C2E))
                    )
                )
        ) {
            PumpStatusCard(
                state = state,
                viewModel = viewModel,
                triggerAlert = triggerAlert,
                onTriggerAlert = { triggerAlert = it },
                context = context
            )
        }
    }
}

@Composable
fun PumpStatusCard(
    state: WaterModel,
    viewModel: WaterViewModel,
    triggerAlert: Boolean,
    onTriggerAlert: (Boolean) -> Unit,
    context: android.content.Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Back",
                color = Color(0xFF9DB9D0),
                fontSize = 16.sp,
                modifier = Modifier.clickable { (context as? ComponentActivity)?.finish() }
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Water Pump",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            "Control and schedule your water pump",
            color = Color(0xFF9AB3C8),
            fontSize = 14.sp
        )

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF113A5B), Color(0xFF0E2B4C), Color(0xFF112B4C))
                    )
                )
                .padding(18.dp)
        ) {
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Pump Status", color = Color.White.copy(alpha = 0.7f))
                        Text(
                            if (state.isPumpOn) "On" else "Off",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text("", fontSize = 40.sp)
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PumpInfoBox("Today's Usage", String.format("%.1f L", state.todayUsage))
                    PumpInfoBox("Flow Rate", String.format("%.1f L/min", state.flowRate))
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = { viewModel.togglePump() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2295F3)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(if (state.isPumpOn) "Turn Off" else "Turn On")
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0E2433))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("", fontSize = 28.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Auto Mode", color = Color.White, fontSize = 16.sp)
                    Text(
                        "Schedule-based operation",
                        color = Color(0xFF9AB3C8),
                        fontSize = 12.sp
                    )
                }
                Spacer(Modifier.weight(1f))

                Switch(
                    checked = state.autoMode,
                    onCheckedChange = {
                        viewModel.toggleAutoMode()
                        onTriggerAlert(it)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Receive alert if water fails to reach expected level",
            color = Color(0xFFCED4DA),
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@Composable
fun PumpInfoBox(title: String, value: String) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .background(Color(0xFF0D1B2A), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Text(title, color = Color.Gray, fontSize = 12.sp)
        Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun WaterBodyPreview() {
    SmartHomeTheme {
        val viewModel: WaterViewModel = viewModel()
        val state by viewModel.state.collectAsState()
        WaterBody(state = state, viewModel = viewModel)
    }
}
