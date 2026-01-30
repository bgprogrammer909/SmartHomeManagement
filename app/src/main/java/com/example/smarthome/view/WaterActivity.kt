package com.example.smarthome.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.R
import com.example.smarthome.viewmodel.WaterViewModel
import com.example.smarthome.viewmodel.WaterViewModelFactory
import com.example.smarthome.viewmodel.SecurityViewModel
import com.example.smarthome.viewmodel.SecurityViewModelFactory


class WaterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userId = intent.getStringExtra("USER_ID") ?: return

        setContent {
            val viewModel: WaterViewModel = viewModel(factory = WaterViewModelFactory(userId))
            val securityViewModel: SecurityViewModel = viewModel(
                factory = SecurityViewModelFactory(userId)
            )

            WaterControlScreen(viewModel, securityViewModel)

        }
    }
}

fun getWaterLevelLabel(level: Double): String {
    return when {
        level < 200.0 -> "LOW"
        level in 200.0..349.9 -> "MEDIUM"
        level in 350.0..419.9 -> "HIGH"
        level >= 420.0 -> "FULL"
        else -> "UNKNOWN"
    }
}

@Composable
fun WaterControlScreen(
    viewModel: WaterViewModel,
    securityViewModel: SecurityViewModel
) {
    val showMotionAlert by securityViewModel.showMotionAlert
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    val snackbarHostState = remember { SnackbarHostState() }
    var showAutoModeAlert by remember { mutableStateOf(false) }
    var showExitBlockDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showAutoModeAlert) {
        if (showAutoModeAlert) {
            snackbarHostState.showSnackbar("Water level monitoring enabled")
            showAutoModeAlert = false
        }
    }

    val bg = Brush.verticalGradient(
        listOf(Color(0xFF0B132B), Color(0xFF1C1C2E))
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
                .padding(20.dp)
                .statusBarsPadding()
        ) {

            // Top Back Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        // Check if water is on and auto mode is off
                        if (state.isPumpOn && !state.autoMode) {
                            showExitBlockDialog = true
                        } else {
                            activity?.finish()
                        }
                    }
                    .testTag("backButton")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color(0xFF9DB9D0),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Back",
                    color = Color(0xFF9DB9D0)
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

            // Main Pump Status Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF113A5B), Color(0xFF0E2B4C))
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Pump Status",
                                color = Color(0xFFBFD9E6),
                                fontSize = 14.sp
                            )
                            Text(
                                if (state.isPumpOn) "RUNNING" else "STOPPED",
                                modifier = Modifier.testTag("pumpStatusText"),
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Icon(
                            painter = painterResource(R.drawable.baseline_water_drop_24),
                            contentDescription = null,
                            tint = if (state.isPumpOn) Color(0xFF2196F3) else Color(0xFF607D8B),
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PumpInfoBox(
                            title = "Water Level",
                            value = getWaterLevelLabel(state.todayUsage),
                            icon = R.drawable.baseline_water_drop_24
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (!state.autoMode) {
                                viewModel.togglePump()
                            } else {
                                Toast.makeText(context,"Automatic mode is on", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("pumpToggleButton"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.isPumpOn) Color(0xFFE53935) else Color(0xFF2196F3)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            if (state.isPumpOn) "Turn Off Pump" else "Turn On Pump",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Auto Mode Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0E2433))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_calendar_today_24),
                        contentDescription = null,
                        tint = Color(0xFF1FB7FF),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Auto Mode",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Schedule-based operation",
                            color = Color(0xFF9AB3C8),
                            fontSize = 13.sp
                        )
                    }
                    Switch(
                        checked = state.autoMode,
                        onCheckedChange = {
                            viewModel.setAutoMode(it)
                            if (it) showAutoModeAlert = true
                        },
                        modifier = Modifier.testTag("autoModeSwitch")
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Info Text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0E2433))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_edit_24),
                    contentDescription = null,
                    tint = Color(0xFF64B5F6),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Receive alert if water fails to reach expected level",
                    color = Color(0xFFCED4DA),
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            // Updated Energy Efficiency Card
            EnergyEfficiencyCard(autoMode = state.autoMode)
        }

        if (showMotionAlert) {
            AlertDialog(
                onDismissRequest = { },
                confirmButton = {
                    TextButton(onClick = { securityViewModel.dismissMotionAlert() }) {
                        Text("OK", color = Color(0xFF1FB7FF))
                    }
                },
                title = {
                    Text(
                        "⚠️ Motion Detected!",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        "Motion has been detected in your home. Please check your security cameras."
                    )
                },
                containerColor = Color(0xFF1C1C2E),
                titleContentColor = Color.White,
                textContentColor = Color(0xFF9AB3C8)
            )
        }

        if (showExitBlockDialog) {
            AlertDialog(
                onDismissRequest = { showExitBlockDialog = false },
                confirmButton = {
                    TextButton(onClick = { showExitBlockDialog = false }) {
                        Text("OK", color = Color(0xFF1FB7FF))
                    }
                },
                title = {
                    Text(
                        "⚠️ Pump Running",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        "The water pump is currently running. Please turn it off before exiting, or enable Auto Mode to allow the system to manage itself."
                    )
                },
                containerColor = Color(0xFF1C1C2E),
                titleContentColor = Color.White,
                textContentColor = Color(0xFF9AB3C8)
            )
        }

    }
}

@Composable
fun PumpInfoBox(title: String, value: String, icon: Int) {
    Box(
        modifier = Modifier
            .testTag("${title}_info")
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0D1B2A))
            .padding(14.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Color(0xFF64B5F6),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    title,
                    color = Color(0xFF9AB3C8),
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                value,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EnergyEfficiencyCard(autoMode: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("energyEfficiencyCard"),
        shape = RoundedCornerShape(18.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF0F3D2E), Color(0xFF0A2A22))
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_security_24),
                        contentDescription = null,
                        tint = Color(0xFF2EFFA3),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Energy Efficiency",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(8.dp))

                val message = if (autoMode) {
                    "Great! Auto Mode is ON, saving more energy. Always ensure there are no water leaks."
                } else {
                    "Using manual mode. Auto Mode can help save more energy. Always ensure there are no water leaks."
                }

                Text(
                    message,
                    color = Color(0xFFB7E8D8),
                    fontSize = 13.sp
                )
            }

        }
    }
}