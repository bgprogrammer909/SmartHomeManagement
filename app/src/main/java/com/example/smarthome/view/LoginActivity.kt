package com.example.smarthome.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthome.R
import com.example.smarthome.util.CurrentUser
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : ComponentActivity() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginScreen()
        }
    }

    @Composable
    fun LoginScreen() {
        val context = LocalContext.current
        val activity = context as? Activity

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var visibility by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        var showForgotDialog by remember { mutableStateOf(false) }
        var forgotEmail by remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize()) {
            // Background image
            Image(
                painter = painterResource(R.drawable.computer),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                colorResource(R.color.semi),
                                colorResource(R.color.darker)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 150.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Sign in",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 35.sp
                )
                Text(
                    "Welcome to Smart Home",
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.Gray.copy(1f),
                    fontWeight = FontWeight.Bold
                )

                Card(
                    modifier = Modifier
                        .height(450.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 20.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B243A).copy(0.8f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                            .padding(top = 40.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Email field
                        Text(
                            "Email",
                            color = Color.White,
                            style = TextStyle(fontSize = 20.sp),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            shape = RoundedCornerShape(20.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Enter your Email", color = Color.Gray) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = colorResource(R.color.radial),
                                unfocusedContainerColor = colorResource(R.color.radial),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        // Password field
                        Text(
                            "Password",
                            color = Color.White,
                            style = TextStyle(fontSize = 20.sp),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            visualTransformation = if (!visibility) PasswordVisualTransformation() else VisualTransformation.None,
                            trailingIcon = {
                                IconButton(onClick = { visibility = !visibility }) {
                                    Icon(
                                        painter = if (visibility)
                                            painterResource(R.drawable.baseline_remove_red_eye_24)
                                        else
                                            painterResource(R.drawable.baseline_visibility_off_24),
                                        contentDescription = null,
                                        tint = Color(0xFF67A1EF)
                                    )
                                }
                            },
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("********", color = Color.Gray) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = colorResource(R.color.radial),
                                unfocusedContainerColor = colorResource(R.color.radial),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        // Login button
                        Button(
                            onClick = {
                                if (email.isBlank() || password.isBlank()) {
                                    Toast.makeText(context, "Enter email & password", Toast.LENGTH_SHORT).show()
                                } else {
                                    isLoading = true
                                    auth.signInWithEmailAndPassword(email, password)
                                        .addOnSuccessListener { result ->
                                            val uid = result.user?.uid
                                            if (uid != null) {
                                                CurrentUser.userId = uid
                                                context.startActivity(Intent(context, HomeDashboardActivity::class.java))
                                                activity?.finish()
                                            } else {
                                                Toast.makeText(context, "Login failed: UID not found", Toast.LENGTH_SHORT).show()
                                            }
                                            isLoading = false
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                            isLoading = false
                                        }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32A7EE)),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(horizontal = 15.dp)
                                .padding(top = 15.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Log in", style = TextStyle(fontSize = 20.sp), fontWeight = FontWeight.Bold)
                            }
                        }

                        // Forgot password link
                        Text(
                            "Forget Password?",
                            color = Color.Gray,
                            style = TextStyle(fontSize = 16.sp),
                            textDecoration = TextDecoration.Underline,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .clickable { showForgotDialog = true })
                    }
                }
            }
        }

        // Optional: Forgot password dialog UI (can be implemented similarly)
        if (showForgotDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showForgotDialog = false },
                title = { Text("Forgot Password") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = forgotEmail,
                            onValueChange = { forgotEmail = it },
                            label = { Text("Enter your email") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        auth.sendPasswordResetEmail(forgotEmail)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Reset link sent", Toast.LENGTH_SHORT).show()
                                    showForgotDialog = false
                                } else {
                                    Toast.makeText(context, "Failed to send reset link", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }) {
                        Text("Send Reset Link")
                    }
                },
                dismissButton = {
                    Button(onClick = { showForgotDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
