package com.example.smarthome.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthome.R
import com.example.smarthome.view.ui.theme.SmartHomeTheme

class WaterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartHomeTheme {
                WaterBody()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterBody() {
    // Create a mutable state to pass to the card
    val isPumpOn = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.outline_arrow_back_24),
                        contentDescription = null
                    )
                },
                title = { Text("Back") }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0B132B), // dark blue
                                Color(0xFF1C1C2E)  // darker shade
                            )
                    )
                )
        ) {
            PumpStatusCard(isPumpOn)
        }
    }
}

@Composable
fun PumpStatusCard(isPumpOn: MutableState<Boolean>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF112030))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Pump Status", color = Color.White.copy(alpha = 0.7f))
                    Text(
                        if (isPumpOn.value) "On" else "Off",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.baseline_water_drop_24),
                    contentDescription = null,
                    tint = Color.Cyan,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PumpInfoBox("Today's Usage", "0 L/min")
                PumpInfoBox("Flow Rate", "0 L/min")
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { isPumpOn.value = !isPumpOn.value },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2295F3)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(if (isPumpOn.value) "Turn Off" else "Turn On")
            }
        }
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

@Preview
@Composable
fun WaterBodyPreview() {
    SmartHomeTheme {
        WaterBody()
    }
}
