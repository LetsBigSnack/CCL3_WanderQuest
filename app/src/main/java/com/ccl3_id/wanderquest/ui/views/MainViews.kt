package com.ccl3_id.wanderquest.ui.views

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import com.ccl3_id.wanderquest.data.models.dungeons.Dungeon
import com.ccl3_id.wanderquest.data.models.entities.Player
import com.ccl3_id.wanderquest.data.models.items.Item
import com.ccl3_id.wanderquest.viewModels.MainViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ccl3_id.wanderquest.viewModels.ItemViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.unit.IntOffset
import com.ccl3_id.wanderquest.data.models.rooms.Room
import kotlinx.coroutines.coroutineScope


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
        bottomBar = {BottomNavigationBar(navController, state.value.selectedScreen,mainViewModel)}
    ) {
        NavHost(
            navController = navController,
            modifier = Modifier.padding(it),
            startDestination = Screen.Character.route
        ){
            composable(Screen.Character.route){
                LaunchedEffect(Unit) { // Unit can be replaced with a specific key if needed
                    mainViewModel.selectScreen(Screen.Character)
                }
                mainViewModel.getPlayer()
                displayCharacterSheet(mainViewModel, itemViewModel)
            }
            composable(Screen.Items.route){
                LaunchedEffect(Unit) { // Unit can be replaced with a specific key if needed
                    mainViewModel.selectScreen(Screen.Items);
                }
                itemViewModel.getItems(state.value.selectedPlayer!!.id)
                itemViewModel.getEquipItems(state.value.selectedPlayer!!.id)
                itemsScreen(itemViewModel, mainViewModel)
            }
            composable(Screen.Dungeon.route){
                LaunchedEffect(Unit) { // Unit can be replaced with a specific key if needed
                    mainViewModel.selectScreen(Screen.Dungeon);
                }
                mainViewModel.getPlayer();
                mainViewModel.getOpenDungeons()
                mainViewModel.getActiveDungeons()
                //displayDungeons(mainViewModel)
                displayBattleScreen(mainViewModel);
                //ScrollableCanvasWithRectangles()
            }
        }
    }
}

