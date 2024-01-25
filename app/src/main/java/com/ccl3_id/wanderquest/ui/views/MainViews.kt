package com.ccl3_id.wanderquest.ui.views

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.unit.IntOffset
import com.ccl3_id.wanderquest.data.models.rooms.Room
import kotlinx.coroutines.coroutineScope
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.StrokeCap
import com.ccl3_id.wanderquest.data.models.entities.Enemy
import com.ccl3_id.wanderquest.ui.composables.ButtonSettings
import com.ccl3_id.wanderquest.ui.composables.MultiStyleText
import com.ccl3_id.wanderquest.ui.composables.WanderButton
import com.ccl3_id.wanderquest.ui.theme.RobotoFontFamily
import kotlin.math.abs

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

        topBar = {
            WanderQuestAppBar(mainViewModel,navController)
        },

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
                //mainViewModel.getPlayer()
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
                //mainViewModel.getPlayer();
                mainViewModel.getOpenDungeons()
                mainViewModel.getActiveDungeons()
                displayDungeons(mainViewModel)
                //displayBattleScreen(mainViewModel);
                //ScrollableCanvasWithRectangles()
            }
        }
    }
}

@Composable
fun WanderQuestAppBar(mainViewModel: MainViewModel, navController : NavHostController){

    val state = mainViewModel.mainViewState.collectAsState()
    val player = state.value.selectedPlayer

    TopAppBar(
        title = {

        },
        navigationIcon = {

        },
        actions = {

            if(player != null){
                Text(text =  "HP ${player.entityCurrentHealth}/${player.entityMaxHealth}", fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, color = Color.White, modifier = Modifier.padding(10.dp))

            }

            val imageResource = painterResource(id = R.drawable.doorway)

            Image(
                painter = imageResource,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(4.dp)
                    .aspectRatio(1f)
                    .clickable {
                        mainViewModel.selectScreen(Screen.Character)
                        navController.navigate(Screen.Character.route)
                    }
            )
        },
        backgroundColor = MaterialTheme.colorScheme.secondary
    )
}


@Composable
fun ScrollableCanvasWithRectangles(mainViewModel: MainViewModel) {

    val state = mainViewModel.mainViewState.collectAsState()
    val dungeonRooms = state.value.dungeonRooms
    val adjacentRooms = state.value.adjacentRooms
    val selectedRoom = state.value.currentSelectedRoom


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

    dungeonRooms!!.forEach { row ->
        row.forEach { room ->
            if (room != null) {
                val topLeft =  with(density) {Offset(room.xIndex * Room.SLOT_SIZE.dp.toPx() + room.randomX.dp.toPx(), room.yIndex * Room.SLOT_SIZE.dp.toPx()  + room.randomY.dp.toPx())}
                room.centerPos = Offset(topLeft.x + rectSize / 2, topLeft.y + rectSize / 2)
                if(selectedRoom != null){
                    if(room.xIndex == selectedRoom.xIndex && room.yIndex == selectedRoom.yIndex){
                        selectedRoom.centerPos = room.centerPos
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = "initialScroll") {
        with(density) {
            var middleX : Float = 0f;
            var middleY : Float = 0f;
            val screenWidth = configuration.screenWidthDp.dp.toPx()
            val screenHeight = configuration.screenHeightDp.dp.toPx()
            val viewportCenterX = screenWidth / 2
            val viewportCenterY = screenHeight / 2

            if(selectedRoom == null){
                middleX = (canvasSize.toPx() - screenWidth) / 2
                middleY = (canvasSize.toPx() - screenHeight) / 2
            }else{
                middleX = selectedRoom.centerPos!!.x - viewportCenterX
                middleY = selectedRoom.centerPos!!.y - viewportCenterY
            }

            horizontalScrollState.scrollTo(middleX.toInt())
            verticalScrollState.scrollTo(middleY.toInt())
        }
    }

    var lineColor = MaterialTheme.colorScheme.secondary
    var selectedColor = Color(0f, 0f, 0f, 0.25f)
    var adjacentRoomColor = MaterialTheme.colorScheme.onSurface
    var unexploredRoomColor = MaterialTheme.colorScheme.surface
    var startingRoomColor = Color(0xFF6AC8E4)
    var itemRoomColor = Color(0xFF00C2A2)
    var monsterRoomColor = Color(0xFFFF859A)
    var emptyRoomColor = Color(0xFF6AC8E4)

    Column {
        Canvas(modifier = canvasModifier
            .fillMaxSize()
            .weight(1f)
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    coroutineScope.launch {
                        horizontalScrollState.scrollBy(-dragAmount.x)
                        verticalScrollState.scrollBy(-dragAmount.y)
                    }
                }
            }
            .pointerInput(Unit) {
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
                                    mainViewModel.selectRoom(room)
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
                                color = lineColor,
                                start = currentRoom!!.centerPos!!,
                                end = dungeonRooms!![i][j + 1]!!.centerPos!!,
                                strokeWidth = 30f
                            )
                        }
                        // Draw line below
                        if (i < dungeonRooms!!.lastIndex && dungeonRooms!![i + 1][j] != null) {
                            drawLine(
                                color = lineColor,
                                start = currentRoom!!.centerPos!!,
                                end = dungeonRooms!![i + 1][j]!!.centerPos!!,
                                strokeWidth = 30f
                            )
                        }
                    }
                }
            }

            if(state.value.currentSelectedRoom != null){
                val selectSize = 60
                val center = state.value.currentSelectedRoom!!.centerPos!!
                val topLeft = Offset((center.x - rectSize / 2)-selectSize/2, (center.y - rectSize / 2)-selectSize/2)

                drawRoundRect(
                    color = selectedColor,
                    topLeft = topLeft,
                    size = Size(rectSize+selectSize, rectSize+selectSize),
                    cornerRadius = CornerRadius(10.dp.toPx())
                )

            }


            dungeonRooms!!.forEach { row ->
                row.forEach { room ->
                    if (room != null) {
                        val center = room.centerPos!!
                        val topLeft = Offset(center.x - rectSize / 2, center.y - rectSize / 2)

                        var color = startingRoomColor

                        if(room.hasBeenVisited){
                            when (room.roomType) {
                                "Monster" -> color = monsterRoomColor
                                "Item" -> color = itemRoomColor
                                "Empty" -> color = emptyRoomColor
                            }
                        }else{
                            if(adjacentRooms.find { adjacentRoom: Room -> adjacentRoom.xIndex == room.xIndex && adjacentRoom.yIndex == room.yIndex } != null){
                                color = adjacentRoomColor
                            }else{
                                color = unexploredRoomColor
                            }
                        }

                        drawRoundRect(
                            color = color,
                            topLeft = topLeft,
                            size = Size(rectSize, rectSize),
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                }
            }
        })

        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.Bottom

        ) {

            WanderButton(
                text = "Enter Room",
                color = MaterialTheme.colorScheme.primary,
                onClickEvent = {
                    mainViewModel.openDungeonDialog()
                },
                fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                textColor = Color.White,
                enabled = mainViewModel.checkRoomEnabled()
            )

            WanderButton(
                text = "Leave Dungeon",
                color = MaterialTheme.colorScheme.tertiary,
                onClickEvent = {
                    mainViewModel.leaveDungeon()
                },
                fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
                textColor = Color.White,
            )

        }

    }

    Column {
        displayPopUpDungeon(mainViewModel)
    }
}


