package com.example.smarthome.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.R
import com.example.smarthome.model.AdminModel
import com.example.smarthome.viewmodel.AdminViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: AdminViewModel = viewModel()
            AdminScreen(vm)
        }
    }
}

@Composable
fun AdminScreen(viewModel: AdminViewModel) {
    val users by viewModel.users.collectAsState()
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var newEmail by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    var showEditPasswordDialog by remember { mutableStateOf(false) }
    var editingUserId by remember { mutableStateOf("") }
    var editingUserEmail by remember { mutableStateOf("") }
    var editingPassword by remember { mutableStateOf("") }

    val filteredUsers = if (searchQuery.isBlank()) users else users.filter {
        it.id.contains(searchQuery, true) || it.email.contains(searchQuery, true)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddDialog = true
                    newEmail = ""
                    newPassword = ""
                },
                containerColor = Color.Blue
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_edit_24),
                    contentDescription = "Add User",
                    tint = Color.White
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() }
        ) {
            Text(
                text = "Admin",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by ID or Email", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_search_24),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .fillMaxWidth()
                    .background(Color.DarkGray, RoundedCornerShape(18.dp)),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
                items(filteredUsers) { user ->
                    UserRow(
                        user = user,
                        onToggle = { viewModel.updateUserStatus(user.id, it) },
                        onEditPassword = {
                            editingUserId = user.id
                            editingUserEmail = user.email
                            editingPassword = ""
                            showEditPasswordDialog = true
                        }
                    )
                }
            }
        }
    }

    // Add User Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New User") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newEmail,
                        onValueChange = { newEmail = it },
                        label = { Text("Email") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Password") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newEmail.isNotBlank() && newPassword.isNotBlank()) {
                            viewModel.addUser(newEmail, newPassword)
                            showAddDialog = false
                        }
                    }
                ) { Text("Add") }
            },
            dismissButton = {
                Button(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Edit Password Dialog
    if (showEditPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showEditPasswordDialog = false },
            title = { Text("Edit Password") },
            text = {
                OutlinedTextField(
                    value = editingPassword,
                    onValueChange = { editingPassword = it },
                    label = { Text("New Password") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editingPassword.isNotBlank()) {
                            viewModel.updateModule(editingUserId, "password", editingPassword)
                            showEditPasswordDialog = false
                            scope.launch {
                                snackbarHostState.showSnackbar("Password updated for $editingUserEmail")
                            }
                        }
                    }
                ) { Text("Update") }
            },
            dismissButton = {
                Button(onClick = { showEditPasswordDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun UserRow(
    user: AdminModel,
    onToggle: (Boolean) -> Unit,
    onEditPassword: () -> Unit
) {
    var isActive by remember { mutableStateOf(user.isActive) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .background(
                if (isActive) Color.DarkGray else Color.Black,
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_person_24),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "ID: ${user.id}",
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Email: ${user.email}",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        Switch(
            checked = isActive,
            onCheckedChange = { checked ->
                isActive = checked
                onToggle(checked)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Green,
                uncheckedThumbColor = Color.Red,
                checkedTrackColor = Color.LightGray,
                uncheckedTrackColor = Color.DarkGray
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Small Edit Password Button
        Button(
            onClick = onEditPassword,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text("Edit", fontSize = 12.sp)
        }
    }
}
