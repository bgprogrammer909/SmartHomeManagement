package com.example.smarthome.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.smarthome.R
import com.example.smarthome.model.*
import com.example.smarthome.util.CurrentUser
import com.google.firebase.database.*

class HomeDashboardActivity : ComponentActivity() {

    private val db = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeDashboardBody()
        }
    }

    @Composable
    fun HomeDashboardBody() {
        var selectedIndex by remember { mutableStateOf(0) }

        var lights by remember { mutableStateOf(listOf(LightModel(), LightModel())) }
        var fan by remember { mutableStateOf(ClimateModel()) }
        var door by remember { mutableStateOf(DoorModel()) }
        var water by remember { mutableStateOf(WaterModel()) }

        LaunchedEffect(Unit) {
            val uid = CurrentUser.userId ?: return@LaunchedEffect

            val lightsRef = db.child("users").child(uid).child("lights")
            lightsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) lightsRef.setValue(listOf(LightModel(), LightModel()))
                }
                override fun onCancelled(error: DatabaseError) {}
            })
            lightsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children.map { it.getValue(LightModel::class.java) ?: LightModel() }
                    lights = list
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            val fanRef = db.child("users").child(uid).child("fan")
            fanRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists() || snapshot.value !is Map<*, *>) fanRef.setValue(ClimateModel())
                }
                override fun onCancelled(error: DatabaseError) {}
            })
            fanRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    fan = if (snapshot.value is Map<*, *>) {
                        snapshot.getValue(ClimateModel::class.java) ?: ClimateModel()
                    } else {
                        fanRef.setValue(ClimateModel())
                        ClimateModel()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            val doorRef = db.child("users").child(uid).child("door")
            doorRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists() || snapshot.value !is Map<*, *>) doorRef.setValue(DoorModel())
                }
                override fun onCancelled(error: DatabaseError) {}
            })
            doorRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    door = if (snapshot.value is Map<*, *>) {
                        snapshot.getValue(DoorModel::class.java) ?: DoorModel()
                    } else {
                        doorRef.setValue(DoorModel())
                        DoorModel()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            val waterRef = db.child("users").child(uid).child("water")
            waterRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists() || snapshot.value !is Map<*, *>) waterRef.setValue(WaterModel())
                }
                override fun onCancelled(error: DatabaseError) {}
            })
            waterRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    water = if (snapshot.value is Map<*, *>) {
                        snapshot.getValue(WaterModel::class.java) ?: WaterModel()
                    } else {
                        waterRef.setValue(WaterModel())
                        WaterModel()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }

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
                    0 -> DashboardScreen(lights, fan, door, water)
                    1 -> EnergyAnalyticsActivityScreen()
                    2 -> SecurityScreen()
                    3 -> ProfileActivityScreen()
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
                    label = { Text(item.label, fontSize = 12.sp, color = Color.White) },
                    selected = selectedIndex == index,
                    onClick = { onItemSelected(index) },
                    alwaysShowLabel = true
                )
            }
        }
    }

    data class NavItem(val icon: Int, val label: String)

    @Composable
    fun DashboardScreen(lights: List<LightModel>, fan: ClimateModel, door: DoorModel, water: WaterModel) {
        val context = LocalContext.current
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
                DeviceGrid(context, lights, fan, door, water)
            }
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
    fun DeviceGrid(context: Context, lights: List<LightModel>, fan: ClimateModel, door: DoorModel, water: WaterModel) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            DeviceRow(
                context,
                CardData("Light", "${lights.count { it.isOn }} On", R.drawable.outline_lightbulb_24, Color.Yellow, null),
                CardData("Water", if (water.isPumpOn) "Pump On" else "Pump Off", R.drawable.baseline_water_drop_24, Color.Cyan, WaterActivity::class.java)
            )
            DeviceRow(
                context,
                CardData("Door", if (door.mainDoorLocked) "Locked" else "Unlocked", R.drawable.baseline_sensor_door_24, Color(0xFF4CAF50), DoorlockActivity::class.java),
                CardData("Fan", "${fan.temperature}°C", R.drawable.baseline_air_24, Color(0xFF1FB7FF), ClimateControlActivity::class.java)
            )
            DeviceRow(
                context,
                CardData("Security", "Away Mode", R.drawable.baseline_security_24, Color(0xFFFF9800), SecurityActivity::class.java),
                CardData("Analytics", "120 kWh", R.drawable.baseline_query_stats_24, Color(0xFF7A4FFF), EnergyAnalyticsActivity::class.java)
            )
        }
    }

    data class CardData(val title: String, val status: String, val icon: Int, val color: Color, val activity: Class<*>?)

    @Composable
    fun DeviceRow(context: Context, card1: CardData, card2: CardData) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DeviceCard(modifier = Modifier.weight(1f), card = card1, context = context)
            DeviceCard(modifier = Modifier.weight(1f), card = card2, context = context)
        }
    }

    @Composable
    fun DeviceCard(modifier: Modifier, card: CardData, context: Context) {
        Column(
            modifier = modifier
                .height(135.dp)
                .background(Color(0xFF111A32), RoundedCornerShape(20.dp))
                .let { if (card.activity != null) it.clickable { context.startActivity(Intent(context, card.activity)) } else it }
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
    fun EnergyAnalyticsActivityScreen() = ScreenBox("Analytics Page")
    @Composable
    fun SecurityScreen() = ScreenBox("Security Page")
    @Composable
    fun ProfileActivityScreen() = ScreenBox("Profile Page")
    @Composable
    fun ScreenBox(title: String) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFF111A32)),
            contentAlignment = Alignment.Center
        ) { Text(title, color = Color.White, fontSize = 20.sp) }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewDashboard() {
        DashboardScreen(listOf(LightModel(), LightModel()), ClimateModel(), DoorModel(), WaterModel())
    }
}
