package com.example.smarthome.view

import android.content.Context
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarthome.R
import com.example.smarthome.repo.PLightRepoImpl
import com.example.smarthome.util.CurrentUser
import com.example.smarthome.viewmodel.PLightsViewModel
import com.example.smarthome.viewmodel.PLightsViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeDashboardActivity : ComponentActivity() {

    private var isActiveListener: ValueEventListener? = null
    private lateinit var isActiveRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            finish()
            return
        }

        //REAL-TIME SUBSCRIPTION CHECK
        isActiveRef = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(uid)
            .child("isActive")

        isActiveListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isActive = snapshot.getValue(Boolean::class.java) ?: true

                if (!isActive) {
                    FirebaseAuth.getInstance().signOut()

                    Toast.makeText(
                        this@HomeDashboardActivity,
                        "Your subscription has expired. Please contact admin.",
                        Toast.LENGTH_LONG
                    ).show()

                    startActivity(
                        Intent(this@HomeDashboardActivity, LoginActivity::class.java)
                            .addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK or
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                            )
                    )
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        isActiveRef.addValueEventListener(isActiveListener!!)

        setContent {
            HomeDashboardBody()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isActiveListener?.let {
            isActiveRef.removeEventListener(it)
        }
    }
}

@Composable
fun HomeDashboardBody() {
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedIndex) { selectedIndex = it } },
        containerColor = Color(0xFF0B1225)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedIndex) {
                0 -> DashboardScreen()
                1 -> ScreenBox("Analytics")
                2 -> ScreenBox("Security")
                3 -> ScreenBox("Profile Page")
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    val navItems = listOf(
        NavItem(R.drawable.baseline_home_24, "Dashboard"),
        NavItem(R.drawable.baseline_query_stats_24, "Analytics"),
        NavItem(R.drawable.baseline_security_24, "Security"),
        NavItem(R.drawable.baseline_person_24, "Profile")
    )

    NavigationBar(
        containerColor = Color(0xFF0D152F),
        tonalElevation = 4.dp
    ) {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(painterResource(item.icon), contentDescription = item.label) },
                label = { Text(item.label, fontSize = 12.sp) },
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                alwaysShowLabel = true
            )
        }
    }
}

data class NavItem(val icon: Int, val label: String)

@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    val userId = CurrentUser.userId ?: return

    val lightsViewModel: PLightsViewModel = viewModel(
        factory = PLightsViewModelFactory(
            repo = PLightRepoImpl(),
            userId = userId
        )
    )

    val lightsState by lightsViewModel.lights.collectAsState()
    val activeLightsCount =
        listOf(lightsState.light1On, lightsState.light2On).count { it }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1225))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 90.dp)
        ) {
            HeaderSection()
            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                DeviceRow(
                    context,
                    CardData(
                        "Light",
                        "$activeLightsCount On",
                        R.drawable.outline_lightbulb_24,
                        Color.Yellow,
                        PLightActivity::class.java
                    ),
                    CardData(
                        "Water",
                        "Pump Off",
                        R.drawable.baseline_water_drop_24,
                        Color.Cyan,
                        null
                    )
                )

                DeviceRow(
                    context,
                    CardData(
                        "Fan",
                        "24°C",
                        R.drawable.baseline_air_24,
                        Color(0xFF1FB7FF),
                        ClimateControlActivity::class.java
                    ),
                    CardData(
                        "Door",
                        "Main Entrance",
                        R.drawable.baseline_sensor_door_24,
                        Color(0xFF4CAF50),
                        null
                    )
                )

                DeviceRow(
                    context,
                    CardData(
                        "Security",
                        "Away Mode",
                        R.drawable.baseline_security_24,
                        Color(0xFFFF9800),
                        null
                    ),
                    CardData(
                        "Analytics",
                        "120 kWh",
                        R.drawable.baseline_query_stats_24,
                        Color(0xFF7A4FFF),
                        null
                    )
                )
            }
        }
    }
}

data class CardData(
    val title: String,
    val status: String,
    val icon: Int,
    val color: Color,
    val activity: Class<*>?
)

@Composable
fun DeviceRow(context: Context, card1: CardData, card2: CardData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DeviceCard(Modifier.weight(1f), card1, context)
        DeviceCard(Modifier.weight(1f), card2, context)
    }
}

@Composable
fun DeviceCard(modifier: Modifier, card: CardData, context: Context) {
    Column(
        modifier = modifier
            .height(135.dp)
            .background(Color(0xFF111A32), RoundedCornerShape(20.dp))
            .let {
                if (card.activity != null)
                    it.clickable {
                        context.startActivity(
                            Intent(context, card.activity).apply {
                                putExtra("USER_ID", CurrentUser.userId)
                            }
                        )
                    } else it
            }
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(card.icon),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            colorFilter = ColorFilter.tint(card.color)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(card.title, color = Color.White, fontSize = 16.sp)
        Text(card.status, color = Color.White.copy(0.6f), fontSize = 13.sp)
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Welcome Home,", color = Color.White.copy(0.7f), fontSize = 15.sp)
            Text("Alex", color = Color.White, fontSize = 20.sp)
        }

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_person_24),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
fun ScreenBox(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111A32)),
        contentAlignment = Alignment.Center
    ) {
        Text(title, color = Color.White, fontSize = 20.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboard() {
    DashboardScreen()
}
