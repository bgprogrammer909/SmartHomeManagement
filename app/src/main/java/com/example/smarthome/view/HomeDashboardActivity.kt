package com.example.smarthome.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthome.R
import com.example.smarthome.view.ui.theme.SmartHomeTheme



class HomeDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardScreen()
        }
    }
}

@Composable
fun DashboardScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1225))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 90.dp),
        ) {
            HeaderSection()
            Spacer(modifier = Modifier.height(20.dp))
            DeviceGrid()
        }

        // Fixed Bottom Navigation Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            BottomNavigationBar()
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
            Text(
                text = "Welcome Home,",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 15.sp
            )
            Text(
                text = "Alex",
                color = Color.White,
                fontSize = 20.sp
            )
        }

        Image(
            painter = painterResource(R.drawable.baseline_person_24),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun DeviceGrid() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DeviceCard("Light", "4 On", R.drawable.outline_lightbulb_24, toggle = true)
            DeviceCard("Water", "Pump Off", R.drawable.baseline_water_drop_24, toggle = true)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            DeviceCard(
                "Door",
                "Main Entrance",
                R.drawable.baseline_sensor_door_24,
                badge = "Locked"
            )
            DeviceCard(
                "Fan",
                "24°C",
                R.drawable.baseline_air_24,
                toggle = true
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DeviceCard(
                "Security",
                "Away Mode",
                R.drawable.baseline_lock_open_24,
                badge = "Armed"
            )
            DeviceCard(
                "Analytics",
                "120 kWh",
                R.drawable.baseline_query_stats_24
            )
        }
    }
}

@Composable
fun DeviceCard(
    title: String,
    status: String,
    icon: Int,
    toggle: Boolean = false,
    badge: String? = null
) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .height(135.dp)
            .background(Color(0xFF111A32), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )


        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = title, color = Color.White, fontSize = 16.sp)
        Text(text = status, color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
    }
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0D152F), RoundedCornerShape(30.dp))
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem(R.drawable.baseline_home_24, "Dashboard", true)
        NavItem(R.drawable.baseline_bedroom_child_24, "Rooms")
        NavItem(R.drawable.baseline_query_stats_24, "Analytics")
        NavItem(R.drawable.baseline_security_24, "Security")
        NavItem(R.drawable.baseline_person_24, "Profile")




    }
}

@Composable
fun NavItem(icon: Int, label: String, selected: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(icon),
            contentDescription = label,
            modifier = Modifier.size(26.dp)
        )
        Text(
            text = label,
            color = if (selected) Color.White else Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewDashboard() {
    DashboardScreen()
}
