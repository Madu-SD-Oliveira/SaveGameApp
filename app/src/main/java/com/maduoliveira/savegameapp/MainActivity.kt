package com.maduoliveira.savegameapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.maduoliveira.savegameapp.ui.theme.SaveGameAppTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Straight
import com.exyte.animatednavbar.animation.indendshape.StraightIndent
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.maduoliveira.savegameapp.ui.nav.BottomBarScreen
import com.maduoliveira.savegameapp.ui.nav.NavigationGraph
import com.maduoliveira.savegameapp.ui.nav.Screens
import com.maduoliveira.savegameapp.ui.navbottom.colorButtons
import com.maduoliveira.savegameapp.ui.navbottom.colorButtons.ColorButton
import com.maduoliveira.savegameapp.ui.theme.greenLime


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appContainer = application as SaveGameApplication
        val repository = appContainer.financesRepository
        setContent {
            val systemTheme = isSystemInDarkTheme()
            var isDarkModeByUserSettings by remember { mutableStateOf(systemTheme) }

            SaveGameAppTheme(darkTheme = isDarkModeByUserSettings) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute != Screens.SPLASH) {

                            val items = listOf(
                                BottomBarScreen.Dashboard,
                                BottomBarScreen.Transactions,
                                BottomBarScreen.Form,
                                BottomBarScreen.Reports,
                                BottomBarScreen.Settings
                            )
                            val selectedIndex = items.indexOfFirst { screen ->
                                val baseRoute = screen.route.substringBefore("/")
                                currentRoute?.substringBefore("/") == baseRoute
                            }.coerceAtLeast(0)

                            var prevSelectedIndex by remember { mutableIntStateOf(0) }

                            AnimatedNavigationBar(
                                modifier = Modifier
                                    .windowInsetsPadding(WindowInsets.navigationBars)
                                    .padding(horizontal = 8.dp, vertical = 12.dp)
                                    .height(75.dp),
                                selectedIndex = selectedIndex,
                                barColor = MaterialTheme.colorScheme.surface,
                                ballColor = MaterialTheme.colorScheme.onSurface,
                                cornerRadius = shapeCornerRadius(25.dp),
                                ballAnimation = Straight(
                                    spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessVeryLow)
                                ),
                                indentAnimation = StraightIndent(
                                    indentWidth = 56.dp,
                                    indentHeight = 15.dp,
                                    animationSpec = tween(1000)
                                )
                            ) {
                                items.forEachIndexed { index, screen ->

                                    val localConfig = colorButtons.getOrNull(index) ?: colorButtons.first()

                                    ColorButton(
                                        modifier = Modifier.fillMaxSize(),
                                        prevSelectedIndex = prevSelectedIndex,
                                        selectedIndex = selectedIndex,
                                        index = index,
                                        onClick = {
                                            prevSelectedIndex = selectedIndex
                                            val currentBase = currentRoute?.substringBefore("/")
                                            val targetBase = screen.route.substringBefore("/")

                                            if (currentRoute != screen.route) {
                                                navController.navigate(screen.route) {
                                                    popUpTo(Screens.DASHBOARD) { saveState = true }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        },
                                        icon = localConfig.icon,
                                        contentDescription = screen.title,
                                        animationType = localConfig.animationType,
                                        background = localConfig.animationType.background
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavigationGraph(
                        navController = navController,
                        innerPadding = innerPadding,
                        isDark = isDarkModeByUserSettings,
                        onThemeChange = { isDarkModeByUserSettings = it }
                    )
                }
            }
        }
    }
}