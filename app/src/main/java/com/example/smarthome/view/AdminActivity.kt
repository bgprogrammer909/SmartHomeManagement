package com.example.smarthome.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

class AdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdminBody()

        }
    }
}
@Composable
fun AdminBody() {
    Scaffold { padding->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black)
        ){
            Row (
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp)
            ){
                Icon(
                    painter = painterResource(R.drawable.outline_arrow_back_24),
                    contentDescription = null,
                    modifier = Modifier, tint = Color.White
                )
                Text("Admin", style = TextStyle(Color.White), fontSize = 18.sp,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                leadingIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_search_24),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp), tint = Color.Gray

                        )
                    }
                },
                shape = RoundedCornerShape(18.dp),
                placeholder = {Text("Search", color = Color.White)},
                modifier = Modifier.padding(horizontal = 18.dp)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = colorResource(R.color.search),
                    focusedContainerColor = Color.White,
                    focusedTextColor = Color.Black

                )

            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue.copy(0.8f)
                ),
                shape =RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
                    .height(45.dp)
                    .padding(horizontal = 18.dp)
            ) {
                Text("Add User", color = Color.White, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn (modifier = Modifier.fillMaxSize()){
                item {
                    Users("Ethan Carter", "12345", true)
                }

                item {
                    Users("Olivia Bennett", "67890", true)
                }

                item {
                    Users("Noah Thompson", "24680", false)
                }

                item {
                    Users("Ava Harper", "13579", true)
                }
            }
        }
    }

}
@Composable
fun Users(
    name:String,
    id:String,
    isActive: Boolean
){
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically){
        Icon(
            painter = painterResource(R.drawable.baseline_person_24),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "ID: $id",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        Text(
            text = if (isActive) "• Active" else "• Inactive",
            color = if (isActive) Color.Green else Color.Red,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(R.drawable.baseline_edit_24),
            contentDescription = null,
            tint = Color.White)
    }
}

@Preview
@Composable
fun AdminPreview() {
AdminBody()
}