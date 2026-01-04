package com.example.smarthome.view

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthome.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

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
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    var username by remember {
        mutableStateOf(
            user?.email
                ?.substringBefore("@")
                ?.replaceFirstChar { it.uppercase() }
                ?: ""
        )
    }
    var isLoading by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

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
                        tint = Color.White,
                        modifier = Modifier
                            .clickable {
                                if (context is ComponentActivity) {
                                    (context as ComponentActivity).finish()
                                }
                            }
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
                Text(
                    "Username", color = Color.White, fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { data ->
                        username = data
                    },
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
                        focusedContainerColor = colorResource(R.color.field),
                        unfocusedContainerColor = colorResource(R.color.radial),
                        focusedIndicatorColor = colorResource(R.color.border1),
                        unfocusedIndicatorColor = colorResource(R.color.border1),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Current Password", color = Color.White, fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                )
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { data ->
                        currentPassword = data
                    },
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
                        focusedContainerColor = colorResource(R.color.field),
                        unfocusedContainerColor = colorResource(R.color.radial),
                        focusedIndicatorColor = colorResource(R.color.border1),
                        unfocusedIndicatorColor = colorResource(R.color.border1),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "New Password", color = Color.White, fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { data ->
                        newPassword = data
                    },
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
                        focusedContainerColor = colorResource(R.color.field),
                        unfocusedContainerColor = colorResource(R.color.radial),
                        focusedIndicatorColor = colorResource(R.color.border1),
                        unfocusedIndicatorColor = colorResource(R.color.border1),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))

                // ---------------- BUTTON WITH LOADING ----------------
                Button(
                    onClick = {
                        if (currentPassword.isEmpty() || newPassword.isEmpty()) {
                            Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT)
                                .show()
                            return@Button
                        }

                        val firebaseUser = FirebaseAuth.getInstance().currentUser
                        if (firebaseUser == null || firebaseUser.email == null) {
                            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isLoading = true

                        val credential = EmailAuthProvider.getCredential(
                            firebaseUser.email!!,
                            currentPassword
                        )

                        firebaseUser.reauthenticate(credential)
                            .addOnSuccessListener {
                                firebaseUser.updatePassword(newPassword)
                                    .addOnSuccessListener {
                                        isLoading = false
                                        Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
                                        if (context is ComponentActivity) {
                                            context.finish()
                                        }
                                    }
                                    .addOnFailureListener {
                                        isLoading = false
                                        Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            .addOnFailureListener {
                                isLoading = false
                                Toast.makeText(context, "Current password is wrong", Toast.LENGTH_SHORT).show()
                            }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF67A1EF)),
                    elevation = ButtonDefaults.buttonElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(18.dp),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Update",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            modifier = Modifier.alpha(if (isLoading) 0f else 1f)
                        )
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
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
