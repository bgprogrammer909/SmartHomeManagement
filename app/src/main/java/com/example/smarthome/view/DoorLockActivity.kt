package com.example.smarthome.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.R
import com.example.smarthome.ui.theme.Orange
import com.example.smarthome.ui.theme.green3
import com.example.smarthome.viewmodel.DoorViewModel

class DoorlockActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoorBody()
        }
    }
}

@Composable
fun DoorBody(viewModel: DoorViewModel = viewModel()) {
    val isMainLocked by viewModel.mainDoor.collectAsState()
    val isHomeLocked by viewModel.homeDoor.collectAsState()

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
            // Top Row
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
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    "Back",
                    color = Color.Gray.copy(0.8f),
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }

            // Title
            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                Text(
                    "Door Lock",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Secure your home Entrance",
                    color = Color.Gray.copy(0.8f),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Entrance Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 8.dp)
                    .height(160.dp)
                    .border(
                        width = 1.2.dp,
                        color = Color(0xFF175F86),
                        shape = RoundedCornerShape(18.dp)
                    ),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1F2B44).copy(alpha = 0.7f)) // Semi-transparent card background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Main Entrance", color = Color.White, fontSize = 18.sp)
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = if (isMainLocked) Color(0xFF1F6C41) else Orange,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = if (isMainLocked)
                                        painterResource(R.drawable.outline_lock_24)
                                    else
                                        painterResource(R.drawable.baseline_lock_open_24),
                                    contentDescription = null,
                                    tint = if (isMainLocked) Color(0xFF47F37B) else Color.White,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                        Text(
                            if (isMainLocked) "Locked" else "Unlocked",
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { viewModel.toggleMainDoor() },
                            colors = ButtonDefaults.buttonColors(containerColor = Orange),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.fillMaxWidth().height(45.dp)
                        ) {
                            Text(if (isMainLocked) "Unlock" else "Lock", color = Color.White)
                        }
                    }
                }
            }

            // Home Door Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 8.dp)
                    .height(160.dp)
                    .border(
                        width = 1.2.dp,
                        color = Color(0xFF175F86),
                        shape = RoundedCornerShape(18.dp)
                    ),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1F2B44).copy(alpha = 0.7f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Home Door", color = Color.White, fontSize = 18.sp)
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = if (isHomeLocked) Color(0xFF1F6C41) else Orange,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = if (isHomeLocked)
                                        painterResource(R.drawable.outline_lock_24)
                                    else
                                        painterResource(R.drawable.baseline_lock_open_24),
                                    contentDescription = null,
                                    tint = if (isHomeLocked) Color(0xFF47F37B) else Color.White,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                        Text(
                            if (isHomeLocked) "Locked" else "Unlocked",
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { viewModel.toggleHomeDoor() },
                            colors = ButtonDefaults.buttonColors(containerColor = Orange),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.fillMaxWidth().height(45.dp)
                        ) {
                            Text(if (isHomeLocked) "Unlock" else "Lock", color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Lock All / Unlock All Row
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Button(
                    onClick = { viewModel.lockAll() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF29354E)),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.weight(1f).height(50.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_lock_24),
                        contentDescription = null,
                        tint = Orange,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("Lock All", color = Orange)
                }
                Button(
                    onClick = { viewModel.unlockAll() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF29354E)),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.weight(1f).height(50.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_lock_open_24),
                        contentDescription = null,
                        tint = Orange,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("Unlock All", color = Orange)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoorPreview() {
    DoorBody()
}
