// AdminActivity.kt
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

    LaunchedEffect(Unit) { viewModel.fetchAllUsers() }

    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }
    var generatedId by remember { mutableStateOf("") }

    var showEditDialog by remember { mutableStateOf(false) }
    var editPassword by remember { mutableStateOf("") }
    var editUserId by remember { mutableStateOf("") }

    val filteredUsers = if (searchQuery.isBlank()) users else users.filter {
        it.id.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.generateNextUserId { nextId ->
                        generatedId = nextId
                        newPassword = ""
                        showAddDialog = true
                    }
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
                placeholder = { Text("Search", color = Color.Gray) },
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
                        id = user.id,
                        password = user.password,
                        isActive = user.isActive,
                        onToggle = { viewModel.toggleUserStatus(user.id) },
                        onEdit = {
                            editUserId = user.id
                            editPassword = user.password
                            showEditDialog = true
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
                    Text("User ID = $generatedId", color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Password") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPassword.isNotBlank()) {
                            viewModel.addUser(AdminModel(id = generatedId, password = newPassword))
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

    // Edit User Dialog
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit User Password") },
            text = {
                Column {
                    Text("User ID = $editUserId", color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editPassword,
                        onValueChange = { editPassword = it },
                        label = { Text("Password") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editPassword.isNotBlank()) {
                            viewModel.updateUserPassword(editUserId, editPassword)
                            showEditDialog = false
                        }
                    }
                ) { Text("Save") }
            },
            dismissButton = {
                Button(onClick = { showEditDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun UserRow(
    id: String,
    password: String,
    isActive: Boolean,
    onToggle: () -> Unit,
    onEdit: () -> Unit
) {
    var switchState by remember { mutableStateOf(isActive) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .background(
                color = if (switchState) Color.DarkGray else Color.Black,
                shape = RoundedCornerShape(12.dp)
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

        Text(
            text = "ID: $id",
            color = Color.White,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = switchState,
            onCheckedChange = {
                switchState = it
                onToggle()
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Green,
                uncheckedThumbColor = Color.Red,
                checkedTrackColor = Color.LightGray,
                uncheckedTrackColor = Color.DarkGray
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onEdit) {
            Icon(
                painter = painterResource(R.drawable.baseline_edit_24),
                contentDescription = "Edit Password",
                tint = Color.White
            )
        }
    }
}
