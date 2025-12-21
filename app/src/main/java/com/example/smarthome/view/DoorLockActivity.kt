package com.example.smarthome.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.smarthome.R
import com.example.smarthome.ui.theme.Orange
import com.example.smarthome.ui.theme.green3

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
fun DoorBody() {
    var isMainLocked by remember { mutableStateOf(true) }
    var isHomeLocked by remember { mutableStateOf(true) }

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize()
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
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_arrow_back_24),
                    contentDescription = null,
                    tint = Color.Gray.copy(0.8f),
                    modifier = Modifier.size(25.dp)
                )
                Text(
                    "Back",
                    style = TextStyle(color = Color.Gray.copy(0.8f)),
                    fontSize = 15.sp,
                    modifier = Modifier.padding(5.dp)
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 15.dp).padding(vertical = 8.dp)
            ) {
                Text(
                    "Door Lock",
                    style = TextStyle(color = Color.White),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                "Secure your home Entrance",
                style = TextStyle(color = Color.Gray.copy(0.8f)),
                modifier = Modifier.padding(horizontal = 15.dp)
            )

            Card(
                modifier = Modifier.height(210.dp)
                    .fillMaxWidth()
                    .padding(10.dp)
                    .border(
                        width = 1.2.dp,
                        color = Color(0xFF175F86),
                        shape = RoundedCornerShape(18.dp)
                    ),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(green3.copy(0.7f)),
                ) {


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)
                            .padding(top = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Main Entrance",
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        color =  if(isMainLocked) Color(0xFF1F6C41)
                                        else
                                            Orange,
                                        shape = RoundedCornerShape(15.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = if (isMainLocked)
                                        painterResource(R.drawable.outline_lock_24)
                                    else
                                        painterResource(R.drawable.baseline_lock_open_24),
                                    contentDescription = null,
                                    tint = if (isMainLocked) Color(0xFF47F37B)
                                    else Color.White,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                        Text( if (isMainLocked)
                            "Locked" else "Unlocked",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Button(
                            onClick = {
                                isMainLocked=!isMainLocked
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isMainLocked)Orange
                                else Color(0xFF1F6C41)
                            ),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 15.dp)
                        ) {
                            Text( if (isMainLocked)
                                "Unlock" else "Lock", style = TextStyle(fontSize = 18.sp),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            }
            Card(
                modifier = Modifier.height(210.dp)
                    .fillMaxWidth()
                    .padding(10.dp)
                    .border(
                        width = 1.2.dp,
                        color = Color(0xFF175F86),
                        shape = RoundedCornerShape(18.dp)
                    ),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(green3.copy(0.7f)),
                ) {


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)
                            .padding(top = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Home Door",
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        color = if(isHomeLocked) Color(0xFF1F6C41)
                                        else Orange,
                                        shape = RoundedCornerShape(15.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = if(isHomeLocked)
                                        painterResource(R.drawable.outline_lock_24)
                                    else
                                        painterResource(R.drawable.baseline_lock_open_24),
                                    contentDescription = null,
                                    tint = if(isHomeLocked) Color(0xFF47F37B)
                                    else Color.White,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                        Text( if(isHomeLocked)
                            "Locked" else "Unlocked",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Button(
                            onClick = {
                                isHomeLocked=!isHomeLocked
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isHomeLocked)Orange else
                                    Color(0xFF1F6C41)

                            ),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 15.dp)
                        ) {
                            Text( if(isHomeLocked)
                                "Unlock" else "Lock", style = TextStyle(fontSize = 18.sp),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 15.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .height(130.dp)
                        .weight(1f)
                        .padding(10.dp)
                        .border(
                            width = 1.2.dp,
                            color = Color(0xFF29354E),
                            shape = RoundedCornerShape(18.dp)
                        ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF1C263C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_lock_24),
                                contentDescription = null,
                                tint = Color(0xFFCE4A5A),
                                modifier = Modifier.size(38.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    isMainLocked=true
                                    isHomeLocked=true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Orange
                                ),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Text("Lock All")
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .height(130.dp)
                        .weight(1f)
                        .padding(8.dp)
                        .border(
                            width = 1.2.dp,
                            color = Color(0xFF29354E),
                            shape = RoundedCornerShape(18.dp)
                        ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF1C263C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_lock_open_24),
                                contentDescription = null,
                                tint = Color(0xFFCE4A5A),
                                modifier = Modifier.size(38.dp)
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Button(
                                onClick = {
                                    isMainLocked=false
                                    isHomeLocked=false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Orange),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Text("Unlock All")
                            }
                        }
                    }
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