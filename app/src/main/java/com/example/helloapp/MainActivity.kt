package com.example.helloapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.helloapp.ui.theme.HelloAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(navController)
                    }
                ) { innerPadding ->
                    NavigationHost(navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            label = { Text("Home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            label = { Text("Lists") },
            icon = { Icon(Icons.Default.List, contentDescription = "Lists") },
            selected = currentRoute == "lists",
            onClick = { navController.navigate("lists") }
        )
    }
}

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = NavRoutes.Home.route)
    {
        composable(NavRoutes.Home.route) { Home() }
        composable(NavRoutes.Lists.route) { Lists() }
    }
}

@Composable
fun Home(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xAB2196F3)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val myName = stringResource(id = R.string.my_name)
        var otherName by remember { mutableStateOf("") }
        var textValue by remember { mutableStateOf("") }
        Text(
            text = "Hello $otherName!",
            modifier = modifier.padding(32.dp, top = 48.dp, bottom = 32.dp),
            fontSize = 32.sp
        )
        Image(
            painter = painterResource(id = R.drawable.bsuir),
            contentDescription = "Hello BSUIR Image",
            modifier = Modifier.size(150.dp)
        )
        TextField(
            value = textValue,
            onValueChange = { textValue = it },
            label = { Text(text = "Введите имя") },
            placeholder = { Text(text = "Имя") },
            modifier = modifier.padding(8.dp)
        )
        Button(
            onClick =
            {
                otherName = textValue
            }
        ) {
            Text(text = "Вывести имя")
        }
        Button(
            onClick =
            {
                textValue = myName
            },
            modifier = modifier.padding(8.dp)
        ) {
            Text(text = "Моё имя")
        }
        Button(
            onClick = {
                textValue = ""
            },
            modifier = modifier.padding(8.dp)
        ) {
            Text(text = "Очистить")
        }
    }
}

@Composable
fun Lists(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {}
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloAppTheme {
        Home()
    }
}

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Lists : NavRoutes("lists")
}