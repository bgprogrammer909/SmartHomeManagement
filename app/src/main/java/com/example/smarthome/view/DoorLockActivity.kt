package com.example.smarthome.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.R
import com.example.smarthome.ui.theme.Orange
import com.example.smarthome.viewmodel.DoorViewModel
import com.example.smarthome.viewmodel.DoorViewModelFactory

class DoorLockActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userId = intent.getStringExtra("USER_ID") ?: return

        setContent {
            val viewModel: DoorViewModel = viewModel(
                factory = DoorViewModelFactory(userId)
            )
            DoorScreen(viewModel)
        }
    }
}

@Composable
fun DoorScreen(viewModel: DoorViewModel) {

    // ✅ CORRECT: collect DoorModel
    val doors by viewModel.doors.collectAsState()

    val context = LocalContext.current
    val activity = context as ComponentActivity

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.linearGradient(
                        listOf(
                            colorResource(R.color.radial),
                            colorResource(R.color.radial),
                            colorResource(R.color.Radial)
                        ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
        ) {

            // Back
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_arrow_back_24),
                    contentDescription = null,
                    tint = Color.Gray.copy(0.8f),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { activity.finish() }
                )
                Text(
                    "Back",
                    color = Color.Gray.copy(0.8f),
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .clickable { activity.finish() }
                )
            }

            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                Text(
                    "Door Lock",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Secure your home entrance",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            DoorCard(
                title = "Main Door",
                locked = doors.mainDoorLocked,
                onToggle = { viewModel.toggleMainDoor() }
            )

            DoorCard(
                title = "Home Door",
                locked = doors.homeDoorLocked,
                onToggle = { viewModel.toggleHomeDoor() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Button(
                    onClick = { viewModel.lockAll() },
                    modifier = Modifier.weight(1f) .testTag("LoclAllButton"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF29354E))
                ) {
                    Text("Lock All", color = Orange)
                }

                Button(
                    onClick = { viewModel.unlockAll() },
                    modifier = Modifier.weight(1f) .testTag("unlockAllButton"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF29354E))
                ) {
                    Text("Unlock All", color = Orange)
                }
            }
        }
    }
}

@Composable
fun DoorCard(
    title: String,
    locked: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 8.dp)
            .height(150.dp)
            .border(1.dp, Color(0xFF175F86), RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F2B44)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = Color.White, fontSize = 18.sp)

            Text(
                if (locked) "Locked" else "Unlocked",
                modifier = Modifier.testTag("$title-status"),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Button(
                onClick = onToggle,
                colors = ButtonDefaults.buttonColors(containerColor = Orange),
                modifier = Modifier.fillMaxWidth() .testTag("$title-toggle")

            ) {
                Text(if (locked) "Unlock" else "Lock")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoorPreview() {}
