package com.example.smarthome.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope.weight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smarthome.R
import kotlin.ranges.rangeTo

// ===========================================================
// Main Activity
// ===========================================================
class LightActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable full-screen layout
        setContent {
            LightsScreen() // Set main composable
        }
    }
}

// ===========================================================
// Main Composable: Lights Screen
// ===========================================================
@Composable
fun LightsScreen() {
    val ctx = LocalContext.current // Context for Toast messages

    // -----------------------------
    // Background Gradient
    // -----------------------------
    val bgGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF0D1B2A), Color(0xFF0A1320))
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient),
        containerColor = Color.Transparent
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // -----------------------------
            // Top Navigation Row
            // -----------------------------
            TopNavigation()

            Spacer(modifier = Modifier.height(22.dp))

            // -----------------------------
            // Screen Title and Subtitle
            // -----------------------------
            Text(
                "Lights Control",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                "Manage all your smart lights",
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(22.dp))

            // -----------------------------
            // Top Status Card (Total Lights)
            // -----------------------------
            TopStatusCard(
                activeCount = 2,
                onTurnOffAll = { Toast.makeText(ctx, "Turned off", Toast.LENGTH_SHORT).show() }
            )

            Spacer(modifier = Modifier.height(25.dp))

            // -----------------------------
            // Room Controls Section
            // -----------------------------
            Text(
                "Room Controls",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LightControlCard(label = "Light 1")
            Spacer(modifier = Modifier.height(16.dp))
            LightControlCard(label = "Light 2")
        }
    }
}

// ===========================================================
// Top Navigation Row
// ===========================================================
@Composable
fun TopNavigation() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text("Back", color = Color.White.copy(alpha = 0.7f))
    }
}

// ===========================================================
// Top Status Card Composable
// ===========================================================
@Composable
fun TopStatusCard(activeCount: Int, onTurnOffAll: () -> Unit) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xFF1D233A)),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // -----------------------------
            // Lights Count and Icon
            // -----------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text("Total Lights", color = Color.White.copy(alpha = 0.6f))
                    Text(
                        "$activeCount On",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                        .background(Color(0x33FBC02D)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_lightbulb_24),
                        contentDescription = null,
                        tint = Color(0xFFFBC02D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // -----------------------------
            // Turn All Off Button
            // -----------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFFFFC107), Color(0xFFFFA000))
                        )
                    )
                    .clickable { onTurnOffAll() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Turn All Off",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

// ===========================================================
// Individual Light Control Card Composable
// ===========================================================
@Composable
fun LightControlCard(label: String) {

    // State variables for switch and brightness
    var switchState by remember { mutableStateOf(true) }
    var lightPercent by remember { mutableStateOf(50f) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xFF1A2036)),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            // -----------------------------
            // Row: Icon, Label, Switch
            // -----------------------------
            Row(verticalAlignment = Alignment.CenterVertically) {

                // Lightbulb icon
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                        .background(Color(0x33FBC02D)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_lightbulb_24),
                        contentDescription = null,
                        tint = Color(0xFFFBC02D)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Label and brightness
                Column {
                    Text(
                        label,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        "${lightPercent.toInt()}%",
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // Pushes switch to the right

                // On/Off switch
                Switch(
                    checked = switchState,
                    onCheckedChange = { switchState = it }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // -----------------------------
            // Row: Brightness Slider
            // -----------------------------
            Row(verticalAlignment = Alignment.CenterVertically) {

                // Brightness icon
                Icon(
                    painter = painterResource(id = R.drawable.outline_brightness_5_24),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )

                // Slider
                Slider(
                    value = lightPercent,
                    onValueChange = { newVal -> lightPercent = newVal },
                    valueRange = 0f..100f,
                    steps = 98,
                    modifier = Modifier.weight(1f)
                )

                // Display current brightness
                Text(
                    "${lightPercent.toInt()}%",
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// ===========================================================
// Preview Composable
// ===========================================================
@Preview(showBackground = true)
@Composable
fun LightsScreenPreview() {
    LightsScreen()
}
