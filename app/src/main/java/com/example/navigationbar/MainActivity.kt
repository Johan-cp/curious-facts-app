package com.example.navigationbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.navigationbar.data.FactsDatabase
import com.example.navigationbar.presentation.FactsViewModel
import com.example.navigationbar.routes.Routes
import com.example.navigationbar.screens.NumbersListScreen
import com.example.navigationbar.screens.NumbersScreen
import com.example.navigationbar.ui.theme.NavigationBarTheme
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Teleport
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius

@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext, FactsDatabase::class.java, "facts.db"
        ).build()
    }

    private val viewModel by viewModels<FactsViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FactsViewModel(database.dao) as T
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationBarTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    CustomBottomAppBar(navController)
                }) { innerPadding ->

                    val state by viewModel.state.collectAsState()

                    NavHost(
                        navController = navController,
                        startDestination = Routes.NUMBERS,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = Routes.NUMBERS) {
                            NumbersScreen(state, viewModel::onEvent)
                        }

                        composable(route = Routes.FAV_NUMBERS) {
                            NumbersListScreen(state, viewModel::onEvent)
                        }
                    }
                }
            }
        }
    }
}

fun NavController.navigateIfNeeded(route: String) {
    val currentRoute = this.currentBackStackEntry?.destination?.route
    if (currentRoute != route) {
        this.navigate(route)
    }
}


@Composable
fun CustomBottomAppBar(navController: NavController) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    BottomAppBar(containerColor = Color.Transparent) {
        AnimatedNavigationBar(
            selectedIndex = selectedIndex,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            cornerRadius = shapeCornerRadius(cornerRadius = 34.dp),
            ballAnimation = Teleport(tween(300)),
            indentAnimation = Height(tween(300)),
            barColor = MaterialTheme.colorScheme.primary,
            ballColor = MaterialTheme.colorScheme.primary
        ) {
            NavigationButton(
                icon = Icons.Default.DateRange,
                onClick = {
                    selectedIndex = 0

                    navController.navigateIfNeeded(Routes.NUMBERS)
                },
                contentDescription = "Home"
            )
            NavigationButton(
                icon = Icons.Default.Favorite,
                onClick = {
                    selectedIndex = 1

                    navController.navigateIfNeeded(Routes.FAV_NUMBERS)
                },
                contentDescription = "Favorites"
            )
            NavigationButton(
                icon = Icons.Default.Info,
                onClick = { selectedIndex = 2 },
                contentDescription = "About"
            )
        }
    }
}

@Composable
fun NavigationButton(icon: ImageVector, onClick: () -> Unit, contentDescription: String) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
fun NavigationButtonPreview() {
    NavigationButton(
        icon = Icons.Default.Call,
        onClick = { /*TODO*/ },
        contentDescription = "Icon Button Preview"
    )
}
