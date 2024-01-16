package com.ccl3_id.wanderquest.ui.views

import android.content.Intent
import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ccl3_id.wanderquest.LevelUpActivity
import com.ccl3_id.wanderquest.LoginActivity
import com.ccl3_id.wanderquest.R
import com.ccl3_id.wanderquest.data.models.entities.Player
import com.ccl3_id.wanderquest.data.models.items.EquipedItem
import com.ccl3_id.wanderquest.data.models.items.Item
import com.ccl3_id.wanderquest.viewModels.ItemViewModel
import com.ccl3_id.wanderquest.viewModels.MainViewModel

sealed class Screen(val route: String){
    object Character: Screen("Character")
    object Items: Screen("Items")
    object Dungeon: Screen("Dungeon")
}


@Composable
fun MainView(mainViewModel : MainViewModel, itemViewModel: ItemViewModel) {
    // Collect the current state of the main view from the MainViewModel.
    val state = mainViewModel.mainViewState.collectAsState()
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {BottomNavigationBar(navController, state.value.selectedScreen)}
    ) {
        NavHost(
            navController = navController,
            modifier = Modifier.padding(it),
            startDestination = Screen.Character.route
        ){
            composable(Screen.Character.route){
                mainViewModel.getPlayer();
                mainViewModel.selectScreen(Screen.Character);
                displayCharacterSheet(mainViewModel)
            }
            composable(Screen.Items.route){
                itemViewModel.getItems()
                itemViewModel.getEquipItems(state.value.selectedPlayer!!.id)
                mainViewModel.selectScreen(Screen.Items);
                itemsScreen(itemViewModel, mainViewModel)
            }
            composable(Screen.Dungeon.route){
                mainViewModel.getPlayer();
                mainViewModel.selectScreen(Screen.Dungeon);
                displayBattleScreen(mainViewModel);
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, selectedScreen: Screen){
    BottomNavigation (
        backgroundColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(
            selected = (selectedScreen == Screen.Character),
            onClick = { navController.navigate(Screen.Character.route) },
            icon = { Icon(painter = painterResource(id = R.drawable.cowled), contentDescription = "Character Screen") })

        NavigationBarItem(
            selected = (selectedScreen == Screen.Items),
            onClick = { navController.navigate(Screen.Items.route) },
            icon = { Icon(painter = painterResource(id = R.drawable.cowled), contentDescription = "Item Screen") })

        NavigationBarItem(
            selected = (selectedScreen == Screen.Dungeon),
            onClick = { navController.navigate(Screen.Dungeon.route) },
            icon = { Icon( painter = painterResource(id = R.drawable.battle_gear), contentDescription = "Dungeon Screen") })
    }
}


// Composable function for the items screen of the app.
@Composable
fun itemsScreen(itemViewModel: ItemViewModel, mainViewModel: MainViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val mainState = itemViewModel.mainViewState.collectAsState()
        val items = mainState.value.allItems
        val equipedItems = mainState.value.allEquipedItem

        Text(
            text = "Equiped:",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Display items
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ){
            items(equipedItems) { equipedItem ->
                EquipedItemCard(equipedItem, itemViewModel)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Items:",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Display items
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ){
            items(items) { item ->
                ItemCard(item, itemViewModel)
            }
        }
    }
    Column {
        ItemPopUp(itemViewModel, mainViewModel)
    }
    Column {
        EquipedItemPopUp(itemViewModel, mainViewModel)
    }
}

@Composable
fun ItemCard(item: Item, itemViewModel: ItemViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { itemViewModel.selcetItem(item) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemPopUp(itemViewModel : ItemViewModel, mainViewModel: MainViewModel){
    val mainViewState = itemViewModel.mainViewState.collectAsState()
    val mainMainViewState = mainViewModel.mainViewState.collectAsState()

    if (mainViewState.value.itemClicked){
        AlertDialog(onDismissRequest = {
                itemViewModel.deselectItem()
            },
            text = {
                Column {
                    // https://www.jetpackcompose.net/textfield-in-jetpack-compose
                    Text(
                        text = mainViewState.value.clickedItem!!.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = mainViewState.value.clickedItem!!.type,
                        fontSize = 14.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        itemViewModel.equipItem(mainViewState.value.clickedItem!!.id, mainMainViewState.value.selectedPlayer!!.id)
                        itemViewModel.deselectItem()
                    }
                ) {
                    Text(
                        text = "Equip"
                    )
                }
            }
        )
    }
}

@Composable
fun EquipedItemPopUp(itemViewModel : ItemViewModel, mainViewModel: MainViewModel){
    val mainViewState = itemViewModel.mainViewState.collectAsState()
    val mainMainViewState = mainViewModel.mainViewState.collectAsState()

    if(mainViewState.value.equipedItemClicked){
        AlertDialog(onDismissRequest = {
            itemViewModel.deselectItem()
        },
            text = {
                Column {
                    // https://www.jetpackcompose.net/textfield-in-jetpack-compose
                    Text(
                        text = mainViewState.value.clickedEquipedItem!!.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = mainViewState.value.clickedEquipedItem!!.type,
                        fontSize = 14.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        itemViewModel.unequipItem(mainViewState.value.clickedEquipedItem!!.id, mainMainViewState.value.selectedPlayer!!.id)
                        itemViewModel.deselectItem()
                    },
                ) {
                    Text(
                        text = "Unequipe"
                    )
                }
            }
        )
    }
}

@Composable
fun EquipedItemCard(equipedItem: EquipedItem, itemViewModel: ItemViewModel){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { itemViewModel.selcetEquipedItem(equipedItem) }
    ) {
        // Load image dynamically from the database using the image string
        val imageResource = painterResource(id = getImageResourceId(equipedItem.img))
        Image(
            painter = imageResource,
            contentDescription = equipedItem.name,
            modifier = Modifier
                .size(100.dp)
                .clip(shape = MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(4.dp))
    }
}

// Helper function to get the resource ID of an image based on its name
@Composable
fun getImageResourceId(imageName: String): Int {
    // Assuming you store your images in the res/drawable directory
    // The context parameter is needed to resolve the resource ID
    return LocalContext.current.resources.getIdentifier(imageName, "drawable", LocalContext.current.packageName)
}

@Composable
fun displayCharacterSheet(mainViewModel: MainViewModel){

    val state = mainViewModel.mainViewState.collectAsState()
    val player = state.value.selectedPlayer;
    val context = LocalContext.current
    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 20.dp, start = 20.dp)
    ) {
        if (player != null) {

            displayPlayerInfo(player);

            Spacer(modifier = Modifier.padding(15.dp))

            displayPlayerStats(player);

            Spacer(modifier = Modifier.padding(15.dp))

            displayPlayerAbilities(player);

        }

        Button(
            onClick = {
                val intent = Intent(context, LoginActivity::class.java);
                context.startActivity(intent);
            },
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Logout", fontSize = 25.sp)
        }

    }
}

@Composable
fun displayPlayerInfo(player: Player) {

    val context = LocalContext.current

    Text(
        text = player.playerName,
        fontSize = 30.sp,
        style = TextStyle(fontFamily = FontFamily.Monospace)
    )
    Text(
        text = "Class: "+player.playerClass,
        fontSize = 20.sp,
    )

    Text(
        text = "Level: " + player.playerLevel,
        fontSize = 20.sp,
    )

    Text(
        text = "Max HP: " + player.entityMaxHealth,
        fontSize = 20.sp,
    )
    //player.playerCurrentXP >= player.playerXPToNextLevel
    if(player.playerCurrentXP >= player.playerXPToNextLevel){
        Button(
            onClick = {
                val intent = Intent(context, LevelUpActivity::class.java); context.startActivity(intent);
            }
        ) {
            Text(text = "Level UP!", fontSize = 15.sp)
        }
    }else{
        Text(
            text = "XP: " + player.playerCurrentXP + "/" + player.playerXPToNextLevel,
            fontSize = 20.sp,
        )
    }
}

@Composable
fun displayPlayerStats(player: Player){
    Text(
        text = "Stats: ",
        fontSize = 30.sp,
    )

    Text(
        text = "Strength: " + player.playerStats[Player.STAT_STRENGTH],
        fontSize = 20.sp,
    )
    Text(
        text = "Stamina: " +player.playerStats[Player.STAT_STAMINA],
        fontSize = 20.sp,
    )
    Text(
        text = "Dexterity: "  + player.playerStats[Player.STAT_DEXTERITY],
        fontSize = 20.sp,
    )
    Text(
        text = "Constitution: "  + player.playerStats[Player.STAT_CONSTITUTION],
        fontSize = 20.sp,
    )
    Text(
        text = "Motivation: "  +player.playerStats[Player.STAT_MOTIVATION],
        fontSize = 20.sp,
    )

}

@Composable
fun displayPlayerAbilities(player: Player){
    Text(
        text = "Abilities: ",
        fontSize = 30.sp,
    )

    Text(
        text = player.abilityOneName+ ": " + player.abilityOneDescription,
        fontSize = 20.sp,
    )
    Text(
        text = player.abilityTwoName+ ": " + player.abilityTwoDescription,
        fontSize = 20.sp,
    )
    Text(
        text = player.abilityThreeName+ ": " + player.abilityThreeDescription,
        fontSize = 20.sp,
    )
    Text(
        text = player.abilityFourName+ ": " + player.abilityFourDescription,
        fontSize = 20.sp,
    )

}


@Composable
fun displayBattleScreen(mainViewModel: MainViewModel) {

    val state = mainViewModel.mainViewState.collectAsState()

    if(state.value.battleStarted){
        displayBattleContent(mainViewModel);
    }else{
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,) {
            Button(
                onClick = {
                    mainViewModel.startBattle();
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Start Battle", fontSize = 20.sp)
            }

        }
    }
}

@Composable
fun displayBattleContent(mainViewModel: MainViewModel){

    val state = mainViewModel.mainViewState.collectAsState()
    val enemy = state.value.enemy;
    val player = state.value.selectedPlayer;

    val enemyText =  state.value.enemyText;
    val playerText = state.value.playerText;

    val enemyCurrentHealth = state.value.currentEnemyHealth;
    val playerCurrentHealth = state.value.currentPlayerHealth;
    val battleCompleteText = state.value.battleCompleteText;

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)) {

        // Enemy Section (Aligned Right)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.End, // Aligns content to the right
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth(0.5f)) {
                Box(modifier = Modifier
                    .size(100.dp, 100.dp)
                    .background(Color.Gray))
                Text(text = "${enemy!!.entityName}", modifier = Modifier.padding(top = 4.dp))
                Row( verticalAlignment = Alignment.CenterVertically){
                    Text(text = "$enemyCurrentHealth/${enemy!!.entityMaxHealth}", modifier = Modifier.padding(4.dp))
                    LinearProgressIndicator(progress = (enemyCurrentHealth.toFloat()/enemy!!.entityMaxHealth.toFloat()).toFloat(), modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(10.dp), color = Color.Red)
                }
            }
        }

        // Player Section (Aligned Left)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Start, // Aligns content to the left
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth(0.5f)) {

                Box(modifier = Modifier
                    .size(100.dp, 100.dp)
                    .background(Color.Blue))
                Text(text = "${player!!.entityName}", modifier = Modifier.padding(top = 4.dp))
                Row( verticalAlignment = Alignment.CenterVertically){
                    Text(text = "$playerCurrentHealth/${player!!.entityMaxHealth}", modifier = Modifier.padding(4.dp))
                    LinearProgressIndicator(progress = (playerCurrentHealth.toFloat()/player!!.entityMaxHealth.toFloat()).toFloat(), modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(10.dp), color = Color.Green)
                }

            }
        }


        Text(text = "$enemyText", fontSize = 20.sp)
        Text(text = "$playerText", fontSize = 20.sp)


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            if(!state.value.battleComplete){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { mainViewModel.useAbility(1) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    ) {
                        Text(text = player!!.abilityOneName, fontSize = 20.sp)
                    }
                    Button(
                        onClick = {  mainViewModel.useAbility(2) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    ) {
                        Text(text = player!!.abilityTwoName, fontSize = 20.sp)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {  mainViewModel.useAbility(3) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    ) {
                        Text(text = player!!.abilityThreeName, fontSize = 20.sp)
                    }
                    Button(
                        onClick = {  mainViewModel.useAbility(4) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    ) {
                        Text(text = player!!.abilityFourName, fontSize = 20.sp)
                    }
                }
            }else{
                Text(text = "$battleCompleteText", fontSize = 20.sp)
            }



            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Button(
                    onClick = {
                        mainViewModel.leaveBattle();
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Run away", fontSize = 20.sp)
                }
            }

        }
    }
}


