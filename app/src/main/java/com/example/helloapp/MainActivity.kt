package com.example.helloapp

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.helloapp.ui.theme.HelloAppTheme
import java.io.File

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
        NavigationBarItem(
            label = { Text("Picture") },
            icon = { Icon(Icons.Default.Face, contentDescription = "Picture") },
            selected = currentRoute == NavRoutes.Picture.route,
            onClick = { navController.navigate(NavRoutes.Picture.route) }
        )
        NavigationBarItem(
            label = { Text("Camera") },
            icon = { Icon(Icons.Default.Add, contentDescription = "Camera") },
            selected = currentRoute == NavRoutes.Camera.route,
            onClick = { navController.navigate(NavRoutes.Camera.route) }
        )
    }
}

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = NavRoutes.Home.route)
    {
        composable(NavRoutes.Home.route) { Home() }
        composable(NavRoutes.Lists.route) { Lists() }
        composable(NavRoutes.Picture.route) { Picture() }
        composable(NavRoutes.Camera.route) { CameraScreen() }
    }
}

@Composable
fun Home(modifier: Modifier = Modifier) {
    val myName = stringResource(id = R.string.my_name)
    var textValue by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = textValue
        )
        Box(
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { textValue = myName },
                    modifier = Modifier.weight(3f)
                ) {
                    Text(text = "Вывести имя")
                }
                Button(
                    onClick = { textValue = "" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "X")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Lists(modifier: Modifier = Modifier) {
    val books = listOf(
        Book("А зори здесь тихие...", "Михаил Булгаков"),
        Book("Записки юного врача", "Михаил Булгаков"),
        Book("Мастер и Маргарита", "Михаил Булгаков"),
        Book("Собачье Сердце", "Михаил Булгаков"),
        Book("Братья Карамазовы", "Фёдор Достоевский"),
        Book("Преступление и наказание", "Фёдор Достоевский"),
        Book("Братья Карамазовы", "Фёдор Достоевский"),
        Book("Яма", "Александр Куприн")
    )
    val groups = books.groupBy { it.author }
    val configuration = LocalConfiguration.current
    LazyColumn(
        modifier = modifier.padding(
            start = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 50.dp else 0.dp,
            end = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 50.dp else 0.dp,
            top = 48.dp,
            bottom = 75.dp
        ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        groups.forEach { (author, books) ->
            stickyHeader {
                Text(
                    text = author,
                    fontSize = 28.sp,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.Gray)
                        .padding(5.dp)
                        .fillMaxWidth()
                )
            }
            items(books) { book ->
                Text(
                    book.title,
                    Modifier.padding(5.dp),
                    fontSize = 28.sp
                )
            }
        }
    }
}

@Composable
fun Picture(modifier: Modifier = Modifier) {
    var rotated by rememberSaveable { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
        targetValue = if (rotated) 360f else 0f,
        animationSpec = tween(4000)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .rotate(angle),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.bsuir),
                contentDescription = "BSUIR"
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { rotated = !rotated },
            modifier = Modifier.padding(10.dp, bottom = 32.dp)
        ) {
            Text(text = "Повернуть")
        }
    }
}

@Composable
fun CameraScreen(modifier: Modifier = Modifier) {
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var hasImage by rememberSaveable { mutableStateOf(false) }
    var currentUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(), onResult = { uri: Uri? ->
            hasImage = uri != null
            imageUri = uri
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
            if (success) {
                imageUri = currentUri
            }
        }
    )

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Разрешение получено", Toast.LENGTH_SHORT).show()
                currentUri?.let { cameraLauncher.launch(it) }
            } else {
                Toast.makeText(context, "В разрешении отказано", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(top = 64.dp, start = 16.dp, end = 16.dp)
        ) {
            if (hasImage && imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = "Selected Image",
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(16.dp, bottom = 32.dp)
                .align(Alignment.CenterHorizontally)
                .weight(1f, fill = false),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = { imagePicker.launch("image/*") },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Выбрать изображение из галереи")
            }
            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = {
                    currentUri = ComposeFileProvider.getImageUri(context)
                    val permissionCheckResult = ContextCompat.checkSelfPermission(
                        context, android.Manifest.permission.CAMERA
                    )
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(currentUri!!)
                    } else {
                        permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                },
            ) {
                Text(text = "Создать снимок")
            }
        }
    }
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
    object Picture : NavRoutes("picture")
    object Camera : NavRoutes("camera")
}

data class Book(val title: String, val author: String)

class ComposeFileProvider : FileProvider(R.xml.files_paths) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory,
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}