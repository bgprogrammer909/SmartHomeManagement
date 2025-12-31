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
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : ComponentActivity() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { LoginScreen() }
    }

    @Composable
    fun LoginScreen() {
        val context = LocalContext.current
        val activity = context as? Activity

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        var showForgotDialog by remember { mutableStateOf(false) }
        var forgotEmail by remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize()) {

            Image(
                painter = painterResource(R.drawable.computer),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(
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

                Text("Sign in", color = Color.White, fontSize = 35.sp, fontWeight = FontWeight.Bold)
                Text("Welcome to Smart Home", color = Color.Gray, fontWeight = FontWeight.Bold)

                Card(
                    modifier = Modifier
                        .height(450.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 20.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B243A).copy(0.8f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 40.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Text("Email", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("Enter your Email", color = Color.Gray) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = colorResource(R.color.radial),
                                unfocusedContainerColor = colorResource(R.color.radial),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )

                        Text("Password", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("********", color = Color.Gray) },
                            visualTransformation = if (isPasswordVisible)
                                VisualTransformation.None
                            else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        painter = painterResource(
                                            if (isPasswordVisible)
                                                R.drawable.baseline_remove_red_eye_24
                                            else
                                                R.drawable.baseline_visibility_off_24
                                        ),
                                        contentDescription = null,
                                        tint = Color(0xFF67A1EF)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = colorResource(R.color.radial),
                                unfocusedContainerColor = colorResource(R.color.radial),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )

                        Button(
                            onClick = {
                                if (email.isBlank() || password.isBlank()) {
                                    Toast.makeText(context, "Enter email & password", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                isLoading = true

                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnSuccessListener { result ->
                                        val uid = result.user?.uid ?: return@addOnSuccessListener

                                        FirebaseDatabase.getInstance()
                                            .getReference("users")
                                            .child(uid)
                                            .child("isActive")
                                            .get()
                                            .addOnSuccessListener { snapshot ->
                                                val isActive = snapshot.getValue(Boolean::class.java) ?: false

                                                if (!isActive) {
                                                    auth.signOut()
                                                    Toast.makeText(
                                                        context,
                                                        "Subscription expired. Please contact admin.",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    isLoading = false
                                                    return@addOnSuccessListener
                                                }


                                                CurrentUser.userId = uid
                                                context.startActivity(
                                                    Intent(context, HomeDashboardActivity::class.java)
                                                )
                                                activity?.finish()
                                                isLoading = false
                                            }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                                        isLoading = false
                                    }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(top = 15.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32A7EE))
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Log in", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            }
                        }

                        Text(
                            "Forget Password?",
                            color = Color.Gray,
                            textDecoration = TextDecoration.Underline,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showForgotDialog = true }
                        )
                    }
                }
            }
        }

        if (showForgotDialog) {
            AlertDialog(
                onDismissRequest = { showForgotDialog = false },
                title = { Text("Forgot Password") },
                text = {
                    OutlinedTextField(
                        value = forgotEmail,
                        onValueChange = { forgotEmail = it },
                        label = { Text("Enter your email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        auth.sendPasswordResetEmail(forgotEmail)
                        Toast.makeText(context, "Reset link sent", Toast.LENGTH_SHORT).show()
                        showForgotDialog = false
                    }) { Text("Send Reset Link") }
                },
                dismissButton = {
                    Button(onClick = { showForgotDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}