@Composable
fun displayPopUpDungeon(mainViewModel: MainViewModel) {

    val state = mainViewModel.mainViewState.collectAsState()
    val currentDungeon = state.value.currentSelectedRoom

    if(state.value.displayDungeonPopUp && currentDungeon != null){

        if(currentDungeon.hasBeenVisited){
            alreadyVisited(mainViewModel)
        }else{
            displayRoomContents(mainViewModel)
        }

    }
}

@Composable
fun alreadyVisited(mainViewModel: MainViewModel){


    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(10.dp),
        onDismissRequest = {
            mainViewModel.dismissDialog()
        },
        title = {
            Text(text = "Already Visited", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
        },
        text = {
            Column {
                Text(text = "You already visited this room!", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
            }
        },
        confirmButton = {
            WanderButton(
                text = "Okay",
                color = MaterialTheme.colorScheme.primary,
                onClickEvent = {
                    mainViewModel.dismissDialog()
                },
                fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                textColor = Color.White,
                modifier = Modifier
            )

        }

    )
}

@Composable
fun displayRoomContents(mainViewModel: MainViewModel){

    val state = mainViewModel.mainViewState.collectAsState()
    val currentDungeon = state.value.currentSelectedRoom
    val roomContent = currentDungeon?.roomContents


    if(roomContent != null){
        when (roomContent) {
            is Item -> displayRoomItem(roomContent, mainViewModel)// handle Item
            is Enemy -> displayRoomMonster(roomContent, mainViewModel)// handle Monster
            else -> throw IllegalArgumentException("Unknown type")
        }
    }else{
        displayRoomNothing(mainViewModel)
    }
}
@Composable
fun displayRoomMonster(monster : Enemy, mainViewModel: MainViewModel) {

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(10.dp),
        onDismissRequest = {
            mainViewModel.dismissDialog()
        },
        title = {
            Text(text = "Monster Encounter", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
        },
        text = {
            Column {
                Text(text = "You encountered a Monster", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
                Text(text = "${monster.entityName}", color = MaterialTheme.colorScheme.primary, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
            }
        },
        confirmButton = {
            WanderButton(
                text = "Okay",
                color = MaterialTheme.colorScheme.primary,
                onClickEvent = {
                    mainViewModel.startBattle(monster.copy())
                    mainViewModel.dismissDialog()
                },
                fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                textColor = Color.White,
                modifier = Modifier
            )

        }

    )

}

@Composable
fun displayRoomItem(item: Item, mainViewModel: MainViewModel) {

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(10.dp),
        onDismissRequest = {
            mainViewModel.dismissDialog()
        },
        title = {
            Text(text = "Item Encounter", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
        },
        text = {
            Column {
                Text(text = "You found an Item!", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
                MultiStyleText("Name: ", Color.White, item.name, MaterialTheme.colorScheme.primary, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, Modifier)
                MultiStyleText("Type: ", Color.White, item.type, MaterialTheme.colorScheme.primary, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, Modifier)

                val itemStats = item.getStats()
                if (itemStats != null) {
                    //TODO make more fancy
                    Text(
                        text = "Stat Buffs:", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM
                    )
                    itemStats.statBuffs.entries.forEach { (k,v) ->
                        MultiStyleText("$k: ", MaterialTheme.colorScheme.onSurface, "$v" , MaterialTheme.colorScheme.primary, fontSize = 18.sp, Modifier.padding(start = 20.dp))
                    }
                    if(itemStats.abilities != " "){
                        MultiStyleText("Abilities: ", Color.White, itemStats.abilities, MaterialTheme.colorScheme.primary, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, Modifier)
                    }

                    if(itemStats.statNerfs.entries.filter { (k,v) -> k != " "}.isNotEmpty()){
                        Text(
                            text = "Stat Nerfs: ", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM
                        )
                        itemStats.statNerfs.entries.forEach { (k,v) ->
                            if(k != " "){
                                MultiStyleText("$k: ", MaterialTheme.colorScheme.onSurface, "$v" , MaterialTheme.colorScheme.primary, fontSize = 18.sp, Modifier.padding(start = 20.dp))
                            }
                        }
                    }

                } else {
                    Text(text = "No additional stats available.", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
                }

            }
        },
        confirmButton = {
            WanderButton(
                text = "Okay",
                color = MaterialTheme.colorScheme.primary,
                onClickEvent = {
                    mainViewModel.getItem(item)
                    mainViewModel.completeRoom()
                    mainViewModel.dismissDialog()
                },
                fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                textColor = Color.White,
                modifier = Modifier
            )

        }

    )
}

@Composable
fun displayRoomNothing(mainViewModel: MainViewModel) {

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(10.dp),
        onDismissRequest = {
            mainViewModel.dismissDialog()
        },
        title = {
            Text(text = "Nothing", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
        },
        text = {
            Column {
                Text(text = "You find nothing of interest in this room.", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
            }
        },
        confirmButton = {
            WanderButton(
                text = "Okay",
                color = MaterialTheme.colorScheme.primary,
                onClickEvent = {
                    mainViewModel.completeRoom()
                    mainViewModel.dismissDialog()
                },
                fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                textColor = Color.White,
                modifier = Modifier
            )

        }

    )
}


@Composable
fun BottomNavigationBar(navController: NavHostController, selectedScreen: Screen, mainViewModel: MainViewModel){
    BottomNavigation (
        backgroundColor = MaterialTheme.colorScheme.secondary
    ) {
        NavigationBarItem(
            selected = (selectedScreen == Screen.Character),
            colors = androidx.compose.material3.NavigationBarItemDefaults
                .colors(selectedIconColor = MaterialTheme.colorScheme.primary,  indicatorColor = MaterialTheme.colorScheme.secondary),
            onClick = {
                mainViewModel.selectScreen(Screen.Character);
                navController.navigate(Screen.Character.route) },
            icon = { Icon(painter = painterResource(id = R.drawable.weight_lifting_up), contentDescription = "Character Screen",
                tint = if (selectedScreen == Screen.Character) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(42.dp)) })

        NavigationBarItem(
            selected = (selectedScreen == Screen.Items),
            colors = androidx.compose.material3.NavigationBarItemDefaults
                .colors(selectedIconColor = MaterialTheme.colorScheme.primary, indicatorColor = MaterialTheme.colorScheme.secondary),
            onClick = {
                mainViewModel.selectScreen(Screen.Items);
                navController.navigate(Screen.Items.route)
                      },
            icon = { Icon(painter = painterResource(id = R.drawable.backpack), contentDescription = "Item Screen",  tint = if (selectedScreen == Screen.Items) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(42.dp)) })

        NavigationBarItem(
            selected = (selectedScreen == Screen.Dungeon),
            colors = androidx.compose.material3.NavigationBarItemDefaults
                .colors(selectedIconColor = MaterialTheme.colorScheme.primary, indicatorColor = MaterialTheme.colorScheme.secondary),
            onClick = {
                mainViewModel.selectScreen(Screen.Dungeon);
                navController.navigate(Screen.Dungeon.route) },
            icon = { Icon( painter = painterResource(id = R.drawable.doorway), contentDescription = "Dungeon Screen", tint = if (selectedScreen == Screen.Dungeon) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(42.dp)) })
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
        Spacer(modifier = Modifier.height(4.dp))

        displayPlayerEquipedItems(itemViewModel)

        Spacer(modifier = Modifier.height(4.dp))

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
            .size(80.dp)
            .aspectRatio(1f)
            .background(
                MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
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

    Text(text = "Items:", fontSize = ButtonSettings.BUTTON_FONT_SIZE_MASSIVE, color = Color.White)

    // Display items
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(8.dp),
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
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(10.dp),
        onDismissRequest = {
            itemViewModel.deselectItem()
        },
        title = {
            Text(text = "Item Info", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG)
        },
        text = {

            Column{
                MultiStyleText("Name: ", Color.White, clickedItem.name, MaterialTheme.colorScheme.primary, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, Modifier)
                MultiStyleText("Type: ", Color.White, clickedItem.type, MaterialTheme.colorScheme.primary, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, Modifier)

                if (itemStats != null) {
                    //TODO make more fancy
                    Text(
                        text = "Stat Buffs:", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM
                    )
                    itemStats.statBuffs.entries.forEach { (k,v) ->
                        MultiStyleText("$k: ", MaterialTheme.colorScheme.onSurface, "$v" , MaterialTheme.colorScheme.primary, fontSize = 18.sp, Modifier.padding(start = 20.dp))
                    }
                    if(itemStats.abilities != " "){
                        MultiStyleText("Abilities: ", Color.White, itemStats.abilities, MaterialTheme.colorScheme.primary, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, Modifier)
                    }

                    if(itemStats.statNerfs.entries.filter { (k,v) -> k != " "}.isNotEmpty()){
                        Text(
                            text = "Stat Nerfs: ", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM
                        )
                        itemStats.statNerfs.entries.forEach { (k,v) ->
                            if(k != " "){
                                MultiStyleText("$k: ", MaterialTheme.colorScheme.onSurface, "$v" , MaterialTheme.colorScheme.primary, fontSize = 18.sp, Modifier.padding(start = 20.dp))
                            }
                        }
                    }

                } else {
                    Text(text = "No additional stats available.", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
                }
            }
        },
        confirmButton = {
            WanderButton(
                text = "Equip",
                color = MaterialTheme.colorScheme.primary,
                onClickEvent = {
                    itemViewModel.equipItem(clickedItem, player)
                    itemViewModel.deselectItem()
                },
                fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                textColor = Color.White,
                modifier = Modifier
            )

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
        title = {
            Text(text = "Item Info", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG)
        },
        text = {

            Column{
                MultiStyleText("Name: ", Color.White, clickedItem.name, MaterialTheme.colorScheme.primary, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, Modifier)
                MultiStyleText("Type: ", Color.White, clickedItem.type, MaterialTheme.colorScheme.primary, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, Modifier)

                if (itemStats != null) {
                    //TODO make more fancy
                    Text(
                        text = "Stat Buffs:", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM
                    )
                    itemStats.statBuffs.entries.forEach { (k,v) ->
                        MultiStyleText("$k: ", MaterialTheme.colorScheme.onSurface, "$v" , MaterialTheme.colorScheme.primary, fontSize = 18.sp, Modifier.padding(start = 20.dp))
                    }
                    if(itemStats.abilities != " "){
                        MultiStyleText("Abilities: ", Color.White, itemStats.abilities, MaterialTheme.colorScheme.primary, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, Modifier)
                    }

                    if(itemStats.statNerfs.entries.filter { (k,v) -> k != " "}.isNotEmpty()){
                        Text(
                            text = "Stat Nerfs: ", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM
                        )
                        itemStats.statNerfs.entries.forEach { (k,v) ->
                            if(k != " "){
                                MultiStyleText("$k: ", MaterialTheme.colorScheme.onSurface, "$v" , MaterialTheme.colorScheme.primary, fontSize = 18.sp, Modifier.padding(start = 20.dp))
                            }
                        }
                    }

                } else {
                    Text(text = "No additional stats available.", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "You have already equipped an item of this type. Would you like to replace it?", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
            }
        },
        confirmButton = {

            WanderButton(
                text = "Replace",
                color = MaterialTheme.colorScheme.primary,
                onClickEvent = {
                    itemViewModel.replaceEquippedItem(clickedItem, player)
                    itemViewModel.deselectItem()
                },
                fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                textColor = Color.White,
                modifier = Modifier
            )
        },
        dismissButton = {
            WanderButton(
                text = "Cancel",
                color = MaterialTheme.colorScheme.tertiary,
                onClickEvent = {
                    itemViewModel.deselectItem()
                },
                fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                textColor = Color.White,
                modifier = Modifier
            )
        }
    )
}

@Composable
fun EquippedItemCard(equippedItem: Item, itemViewModel: ItemViewModel){
    Column(
        modifier = Modifier
            .size(80.dp)
            .clickable { itemViewModel.selcetEquippedItem(equippedItem) }
            .padding(4.dp)
            .aspectRatio(1f)
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
                .size(80.dp)
        )
    }
}


@Composable
fun displayPlayerEquipedItems(itemViewModel: ItemViewModel){
    val itemState = itemViewModel.mainViewState.collectAsState()
    val equippedItems = itemState.value.equippedItemSlots

    Text(text = "Equipped Items:", fontSize = ButtonSettings.BUTTON_FONT_SIZE_MASSIVE, color = Color.White)

    // Display equipped items
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(8.dp),
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


    if(mainViewState.value.equippedItemClicked &&  mainViewState.value.clickedEquippedItem != null){
        val clickedItem = mainViewState.value.clickedEquippedItem!!

        val itemStats = clickedItem.getStats() // Get parsed stats
        AlertDialog(
            onDismissRequest = {
                itemViewModel.deselectItem()
            },
            containerColor = MaterialTheme.colorScheme.background,
            title = {
                Text(text = "Item Info", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG)
            },
            text = {
                Column {
                    MultiStyleText("Name: ", Color.White, clickedItem.name, MaterialTheme.colorScheme.primary, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, Modifier)
                    MultiStyleText("Type: ", Color.White, clickedItem.type, MaterialTheme.colorScheme.primary, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, Modifier)

                    if (itemStats != null) {
                        //TODO make more fancy
                        Text(
                            text = "Stat Buffs:", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM
                        )
                        itemStats.statBuffs.entries.forEach { (k,v) ->
                            MultiStyleText("$k: ", MaterialTheme.colorScheme.onSurface, "$v" , MaterialTheme.colorScheme.primary, fontSize = 18.sp, Modifier.padding(start = 20.dp))
                        }
                        if(itemStats.abilities != " "){
                            MultiStyleText("Abilities: ", Color.White, itemStats.abilities, MaterialTheme.colorScheme.primary, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, Modifier)
                        }
                        val test = itemStats.statNerfs.entries.filter { (k,v) -> k != " "}
                        if(itemStats.statNerfs.entries.filter { (k, v) -> k != " " }.isNotEmpty()){
                            Text(
                                text = "Stat Nerfs: ", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM
                            )
                            itemStats.statNerfs.entries.forEach { (k,v) ->
                                if(k != " "){
                                    MultiStyleText("$k: ", MaterialTheme.colorScheme.onSurface, "$v" , MaterialTheme.colorScheme.primary, fontSize = 18.sp, Modifier.padding(start = 20.dp))
                                }
                            }
                        }

                    } else {
                        Text(text = "No additional stats available.", color = Color.White, fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM)
                    }
                }
            },
            confirmButton = {
                WanderButton(
                    text = "Unequip",
                    color = MaterialTheme.colorScheme.primary,
                    onClickEvent = {
                        itemViewModel.unequipItem(mainViewState.value.clickedEquippedItem!!, mainMainViewState.value.selectedPlayer!!)
                        itemViewModel.deselectItem()
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                    textColor = Color.White,
                    modifier = Modifier
                )
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
            .padding(20.dp)
    ) {
        if (player != null) {

            displayPlayerInfo(player);

            Divider(color = MaterialTheme.colorScheme.secondary, thickness = 2.dp, modifier = Modifier.padding(4.dp,16.dp))

            displayPlayerEquipedItems(itemViewModel)

            Divider(color = MaterialTheme.colorScheme.secondary, thickness = 2.dp, modifier = Modifier.padding(4.dp,16.dp))

            displayPlayerStats(player, itemViewModel);

            Divider(color = MaterialTheme.colorScheme.secondary, thickness = 2.dp, modifier = Modifier.padding(4.dp,16.dp))

            displayPlayerAbilities(player);

            Divider(color = MaterialTheme.colorScheme.secondary, thickness = 2.dp, modifier = Modifier.padding(4.dp,16.dp))

        }

        WanderButton(
            text = "Logout",
            color = MaterialTheme.colorScheme.primary,
            onClickEvent = {
                val intent = Intent(context, LoginActivity::class.java);
                context.startActivity(intent);
            },
            fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
            textColor = Color.White
        )

    }
    Column {
        EquippedItemPopUp(itemViewModel, mainViewModel)
    }
}

@Composable
fun displayPlayerInfo(player: Player) {

    val context = LocalContext.current
    val imageResource = painterResource(id = R.drawable.doorway)


    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = imageResource,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .weight(0.4f)
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier
            .weight(0.1f))

        Column(modifier = Modifier
            .weight(0.5f)
            .fillMaxWidth()) {
            Text(text = player.playerName, fontSize = 36.sp, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 4.dp))
            Text( text = "Level: "+player.playerLevel, fontSize = 18.sp, color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
            Text( text = "Class: "+player.playerClass, fontSize = 18.sp, color = Color.White,  modifier = Modifier.padding(bottom = 4.dp))
        }

    }
    
    Spacer(modifier = Modifier.height(6.dp))
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        LinearProgressIndicator(progress = player.entityCurrentHealth.toFloat()/player.entityMaxHealth.toFloat(), modifier = Modifier
            .fillMaxWidth()
            .height(24.dp), color = MaterialTheme.colorScheme.primary,strokeCap = StrokeCap.Round)
        Text(text =  "HP ${player.entityCurrentHealth}/${player.entityMaxHealth}", fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, color = Color.White, modifier = Modifier.padding(10.dp))
    }


    //player.playerCurrentXP >= player.playerXPToNextLevel
    if(player.playerCurrentXP >= player.playerXPToNextLevel){

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            LinearProgressIndicator(progress =  player.playerCurrentXP.toFloat() / player.playerXPToNextLevel.toFloat(), modifier = Modifier
                .fillMaxWidth()
                .height(24.dp), color = Color(0xFF6AC8E4),strokeCap = StrokeCap.Round)
            Text(text =  "XP ${player.playerXPToNextLevel}/${player.playerXPToNextLevel}", fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, color = Color.White, modifier = Modifier.padding(10.dp))
        }

    }else{
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            LinearProgressIndicator(progress =  player.playerCurrentXP.toFloat() / player.playerXPToNextLevel.toFloat(), modifier = Modifier
                .fillMaxWidth()
                .height(24.dp), color = Color(0xFF6AC8E4),strokeCap = StrokeCap.Round)
            Text(text =  "XP ${player.playerCurrentXP}/${player.playerXPToNextLevel}", fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, color = Color.White, modifier = Modifier.padding(10.dp))
        }
    }

    if(player.playerCurrentXP >= player.playerXPToNextLevel) {
        WanderButton(
            text = "Level Up",
            color = MaterialTheme.colorScheme.primary,
            onClickEvent = {
                val intent =
                    Intent(context, LevelUpActivity::class.java); context.startActivity(intent);
            },
            fontSize = ButtonSettings.BUTTON_FONT_SIZE_BIG,
            textColor = Color.White,
            modifier = Modifier.fillMaxWidth().padding(top=10.dp)
        )
    }

}

@Composable
fun EmptySlotPlaceholder() {
    Column(
        modifier = Modifier
            .size(80.dp)
            .padding(4.dp)
            .aspectRatio(1f)
            .background(
                MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(10.dp)
            )
    ) {

    }
}

@Composable
fun displayPlayerStats(player: Player, itemViewModel: ItemViewModel){
    // Custom styles
    val cardBackgroundColor = MaterialTheme.colorScheme.secondary
    val textColor = MaterialTheme.colorScheme.onSurface
    val statNameStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
    val statValueStyle = TextStyle(fontSize = 18.sp, color = textColor)

    Text(text = "Statistics:", fontSize = ButtonSettings.BUTTON_FONT_SIZE_MASSIVE, color = Color.White)

    Column(modifier = Modifier.padding(4.dp)) {
        val equippedItems = itemViewModel.mainViewState.collectAsState().value.equippedItemSlots
        Player.STAT_LIST.forEach {stat ->

            val baseValue = player.playerStats[stat] ?: 0
            var totalBuffNerf = 0

            equippedItems.values.filterNotNull().forEach { item ->
                item.updateItemStatsFromJSON()
                totalBuffNerf += item.itemStats.getOrDefault(stat, 0)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(vertical = 8.dp)
            ){
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(
                            color = cardBackgroundColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .weight(0.5f)
                        .fillMaxHeight()
                        .padding(10.dp)
                ) {
                    Text(text = stat, fontSize = 24.sp, color = Color.White, modifier = Modifier.padding(4.dp))
                 }
                
                Spacer(modifier = Modifier.width(10.dp))
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(
                            color = cardBackgroundColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .weight(0.4f)
                        .fillMaxHeight()
                        .padding(10.dp)
                ) {

                    if (totalBuffNerf == 0){
                        Text(text = "$baseValue", fontSize = 24.sp, color = Color.White, modifier = Modifier.padding(4.dp))

                    }else{

                        if(totalBuffNerf < 0){
                            MultiStyleText("$baseValue ", Color.White, "- "+abs(totalBuffNerf), Color(0xFFF95B78), fontSize = 24.sp , modifier = Modifier.padding(4.dp))
                        }else{
                            MultiStyleText("$baseValue ", Color.White, "+ "+abs(totalBuffNerf), Color(0xFF00C2A2), fontSize = 24.sp, modifier = Modifier.padding(4.dp))
                        }
                    }
                }

            }

        }


        /*
        Player.STAT_LIST.chunked(3).forEachIndexed { index, chunkedStats ->
            Row(
                horizontalArrangement = if (index == 0) Arrangement.SpaceBetween else Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
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
                            .weight(1f),
                        shape = RoundedCornerShape(10.dp)
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
        */
    }
}


@Composable
fun displayPlayerAbilities(player: Player){

    Text(text = "Abilities:", fontSize = ButtonSettings.BUTTON_FONT_SIZE_MASSIVE, color = Color.White)

    Spacer(modifier = Modifier.height(12.dp))

    Column(modifier = Modifier.fillMaxWidth()) {

        Column(modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(
                    topStart = 10.dp,
                    topEnd = 10.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                )
            )
            .padding(14.dp)) {
            Text(text = player.abilityOneName, fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 10.dp,
                    bottomEnd = 10.dp,
                    bottomStart = 10.dp
                )
            )
            .padding(14.dp)) {
            Text(text = player.abilityOneDescription, fontSize = 24.sp, color = Color.White)
        }

    }

    Spacer(modifier = Modifier.height(24.dp))
    
    Column(modifier = Modifier.fillMaxWidth()) {

        Column(modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(
                    topStart = 10.dp,
                    topEnd = 10.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                )
            )
            .padding(14.dp)) {
            Text(text = player.abilityTwoName, fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 10.dp,
                    bottomEnd = 10.dp,
                    bottomStart = 10.dp
                )
            )
            .padding(14.dp)) {
            Text(text = player.abilityTwoDescription, fontSize = 24.sp, color = Color.White)
        }

    }

    Spacer(modifier = Modifier.height(24.dp))

    Column(modifier = Modifier.fillMaxWidth()) {

        Column(modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(
                    topStart = 10.dp,
                    topEnd = 10.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                )
            )
            .padding(14.dp)) {
            Text(text = player.abilityThreeName, fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 10.dp,
                    bottomEnd = 10.dp,
                    bottomStart = 10.dp
                )
            )
            .padding(14.dp)) {
            Text(text = player.abilityThreeDescription, fontSize = 24.sp, color = Color.White)
        }

    }

    Spacer(modifier = Modifier.height(24.dp))

    Column(modifier = Modifier.fillMaxWidth()) {

        Column(modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(
                    topStart = 10.dp,
                    topEnd = 10.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                )
            )
            .padding(14.dp)) {
            Text(text = player.abilityFourName, fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 10.dp,
                    bottomEnd = 10.dp,
                    bottomStart = 10.dp
                )
            )
            .padding(14.dp)) {
            Text(text = player.abilityFourDescription, fontSize = 24.sp, color = Color.White)
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
                .padding(10.dp)
        ) {

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Active Dungeons - ${activeDungeon.size}/3",
                    fontSize = 24.sp,
                    color = Color.White
                )
            }

            displayActiveDungeons(activeDungeon, mainViewModel);

            Text(
                text = "Open Dungeons - 7",
                fontSize =  24.sp,
                color = Color.White
            )

            displayOpenDungeons(openDungeon, mainViewModel);

        }
    }else{
        //state.value.battleStarted
        if(state.value.battleStarted){
            displayBattleContent(mainViewModel);
        }else{
            ScrollableCanvasWithRectangles(mainViewModel)
        }

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

    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
            .background(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column( modifier = Modifier
            .weight(1f)
            .padding(10.dp)){

            Text(text = dungeon.dungeonName, fontSize = 20.sp, color = Color.White)
            Text(text = "Distance: ${dungeon.displayTotalDistance()}", fontSize = 15.sp, color = Color.White)
            CountdownTimer(convertDateStringToMillis(dungeon.dungeonExpiresIn),dungeon.id)
        }

        WanderButton(
            text = "Add",
            color = MaterialTheme.colorScheme.primary,
            onClickEvent = {
                mainViewModel.enterDungeon(dungeon,context)
            },
            fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
            textColor = Color.White,
            modifier = Modifier.padding(end = 10.dp),
            enabled = activeDungeonNumber < 3
        )
    }
}

