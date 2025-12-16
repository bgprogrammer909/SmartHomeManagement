package com.example.smarthome.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthome.R

class UserEditProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EditBody()
        }
    }
}

@Composable
fun EditBody() {
    Scaffold { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            /* 🔹 TOP GRADIENT BACKGROUND */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                colorResource(R.color.radial),
                                colorResource(R.color.radial),
                                colorResource(R.color.Radial)
                            ),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 228.dp)
                    .background(colorResource(R.color.field))
            )

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_arrow_back_24),
                        contentDescription = "Back",
                        tint = Color.White
                    )

                    Text(
                        text = "Edit Profile",
                        color = Color.White,
                        fontSize = 16.sp
                    )

                    Icon(
                        painter = painterResource(R.drawable.baseline_share_24),
                        contentDescription = "Share",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(117.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    HorizontalDivider(
                        color = Color.Black,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(Color.Gray, CircleShape)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_person_24),
                            contentDescription = "Profile",
                            modifier = Modifier.size(64.dp),
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
                Text("Username", color = Color.White, fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    shape = RoundedCornerShape(18.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                    placeholder = {
                        Text("Username", color = Color.Gray, fontWeight = FontWeight.Normal)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor =colorResource(R.color.field),
                        unfocusedContainerColor = colorResource(R.color.radial),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text("Current Password", color = Color.White, fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    shape = RoundedCornerShape(18.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                    placeholder = {
                        Text("••••••••", color = Color.Gray, fontWeight = FontWeight.ExtraBold)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor =colorResource(R.color.field),
                        unfocusedContainerColor = colorResource(R.color.radial),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text("New Password", color = Color.White, fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    shape = RoundedCornerShape(18.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                    placeholder = {
                        Text("••••••••", color = Color.Gray, fontWeight = FontWeight.ExtraBold)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor =colorResource(R.color.field),
                        unfocusedContainerColor = colorResource(R.color.radial),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults
                        .buttonColors(Color(0xFF67A1EF)),
                    elevation = ButtonDefaults.buttonElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Update", fontSize = 18.sp, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditPreview() {
    EditBody()
}
