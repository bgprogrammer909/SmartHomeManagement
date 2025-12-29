package com.example.smarthome.view


import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthome.repo.LoginRepoImpl
import com.example.smarthome.ui.theme.SmartHomeTheme
import com.example.smarthome.viewmodel.LoginViewModel
import com.example.smarthome.R


class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginBody()
        }
    }
}



@Composable
fun LoginBody() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity
    var showForgotDialog by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }
    val loginRepo = LoginRepoImpl()
    val loginViewModel= LoginViewModel(loginRepo)

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
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
                                if (email.isNotEmpty() && password.isNotEmpty()) {
                                    loginViewModel.login(email, password) { success, message ->
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                        if (success) {
                                            val intent = Intent(
                                                context, HomeDashboardActivity::class.java
                                            )

                                            context.startActivity(intent)
                                            activity?.finish()
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "Please enter both fields", Toast.LENGTH_SHORT).show()
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
                            Text("Log in", style = TextStyle(fontSize = 20.sp), fontWeight = FontWeight.Bold)
                        }

                        Text(
                            "Forget Password?",
                            color = Color.Gray,
                            style = TextStyle(fontSize = 16.sp),
                            textDecoration = TextDecoration.Underline,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .clickable {
                                    showForgotDialog = true
                                })
                    }
                }
            }
        }
    }
    if (showForgotDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showForgotDialog = false },
            title = {
                Text("Forget Password")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = forgotEmail,
                        onValueChange = { forgotEmail = it },
                        shape = RoundedCornerShape(18.dp),
                        label = { Text("Enter your email", color = Color.Black) },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Black,
                            focusedTextColor = Color.Black
                        ),

                            modifier = Modifier.fillMaxWidth()
                    )
                }
            },confirmButton = {
                Button(
                    onClick = {
                        loginViewModel.forgetPassword(forgotEmail) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            if (success) showForgotDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF32A7EE)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        "Send Reset Link",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { showForgotDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        "Cancel",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        )
    }
}





@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginBody()
}