@Composable
fun ActiveDungeonItem(dungeon: Dungeon? = null, mainViewModel: MainViewModel) {

    var color = MaterialTheme.colorScheme.secondary

    if(dungeon != null){
        color = MaterialTheme.colorScheme.onSecondaryContainer
    }


    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
            .background(
                color = color,
                shape = RoundedCornerShape(10.dp)
            )
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if(dungeon != null){

            Column( modifier = Modifier
                .weight(1f)
                .padding(10.dp)){
                Text(text = dungeon.dungeonName, fontSize = 20.sp, color = Color.White)
                Text(text = "Distance: ${dungeon.displayTotalDistance()}", fontSize = 15.sp, color = Color.White)
                if(dungeon.dungeonWalkedDistance < dungeon.dungeonTotalDistance){
                    //TODO change so 1.750 km --> 1.7 km
                    Text(text = "Walked:  ${dungeon.displayWalkedDistance()}", fontSize = 15.sp, color = Color.White)
                }else{
                    Text(text = "", fontSize = 16.sp, color = Color.White)
                }
            }
            //TODO add Dialog if you are sure
            IconButton(onClick = { mainViewModel.deleteDungeon(dungeon) }, modifier = Modifier.padding(10.dp)) {
                Icon(Icons.Default.Delete, "Delete", tint =  MaterialTheme.colorScheme.onSecondary, modifier = Modifier.size(32.dp))
            }
            if(dungeon.dungeonWalkedDistance >= dungeon.dungeonTotalDistance){


                WanderButton(
                    text = "Enter",
                    color = MaterialTheme.colorScheme.primary,
                    onClickEvent = {
                        mainViewModel.selectDungeon(dungeon)
                    },
                    fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                    textColor = Color.White,
                    modifier = Modifier.padding(end = 10.dp)
                )

            }
        }else{
            Column( modifier = Modifier
                .weight(1f)
                .padding(10.dp)){
                Text(text = "", fontSize = 20.sp, color = Color.White)
                Text(text = "", fontSize = 15.sp, color = Color.White)
                Text(text = "", fontSize = 15.sp, color = Color.White)
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
    Text(text = "Expires in: $remainingTime", fontSize = 15.sp, color = Color.White)
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
    //val enemy = Enemy("Monster")
    val player = state.value.selectedPlayer;

    val enemyText =  state.value.enemyText;
    val playerText = state.value.playerText;

    val enemyCurrentHealth = state.value.currentEnemyHealth;
    val battleCompleteText = state.value.battleCompleteText;

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Spacer(modifier = Modifier.weight(1f))

        // Enemy Section (Aligned Right)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.End, // Aligns content to the right
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier
                .fillMaxWidth()) {

                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text =  "${enemy!!.entityName}", fontSize = 50.sp, color = Color.White, modifier = Modifier.padding(bottom = 10.dp))

                    //Enemy Image
                    Box(modifier = Modifier
                        .size(150.dp, 150.dp)
                        .background(Color.Gray)
                        .padding(bottom = 10.dp))

                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LinearProgressIndicator(progress = (enemyCurrentHealth.toFloat()/enemy!!.entityMaxHealth.toFloat()).toFloat(), modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp), color = Color(0xFFF95B78),strokeCap = StrokeCap.Round)
                    Text(text =  "$enemyCurrentHealth/${enemy!!.entityMaxHealth}", fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, color = Color.White, modifier = Modifier.padding(10.dp))
                }
            }
        }

        Column( modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(10.dp)
            )) {

            if(!state.value.battleComplete){
                Text(text = "$enemyText", fontSize =  ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, color = Color.White, modifier = Modifier.padding(8.dp))
                Text(text = "$playerText",  fontSize =  ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, color = Color.White, modifier = Modifier.padding(8.dp))
            }else{
                Text(text = "$battleCompleteText",  fontSize =  ButtonSettings.BUTTON_FONT_SIZE_MEDIUM, color = Color.White, modifier = Modifier.padding(16.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(5.dp)
                ) {
                    WanderButton(
                        text = player!!.abilityOneName,
                        color = MaterialTheme.colorScheme.primary,
                        onClickEvent = {
                            mainViewModel.useAbility(1)
                        },
                        fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                        textColor = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(5.dp),
                        enabled = !state.value.battleComplete
                    )
                    WanderButton(
                        text = player!!.abilityTwoName,
                        color = MaterialTheme.colorScheme.primary,
                        onClickEvent = {
                            mainViewModel.useAbility(2)
                        },
                        fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                        textColor = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(5.dp),
                        enabled = !state.value.battleComplete
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    WanderButton(
                        text = player!!.abilityThreeName,
                        color = MaterialTheme.colorScheme.primary,
                        onClickEvent = {
                            mainViewModel.useAbility(3)
                        },
                        fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                        textColor = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(5.dp),
                        enabled = !state.value.battleComplete
                    )
                    WanderButton(
                        text = player!!.abilityFourName,
                        color = MaterialTheme.colorScheme.primary,
                        onClickEvent = {
                            mainViewModel.useAbility(4)
                        },
                        fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                        textColor = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(5.dp),
                        enabled = !state.value.battleComplete
                    )
                }
            }
            WanderButton(
                text = "Leave Battle",
                color = MaterialTheme.colorScheme.tertiary,
                onClickEvent = {
                    mainViewModel.leaveBattle();
                },
                fontSize = ButtonSettings.BUTTON_FONT_SIZE_MEDIUM,
                textColor = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


