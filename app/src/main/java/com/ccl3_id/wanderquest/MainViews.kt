package com.ccl3_id.wanderquest

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ccl3_id.wanderquest.data.Item
import com.ccl3_id.wanderquest.viewModel.ItemViewModel
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object First : Screen("first")   // Represents the first screen with route "first"
    object Second : Screen("second") // Represents the second screen with route "second"
    object Third : Screen("third")   // Represents the third screen with route "third"
    object Fourth : Screen("fourth")   // Represents the third screen with route "fourth"
}

@OptIn(ExperimentalMaterial3Api::class)
// MainView is a Composable function that creates the main view of your app.
@Composable
fun MainView(itemViewModel: ItemViewModel) {
    // Collect the current state of the main view from the MainViewModel.
    val state = itemViewModel.mainViewState.collectAsState()
    val navController = rememberNavController()

    // Scaffold is a material design container that includes standard layout structures.
    Scaffold{ NavHost(
            navController = navController,
            modifier = Modifier.padding(it), // Apply padding from the Scaffold.
            startDestination = Screen.First.route // Define the starting screen.
        ) {
            // Define the composable function for the 'First' route.
            composable(Screen.First.route) {
                itemViewModel.selectScreen(Screen.First)
                mainScreen(navController)
            }

            // Define the composable function for the 'Second' route.
            composable(Screen.Second.route) {
                itemViewModel.selectScreen(Screen.Second)
                itemsScreen(navController, itemViewModel)
            }

        }
    }
}

@Composable
fun appTitle(navController: NavHostController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Title",
            fontSize = 48.sp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { navController.navigate(Screen.First.route) }
        )
    }
}


@Composable
fun toPacksBtn(navController: NavHostController){
    Button(
        onClick = {
            navController.navigate(Screen.Third.route)
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Packs",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold)
    }
}

// Composable function for the main screen of the app.
@Composable
fun mainScreen(navController: NavHostController){
    appTitle(navController)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //Spacer for appTitle
        Spacer(modifier = Modifier.height(88.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.weapon_chest),
            contentDescription = "Logo",
            modifier = Modifier
                .size(400.dp)
                .clip(shape = MaterialTheme.shapes.medium)
                .clickable { /* Handle logo click if needed */ }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Button to items
        Button(
            onClick = {
                navController.navigate(Screen.Second.route)
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Item list",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button to packs
        toPacksBtn(navController)
    }
}


// Composable function for the items screen of the app.
@Composable
fun itemsScreen(navController: NavHostController, itemViewModel: ItemViewModel){
    appTitle(navController)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //Spacer for appTitle
        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "Item list",
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Button to packs
        toPacksBtn(navController)

        Spacer(modifier = Modifier.height(8.dp))

        // Display items
        val itemsState = itemViewModel.getItemsState.collectAsState()
        val items = itemsState.value

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp) // Adjust padding as needed
        ){
            items(items) { item ->
                ItemCard(item = item)
            }
        }

    }
}

@Composable
fun ItemCard(item: Item) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle item click if needed */ }
    ) {
        // Load image dynamically from the database using the image string
        val imageResource = painterResource(id = getImageResourceId(item.img))
        Image(
            painter = imageResource,
            contentDescription = item.name,
            modifier = Modifier
                .size(100.dp)
                .clip(shape = MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Display item name and type
//        Text(
//            text = "${item.name} - ${item.type}",
//            fontSize = 20.sp,
//            color = MaterialTheme.colorScheme.secondary
//        )
    }
}

// Helper function to get the resource ID of an image based on its name
@Composable
fun getImageResourceId(imageName: String): Int {
    // Assuming you store your images in the res/drawable directory
    // The context parameter is needed to resolve the resource ID
    return LocalContext.current.resources.getIdentifier(imageName, "drawable", LocalContext.current.packageName)
}


