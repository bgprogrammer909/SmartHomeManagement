package com.example.smarthome.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthome.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProfileBody(
                onBackClick = { finish() },
                onEditClick = {
                    startActivity(
                        Intent(
                            this@ProfileActivity,
                            UserEditProfileActivity::class.java
                        )
                    )
                },
                onSettingsClick = {},
                onLogoutClick = {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(
                        Intent(
                            this@ProfileActivity,
                            LoginActivity::class.java
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun ProfileBody(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current
    var isLoggingOut by remember { mutableStateOf(false) }

    // 🔹 Firebase user
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email ?: "No email"
    val username = user?.displayName
        ?: email.substringBefore("@")
            .replaceFirstChar { it.uppercase() }

    // Logout delay
    LaunchedEffect(isLoggingOut) {
        if (isLoggingOut) {
            Toast.makeText(context, "Logging out", Toast.LENGTH_SHORT).show()
            delay(1200)
            onLogoutClick()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E293B))
    ) {
        // Top background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color(0xFF0F172A))
        )

        Column(modifier = Modifier.fillMaxSize()) {

            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { onBackClick() }
                )

                Text(
                    text = "My Profile",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.size(22.dp))
            }

            Spacer(modifier = Modifier.height(90.dp))

            // Profile Avatar
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color.Gray, CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_person_24),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Dynamic Name & Email
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = username,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = email,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Options + Logout
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                ProfileOptionBox("Edit Profile", onEditClick)

                Spacer(modifier = Modifier.height(16.dp))

                ProfileOptionBox("Settings", onSettingsClick)

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = { isLoggingOut = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF4444)
                    )
                ) {
                    if (isLoggingOut) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Logout",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileOptionBox(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF0F172A), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.weight(1f),
            fontSize = 15.sp
        )
        Icon(
            painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileBody(
        onBackClick = {},
        onEditClick = {},
        onSettingsClick = {},
        onLogoutClick = {}
    )
}