@Composable
fun ScrollableCanvasWithRectangles(mainViewModel: MainViewModel) {

    val state = mainViewModel.mainViewState.collectAsState()
    val dungeonRooms = state.value.dungeonRooms


    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    val canvasSize = 2000.dp // The total size of the canvas
    val canvasModifier = Modifier
        .horizontalScroll(horizontalScrollState)
        .verticalScroll(verticalScrollState)
        .size(canvasSize)

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val rectSize = with(density) { Room.ROOM_SIZE.dp.toPx() }
    val coroutineScope = rememberCoroutineScope()

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val maxOffsetX = with(density) { canvasSize.toPx() - screenWidth }
    val maxOffsetY = with(density) { canvasSize.toPx() - screenHeight }


    LaunchedEffect(key1 = "initialScroll") {
        //offsetX = maxOffsetX / 2
        //offsetY = maxOffsetY / 2
    }

    dungeonRooms!!.forEach { row ->
        row.forEach { room ->
            if (room != null) {
                val topLeft =  with(density) {Offset(room.xIndex * Room.SLOT_SIZE.dp.toPx() + room.randomX.dp.toPx(), room.yIndex * Room.SLOT_SIZE.dp.toPx()  + room.randomY.dp.toPx())}
                room.centerPos = Offset(topLeft.x + rectSize / 2, topLeft.y + rectSize / 2)
            }
        }
    }

    Column {
        Canvas(modifier = canvasModifier.fillMaxSize().weight(1f)
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    println("DRAG detected")
                    coroutineScope.launch {
                        horizontalScrollState.scrollBy(-dragAmount.x)
                        verticalScrollState.scrollBy(-dragAmount.y)
                    }
                }
            }.pointerInput(Unit) {
                detectTapGestures { offset ->
                    dungeonRooms!!.forEach { row ->
                        row.forEach { room ->
                            if (room != null) {
                                val center = room.centerPos!!
                                val topLeft =
                                    Offset(center.x - rectSize / 2, center.y - rectSize / 2)
                                val bottomRight =
                                    Offset(center.x + rectSize / 2, center.y + rectSize / 2)
                                if (offset.x >= topLeft.x && offset.x <= bottomRight.x && offset.y >= topLeft.y && offset.y <= bottomRight.y) {
                                    // Display toast with the indices
                                    //CALL Function
                                    println("CLicked on Rect ${room.xIndex} ${room.yIndex}")
                                    return@detectTapGestures
                                }
                            }
                        }
                    }
                }

            }, onDraw = {

            for (i in dungeonRooms!!.indices) {
                for (j in dungeonRooms!![i].indices) {
                    val currentRoom = dungeonRooms!![i][j]
                    if (currentRoom != null) {
                        // Draw line to the right
                        if (j < dungeonRooms!![i].lastIndex && dungeonRooms!![i][j + 1] != null) {
                            drawLine(
                                color = Color.Black,
                                start = currentRoom!!.centerPos!!,
                                end = dungeonRooms!![i][j + 1]!!.centerPos!!,
                                strokeWidth = 30f
                            )
                        }
                        // Draw line below
                        if (i < dungeonRooms!!.lastIndex && dungeonRooms!![i + 1][j] != null) {
                            drawLine(
                                color = Color.Black,
                                start = currentRoom!!.centerPos!!,
                                end = dungeonRooms!![i + 1][j]!!.centerPos!!,
                                strokeWidth = 30f
                            )
                        }
                    }
                }
            }

            dungeonRooms!!.forEach { row ->
                row.forEach { room ->
                    if (room != null) {
                        val center = room.centerPos!!
                        val topLeft = Offset(center.x - rectSize / 2, center.y - rectSize / 2)

                        var color = Color.Blue

                        when (room.roomType) {

                            "Monster" -> color = Color.Red
                            "Item" -> color = Color.Yellow
                            "Empty" -> color = Color.Gray

                        }

                        drawRect(
                            color = color,
                            topLeft = topLeft,
                            size = Size(rectSize, rectSize)
                        )
                    }
                }
            }
        })

        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.Bottom

        ) {
            Button(
                onClick = {
                },
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, bottom = 15.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Enter Room", fontSize = 25.sp)

            }

            Button(
                onClick = {
                    mainViewModel.leaveDungeon()
                },
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Leave Dungeon", fontSize = 25.sp)
            }
        }

    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController, selectedScreen: Screen, mainViewModel: MainViewModel){
    BottomNavigation (
        backgroundColor = MaterialTheme.colorScheme.secondary
    ) {
        NavigationBarItem(
            selected = (selectedScreen == Screen.Character),
            colors = androidx.compose.material3.NavigationBarItemDefaults
                .colors(indicatorColor = MaterialTheme.colorScheme.onBackground),
            onClick = {
                mainViewModel.selectScreen(Screen.Character);
                navController.navigate(Screen.Character.route) },
            icon = { Icon(painter = painterResource(id = R.drawable.cowled), contentDescription = "Character Screen") })

        NavigationBarItem(
            selected = (selectedScreen == Screen.Items),
            colors = androidx.compose.material3.NavigationBarItemDefaults
                .colors(indicatorColor = MaterialTheme.colorScheme.onBackground),
            onClick = {
                mainViewModel.selectScreen(Screen.Items);
                navController.navigate(Screen.Items.route)
                      },
            icon = { Icon(painter = painterResource(id = R.drawable.cowled), contentDescription = "Item Screen") })

        NavigationBarItem(
            selected = (selectedScreen == Screen.Dungeon),
            colors = androidx.compose.material3.NavigationBarItemDefaults
                .colors(indicatorColor = MaterialTheme.colorScheme.onBackground),
            onClick = {
                mainViewModel.selectScreen(Screen.Dungeon);
                navController.navigate(Screen.Dungeon.route) },
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
        Spacer(modifier = Modifier.height(8.dp))

        displayPlayerEquipedItems(itemViewModel)

        Spacer(modifier = Modifier.height(24.dp))

        displayPlayerItems(itemViewModel)
    }
    Column {
        ItemPopUp(itemViewModel, mainViewModel)
    }
    Column {
        EquippedItemPopUp(itemViewModel, mainViewModel)
    }
}

@Composable
fun ItemCard(item: Item, itemViewModel: ItemViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { itemViewModel.selcetItem(item) }
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.onSecondary, shape = RoundedCornerShape(10.dp))
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

@Composable
fun displayPlayerItems(itemViewModel: ItemViewModel){
    val mainState = itemViewModel.mainViewState.collectAsState()
    val items = mainState.value.allItems

    Text(
        text = "Items:",
        fontSize = 32.sp,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemPopUp(itemViewModel : ItemViewModel, mainViewModel: MainViewModel){
    val itemViewState = itemViewModel.mainViewState.collectAsState()
    val equippedItems = itemViewState.value.equippedItemSlots

    val mainViewState = mainViewModel.mainViewState.collectAsState()
    val player = mainViewState.value.selectedPlayer!!

    if (itemViewState.value.itemClicked){
        val clickedItem = itemViewState.value.clickedItem!!
        val isAlreadyEquipped = equippedItems[clickedItem.type]

        if(isAlreadyEquipped != null){
            isAlreadyEquippedPopUp(clickedItem, itemViewModel, player)
        }
        else{
            ItemPopUpEquip(clickedItem, itemViewModel, player)
        }
    }
}

@Composable
fun ItemPopUpEquip(clickedItem: Item, itemViewModel: ItemViewModel, player: Player){
    val itemStats = clickedItem.getStats() // Get parsed stats

    AlertDialog(
        onDismissRequest = {
            itemViewModel.deselectItem()
        },
        containerColor = MaterialTheme.colorScheme.background,
        text = {
            Column{
                // https://www.jetpackcompose.net/textfield-in-jetpack-compose
                Text(
                    text = clickedItem.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = clickedItem.type,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (itemStats != null) {
                    Text(
                        "Stat Buffs: ${itemStats.statBuffs.entries.joinToString { "${it.key}: ${it.value}" }}",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Abilities: ${itemStats.abilities}",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Stat Nerfs: ${itemStats.statNerfs.entries.joinToString { "${it.key}: ${it.value}" }}",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    Text(
                        "No additional stats available.",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    itemViewModel.equipItem(clickedItem, player)
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

@Composable
fun isAlreadyEquippedPopUp(clickedItem: Item, itemViewModel: ItemViewModel, player: Player){
    val itemStats = clickedItem.getStats() // Get parsed stats

    AlertDialog(
        onDismissRequest = {
            itemViewModel.deselectItem()
        },
        containerColor = MaterialTheme.colorScheme.background,
        text = {
            Column {
                // https://www.jetpackcompose.net/textfield-in-jetpack-compose
                Text(
                    text = clickedItem.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = clickedItem.type,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (itemStats != null) {
                    Text(
                        "Stat Buffs: ${itemStats.statBuffs.entries.joinToString { "${it.key}: ${it.value}" }}",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Abilities: ${itemStats.abilities}",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Stat Nerfs: ${itemStats.statNerfs.entries.joinToString { "${it.key}: ${it.value}" }}",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    Text(
                        "No additional stats available.",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "You have already equipped an item of this type. Would you like to replace it?",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    itemViewModel.replaceEquippedItem(clickedItem, player)
                    itemViewModel.deselectItem()
                }){
                Text("Replace")
            }
        },
        dismissButton = {
            Button(onClick = { itemViewModel.deselectItem() }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EquippedItemCard(equippedItem: Item, itemViewModel: ItemViewModel){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { itemViewModel.selcetEquippedItem(equippedItem) }
            .padding(4.dp)
            .background(
                MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        // Load image dynamically from the database using the image string
        val imageResource = painterResource(id = getImageResourceId(equippedItem.img))
        Image(
            painter = imageResource,
            contentDescription = equippedItem.name,
            modifier = Modifier
                .size(100.dp)
                .clip(shape = MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(4.dp))
    }
}


@Composable
fun displayPlayerEquipedItems(itemViewModel: ItemViewModel){
    val itemState = itemViewModel.mainViewState.collectAsState()
    val equippedItems = itemState.value.equippedItemSlots

    Text(
        text = "Equipped Items:",
        fontSize = 30.sp
    )

    // Display equipped items
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
    ){
        items(equippedItems.entries.toList()) { (key, equippedItem) ->
            if (equippedItem != null) {
                EquippedItemCard(equippedItem, itemViewModel)
            }
            else{
                EmptySlotPlaceholder()
            }
        }
    }
}


@Composable
fun EquippedItemPopUp(itemViewModel : ItemViewModel, mainViewModel: MainViewModel){
    val mainViewState = itemViewModel.mainViewState.collectAsState()
    val mainMainViewState = mainViewModel.mainViewState.collectAsState()


    if(mainViewState.value.equippedItemClicked){
        val clickedItem = mainViewState.value.clickedItem!!

        val itemStats = clickedItem.getStats() // Get parsed stats
        AlertDialog(
            onDismissRequest = {
                itemViewModel.deselectItem()
            },
            containerColor = MaterialTheme.colorScheme.background,
            text = {
                Column {
                    // https://www.jetpackcompose.net/textfield-in-jetpack-compose
                    Text(
                        text = mainViewState.value.clickedEquippedItem!!.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = mainViewState.value.clickedEquippedItem!!.type,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (itemStats != null) {
                        Text(
                            "Stat Buffs: ${itemStats.statBuffs.entries.joinToString { "${it.key}: ${it.value}" }}",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Abilities: ${itemStats.abilities}",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Stat Nerfs: ${itemStats.statNerfs.entries.joinToString { "${it.key}: ${it.value}" }}",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        Text(
                            "No additional stats available.",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        itemViewModel.unequipItem(mainViewState.value.clickedEquippedItem!!, mainMainViewState.value.selectedPlayer!!)
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

// Helper function to get the resource ID of an image based on its name
@Composable
fun getImageResourceId(imageName: String): Int {
    // Assuming you store your images in the res/drawable directory
    // The context parameter is needed to resolve the resource ID
    return LocalContext.current.resources.getIdentifier(imageName, "drawable", LocalContext.current.packageName)
}

@Composable
fun displayCharacterSheet(mainViewModel: MainViewModel, itemViewModel: ItemViewModel){
    //TODO Combine itemViewModel and mainViewModel
    val state = mainViewModel.mainViewState.collectAsState()
    val player = state.value.selectedPlayer;
    val context = LocalContext.current

    //TODO refactor
    itemViewModel.getEquipItems(state.value.selectedPlayer!!.id)

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 20.dp, start = 12.dp, end = 12.dp)
    ) {
        if (player != null) {

            displayPlayerInfo(player);

            Spacer(modifier = Modifier.padding(15.dp))

            displayPlayerEquipedItems(itemViewModel)

            Spacer(modifier = Modifier.padding(15.dp))

            displayPlayerStats(player, itemViewModel);

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
fun EmptySlotPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(
                MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Spacer(modifier = Modifier
            .size(104.dp)
            .clip(shape = MaterialTheme.shapes.medium))
    }
}

@Composable
fun displayPlayerStats(player: Player, itemViewModel: ItemViewModel){
    // Custom styles
    val cardBackgroundColor = MaterialTheme.colorScheme.secondary
    val textColor = MaterialTheme.colorScheme.onSurface
    val statNameStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
    val statValueStyle = TextStyle(fontSize = 18.sp, color = textColor)

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Characteristics", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textColor, modifier = Modifier.align(Alignment.CenterHorizontally))

        val equippedItems = itemViewModel.mainViewState.collectAsState().value.equippedItemSlots
        Player.STAT_LIST.chunked(3).forEachIndexed { index, chunkedStats ->
            Row(
                horizontalArrangement = if (index == 0) Arrangement.SpaceBetween else Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                chunkedStats.forEach { stat ->
                    val baseValue = player.playerStats[stat] ?: 0
                    var totalBuffNerf = 0

                    equippedItems.values.filterNotNull().forEach { item ->
                        item.updateItemStatsFromJSON()
                        totalBuffNerf += item.itemStats.getOrDefault(stat, 0)
                    }

                    Card(
                        backgroundColor = cardBackgroundColor,
                        modifier = Modifier
                            .padding(4.dp)
                            .width(110.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = stat, style = statNameStyle)
                            Text(text = "$baseValue ${if (totalBuffNerf != 0) "%+d".format(totalBuffNerf) else ""}", style = statValueStyle)
                        }
                    }
                }
            }
        }
    }
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
fun displayDungeons(mainViewModel: MainViewModel){

    val state = mainViewModel.mainViewState.collectAsState()
    val openDungeon = state.value.allOpenDungeons;
    val activeDungeon = state.value.allActiveDungeon;


    if(state.value.dungeonRooms == null){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 5.dp, end = 5.dp)
        ) {
            Text(
                text = "Active Dungeons ${activeDungeon.size}/3",
                fontSize = 25.sp,
                style = TextStyle(fontFamily = FontFamily.Monospace)
            )

            displayActiveDungeons(activeDungeon, mainViewModel);

            Text(
                text = "Open Dungeons",
                fontSize = 25.sp,
                style = TextStyle(fontFamily = FontFamily.Monospace)
            )

            displayOpenDungeons(openDungeon, mainViewModel);

        }
    }else{
        ScrollableCanvasWithRectangles(mainViewModel)
    }
}

@Composable
fun displayOpenDungeons(openDungeon: List<Dungeon>, mainViewModel: MainViewModel) {
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(openDungeon) {
            OpenDungeonItem(it, mainViewModel)
        }
    }
}

@Composable
fun displayActiveDungeons(openDungeon: List<Dungeon>, mainViewModel: MainViewModel) {


    for (dungeon in openDungeon){
        ActiveDungeonItem(dungeon, mainViewModel)
    }

    for(i in openDungeon.size+1..3){
        ActiveDungeonItem(null, mainViewModel)
    }

}



@Composable
fun OpenDungeonItem(dungeon: Dungeon, mainViewModel: MainViewModel) {

    val state = mainViewModel.mainViewState.collectAsState()
    val context= LocalContext.current;
    val activeDungeonNumber = state.value.allActiveDungeon.size;
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color(128, 128, 128)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column( modifier = Modifier.weight(1f)){
            Text(text = dungeon.dungeonName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "Distance: ${dungeon.displayTotalDistance()}", fontSize = 20.sp)
            CountdownTimer(convertDateStringToMillis(dungeon.dungeonExpiresIn),dungeon.id)
        }
        Button(
            onClick = { mainViewModel.enterDungeon(dungeon,context) },
            enabled = activeDungeonNumber < 3
        ) {
            Text(text = "Enter")
        }
    }
}

@Composable
fun ActiveDungeonItem(dungeon: Dungeon? = null, mainViewModel: MainViewModel) {

    val density = LocalDensity.current
    val slotSize = with(density) { Room.SLOT_SIZE.dp.toPx() }
    val rectSize = with(density) { Room.ROOM_SIZE.dp.toPx() }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(10.dp)
            .background(Color(128, 128, 128)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if(dungeon != null){

            Column( modifier = Modifier.weight(1f)){
                Text(text = dungeon.dungeonName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                if(dungeon.dungeonWalkedDistance < dungeon.dungeonTotalDistance){
                    Text(text = "Distance:  ${dungeon.displayWalkedDistance()} / ${dungeon.displayTotalDistance()}", fontSize = 20.sp)
                }
            }
            //TODO add Dialog if you are sure
            IconButton(onClick = { mainViewModel.deleteDungeon(dungeon) }) {
                Icon(Icons.Default.Delete,"Delete")
            }
            if(dungeon.dungeonWalkedDistance >= dungeon.dungeonTotalDistance){
                Button(
                    onClick = { mainViewModel.selectDungeon(dungeon)  },
                ) {
                    Text(text = "Enter")
                }
            }
        }
    }
}

fun convertDateStringToMillis(dateString: String): Long {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.parse(dateString)?.time ?: 0L
}

@Composable
fun CountdownTimer(expireTime: Long, dungeonID : Int) {
    // This state will hold the remaining time
    var remainingTime by remember { mutableStateOf("") }

    // Remember a CoroutineScope for this CountdownTimer composable
    val coroutineScope = rememberCoroutineScope()

    // Launching a coroutine that ticks every second
    DisposableEffect(key1 = coroutineScope) {
        // Create a flow that emits every second
        val tickerFlow = flow {
            val endTime = expireTime
            while (currentCoroutineContext().isActive) {
                val currentTime = System.currentTimeMillis()
                val remainingMillis = endTime - currentTime
                if (remainingMillis <= 0) {
                    emit("00:00:00:00") // Days:Hours:Minutes:Seconds
                    break
                }
                emit(formatRemainingTime(remainingMillis))
                delay(1000)
            }
        }

        // Coroutine that collects the ticker flow
        coroutineScope.launch {
            tickerFlow.collect { newTime ->
                remainingTime = newTime
            }
        }

        // The onDispose block will be called when the composable leaves the composition
        onDispose {
            coroutineScope.cancel() // Cancels all coroutines launched in this scope
        }
    }

    // Display the remaining time
    Text(text = "Expires in: $remainingTime")
}

fun formatRemainingTime(millisUntilFinished: Long): String {
    val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
    val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
    return String.format(Locale.getDefault(), "%02d:%02d:%02d:%02d", days, hours, minutes, seconds)
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


