package com.ccl3_id.wanderquest.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccl3_id.wanderquest.data.DatabaseHandler
import com.ccl3_id.wanderquest.data.models.dungeons.Dungeon
import com.ccl3_id.wanderquest.data.models.entities.Enemy
import com.ccl3_id.wanderquest.data.models.items.Item
import com.ccl3_id.wanderquest.data.models.rooms.Room
import com.ccl3_id.wanderquest.repository.LocationRepository
import com.ccl3_id.wanderquest.ui.views.Screen
import com.ccl3_id.wanderquest.viewModels.states.MainViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.random.Random


class MainViewModel (val db: DatabaseHandler, private val locationRepository: LocationRepository) : ViewModel() {

    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()

    init {
        startPeriodicDistanceCheck()
    }

    fun getPlayer(){
        _mainViewState.update { it.copy(selectedPlayer = db.getSelectedPlayer()) }
    }

    fun getOpenDungeons(){
        checkExpiredDungeon();
        _mainViewState.update { it.copy(allOpenDungeons = db.getOpenDungeons(_mainViewState.value.selectedPlayer!!.id)) }
    }

    fun getActiveDungeons(){
        _mainViewState.update { it.copy(allActiveDungeon = db.getActiveDungeons(_mainViewState.value.selectedPlayer!!.id)) }
    }


    fun selectScreen(screen: Screen){
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }

    fun startBattle(monster: Enemy) {
        _mainViewState.value.selectedPlayer!!.getNewHealth();
        _mainViewState.update { it.copy(enemy = monster) }
        _mainViewState.update { it.copy(battleStarted = true) }
        _mainViewState.update { it.copy(battleCompleteText = "")}
        _mainViewState.update { it.copy(playerText = "") };
        _mainViewState.update { it.copy(enemyText = "") };
        _mainViewState.update { it.copy(currentEnemyHealth =  _mainViewState.value.enemy!!.entityCurrentHealth) }
        _mainViewState.update { it.copy(currentPlayerHealth =  _mainViewState.value.selectedPlayer!!.entityCurrentHealth) }
    }

    fun leaveBattle(){
        _mainViewState.value.selectedPlayer!!.getNewHealth();
        _mainViewState.update { it.copy(battleComplete = false) }
        _mainViewState.update { it.copy(battleStarted = false) }
    }

    fun useAbility(abilityNumber:Int){

        when(abilityNumber){
            1 -> {
                _mainViewState.update { it.copy(playerText = _mainViewState.value.selectedPlayer!!.abilityOne(_mainViewState.value.enemy!!)) };
                _mainViewState.update { it.copy(enemyText = _mainViewState.value.enemy!!.battle(_mainViewState.value.selectedPlayer!!)) };
            }
            2 -> {
                _mainViewState.update { it.copy(playerText = _mainViewState.value.selectedPlayer!!.abilityTwo(_mainViewState.value.enemy!!)) };
                _mainViewState.update { it.copy(enemyText = _mainViewState.value.enemy!!.battle(_mainViewState.value.selectedPlayer!!)) };
            }
            3 -> {
                _mainViewState.update { it.copy(playerText = _mainViewState.value.selectedPlayer!!.abilityThree(_mainViewState.value.enemy!!)) };
                _mainViewState.update { it.copy(enemyText = _mainViewState.value.enemy!!.battle(_mainViewState.value.selectedPlayer!!)) };
            }
            4 -> {
                _mainViewState.update { it.copy(playerText = _mainViewState.value.selectedPlayer!!.abilityFour(_mainViewState.value.enemy!!)) };
                _mainViewState.update { it.copy(enemyText = _mainViewState.value.enemy!!.battle(_mainViewState.value.selectedPlayer!!)) };
            }
        }

        _mainViewState.update { it.copy(currentEnemyHealth =  _mainViewState.value.enemy!!.entityCurrentHealth) }
        _mainViewState.update { it.copy(currentPlayerHealth =  _mainViewState.value.selectedPlayer!!.entityCurrentHealth) }


        if(!_mainViewState.value.enemy!!.isAlive && _mainViewState.value.selectedPlayer!!.isAlive){
            _mainViewState.update { it.copy(battleComplete = true)}
            val xpGain = Random.nextInt(1,5);
            _mainViewState.value.selectedPlayer!!.earnXP(xpGain)
            _mainViewState.update { it.copy(battleCompleteText = "Enemy defeated earned $xpGain XP")}
            completeRoom()
            db.updatePlayer(_mainViewState.value.selectedPlayer!!)
        }else if(!_mainViewState.value.selectedPlayer!!.isAlive){
            _mainViewState.update { it.copy(battleComplete = true)}
            _mainViewState.update { it.copy(battleCompleteText = "Player defeated")}
        }
    }

    fun checkExpiredDungeon(){
        val playerID = _mainViewState.value.selectedPlayer!!.id
        val expiredDungeons = db.getExpiredDungeons(playerID)

        for (expiredDungeon in expiredDungeons){
            db.deleteDungeon(expiredDungeon)
        }

        db.generateDungeons(playerID.toLong(), expiredDungeons.size)
    }
    fun enterDungeon(dungeon: Dungeon, context : Context){
        if(_mainViewState.value.allActiveDungeon.size >= 3){
            val text = "You can only have 3 Dungeons activate at once"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context , text, duration) // in Activity
            toast.show()
            return;
        }

