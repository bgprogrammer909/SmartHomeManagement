package com.example.smarthome.view

import android.app.Activity
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
import com.example.smarthome.util.CurrentUser
import com.example.smarthome.viewmodel.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/* ---------------- HOME DASHBOARD ACTIVITY ---------------- */
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

        CurrentUser.userId = uid

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
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
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

/* ---------------- DASHBOARD BODY ---------------- */
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
                1 -> EnergyAnalyticsActivityScreen()
                2 -> SecurityScreen()
                3 -> ProfileActivityScreen {}
            }
        }
    }
}

/* ---------------- BOTTOM NAV ---------------- */
@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    val navItems = listOf(
        NavItem(R.drawable.baseline_home_24, "Dashboard"),
        NavItem(R.drawable.baseline_query_stats_24, "Analytics"),
        NavItem(R.drawable.baseline_security_24, "Security"),
        NavItem(R.drawable.baseline_person_24, "Profile")
    )

    NavigationBar(containerColor = Color(0xFF0D152F)) {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(painterResource(item.icon), null) },
                label = { Text(item.label, fontSize = 12.sp) },
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}

data class NavItem(val icon: Int, val label: String)

/* ---------------- DASHBOARD SCREEN ---------------- */
@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    val userId = CurrentUser.userId ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFF0B1225))
    ) {
        HeaderSection()
        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {

            // Row 1: Light + Water
            DeviceRow(
                context,
                CardData("Light", R.drawable.outline_lightbulb_24, Color.Yellow, PLightActivity::class.java, userId),
                CardData("Water", R.drawable.baseline_water_drop_24, Color.Cyan, WaterActivity::class.java, userId)
            )

            // Row 2: Fan + Door
            DeviceRow(
                context,
                CardData("Fan", R.drawable.ic_refresh, Color(0xFF1FB7FF), ClimateControlActivity::class.java, userId),
                CardData("Door", R.drawable.baseline_sensor_door_24, Color(0xFF4CAF50), DoorLockActivity::class.java, userId)
            )

            // Row 3: Security + Analytics
            DeviceRow(
                context,
                CardData("Security", R.drawable.baseline_security_24, Color(0xFFFF9800), SecurityActivity::class.java, userId),
                CardData("Analytics", R.drawable.baseline_query_stats_24, Color(0xFF7A4FFF), EnergyAnalyticsActivity::class.java, userId)
            )
        }
    }
}

/* ---------------- CARD / ROW ---------------- */
data class CardData(
    val title: String,
    val icon: Int,
    val color: Color,
    val activity: Class<out Activity>,
    val userId: String
)

@Composable
fun DeviceRow(context: Context, card1: CardData, card2: CardData) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        DeviceCard(Modifier.weight(1f), card1, context)
        DeviceCard(Modifier.weight(1f), card2, context)
    }
}

@Composable
fun DeviceCard(modifier: Modifier, card: CardData, context: Context) {
    Column(
        modifier = modifier
            .height(150.dp)
            .background(Color(0xFF111A32), RoundedCornerShape(20.dp))
            .clickable {
                context.startActivity(
                    Intent(context, card.activity).putExtra("USER_ID", card.userId)
                )
            }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(card.icon),
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            colorFilter = ColorFilter.tint(card.color)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(card.title, color = Color.White, fontSize = 16.sp)
    }
}

/* ---------------- HEADER ---------------- */
@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Welcome Home", color = Color.White, fontSize = 20.sp)
    }
}

/* ---------------- OTHER SCREENS ---------------- */
@Composable
fun EnergyAnalyticsActivityScreen() {
    val vm: EnergyViewModel = viewModel()
    EnergyAnalyticsScreen(vm, onBack = {})
}

@Composable
fun SecurityScreen() {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, SecurityActivity::class.java))
    }
}

@Composable
fun ProfileActivityScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    ProfileBody(
        onBackClick,
        onEditClick = { context.startActivity(Intent(context, UserEditProfileActivity::class.java)) },
        onSettingsClick = {},
        onLogoutClick = {
            FirebaseAuth.getInstance().signOut()
            context.startActivity(
                Intent(context, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
    )
}

/* ---------------- PREVIEW ---------------- */
@Preview(showBackground = true)
@Composable
fun PreviewDashboard() {
    DashboardScreen()
}