        val playerID = _mainViewState.value.selectedPlayer!!.id
        dungeon.dungeonActive = true;
        db.updateDungeon(dungeon)
        db.generateDungeons(playerID.toLong(), 1)
        getOpenDungeons()
        getActiveDungeons()
    }

    fun deleteDungeon(dungeon: Dungeon){
        db.deleteDungeon(dungeon)
        getOpenDungeons()
        getActiveDungeons()
    }


    private fun resetDistance() {
        locationRepository.resetDistance()
    }

    private fun startPeriodicDistanceCheck() {
        viewModelScope.launch {
            while (isActive) {
                if(_mainViewState.value.selectedPlayer != null){
                    subtractDistanceFromDungeon()
                    resetDistance()
                    getActiveDungeons()
                }
                delay(10000) // Delay for 10 seconds
            }
        }
    }

    private fun subtractDistanceFromDungeon(){
        var distance = locationRepository.getDistanceWalked()
        var activeDungeons = _mainViewState.value.allActiveDungeon

        for(activeDungeon in activeDungeons){
            if(activeDungeon.dungeonWalkedDistance < activeDungeon.dungeonTotalDistance){
                activeDungeon.dungeonWalkedDistance += floor(distance).toInt()
                db.updateDungeon(activeDungeon)
            }
        }

        //getActiveDungeons()

    }

    fun selectDungeon(dungeon: Dungeon){

        var tempDungeon = dungeon

        if(!dungeon.dungeonGenerated){
            generateDungeonRooms(dungeon)
            tempDungeon = db.getDungeonById(dungeon.id)
        }
        //doesn't update Dungeon correctly
        _mainViewState.update { it.copy(selectedDungeon = tempDungeon)}
        _mainViewState.update { it.copy(dungeonRooms = tempDungeon.rooms)}
        getAdjacentRooms()
    }

    private fun generateDungeonRooms(dungeon: Dungeon){
        dungeon.generateRooms();

        dungeon.dungeonGenerated = true

        val rooms = dungeon.rooms

        for (i in rooms.indices) {
            for (j in rooms[i].indices) {
                if(rooms[i][j] != null){
                    db.insertRoom(rooms[i][j]!!)
                }
            }
        }

        db.updateDungeon(dungeon)
    }

    fun selectRoom(room: Room){
        _mainViewState.update { it.copy(currentSelectedRoom = room)}
    }

    fun leaveDungeon(){
        _mainViewState.update { it.copy(selectedDungeon = null)}
        _mainViewState.update { it.copy(dungeonRooms = null)}
        _mainViewState.update { it.copy(currentSelectedRoom = null)}
        getActiveDungeons()
    }

    fun getAdjacentRooms() {

        val dungeonRooms = _mainViewState.value.dungeonRooms
        val visitedRooms = mutableListOf<Room>()
        val adjacentRooms = mutableListOf<Room>()
        val directions: List<Pair<Int,Int>> =  listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1),Pair(-1, 0))

        //Already Entered Rooms
        dungeonRooms!!.forEach { row ->
            row.forEach { room ->
                if (room != null && room.hasBeenVisited) {
                    visitedRooms.add(room)
                }
            }
        }

        for(currentRoom in visitedRooms){
            for(direction in directions){
                val newXIndex = currentRoom.xIndex + direction.first
                val newYIndex = currentRoom.yIndex + direction.second

                if(Dungeon.checkBoundaries(newXIndex,newYIndex)){
                    val adjacentRoom = dungeonRooms[newYIndex][newXIndex]
                    if(adjacentRoom != null && !adjacentRoom.hasBeenVisited){
                        adjacentRooms.add(adjacentRoom)
                    }
                }
            }
        }

        _mainViewState.update { it.copy(adjacentRooms = adjacentRooms)}
    }

    fun checkRoomEnabled(): Boolean {
        val selectedRoom = _mainViewState.value.currentSelectedRoom ?: return false

        if(selectedRoom!!.hasBeenVisited){
            return true
        }

        return _mainViewState.value.adjacentRooms.find { room: Room -> room.xIndex == selectedRoom.xIndex && room.yIndex == selectedRoom.yIndex } != null


    }

    fun enterRoom(){
        val selectedRoom = _mainViewState.value.currentSelectedRoom
        if(selectedRoom != null){
            _mainViewState.update { it.copy(currentRoomContent = selectedRoom.roomContents)}
        }

    }

    fun completeRoom(){
        val dungeonRooms = _mainViewState.value.dungeonRooms?: return
        val selectedRoom = _mainViewState.value.currentSelectedRoom ?: return

        dungeonRooms[selectedRoom.yIndex][selectedRoom.xIndex]!!.hasBeenVisited = true
        _mainViewState.update { it.copy(dungeonRooms = dungeonRooms)}
        selectedRoom.hasBeenVisited = true;
        getAdjacentRooms()
        db.updateRoom(selectedRoom)
        getActiveDungeons()
    }

    fun openDungeonDialog() {
        _mainViewState.update { it.copy(displayDungeonPopUp = true)}
    }

    fun dismissDialog() {
        _mainViewState.update { it.copy(displayDungeonPopUp = false)}
    }

    fun getItem(item : Item) {

        val player = mainViewState.value.selectedPlayer

        if(player != null){
            item.itemPlayerId = player!!.id
            db.insertItem(item)
        }
    }

}