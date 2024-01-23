package com.ccl3_id.wanderquest.data.models.rooms

import androidx.compose.ui.geometry.Offset
import com.ccl3_id.wanderquest.data.models.GameObject
import com.ccl3_id.wanderquest.data.models.entities.Enemy
import com.ccl3_id.wanderquest.data.models.items.Item
import kotlin.random.Random

class Room(val xIndex: Int,
           val yIndex: Int,
           var roomType: String,
           var randomX : Float = 0f,
           var randomY : Float = 0f,
           var hasBeenVisited : Boolean = false,
           var id : Int = 0,
           var dungeonID : Int = 0,
    ) {

    var roomContents : GameObject? = null

    init {
        when(roomType){
            "Item" -> roomContents = Item.generateItem()
            "Monster" -> roomContents = Enemy()
        }
    }

    companion object{
        val ROOM_SIZE = 150
        val SLOT_SIZE = 400
    }

    var centerPos:Offset? = null


    fun generatePosition(){
        randomX = Random.nextInt(0, (SLOT_SIZE - ROOM_SIZE).toInt()).toFloat()
        randomY = Random.nextInt(0, (SLOT_SIZE - ROOM_SIZE).toInt()).toFloat()
    }

    fun visitRoom(){
        this.hasBeenVisited = true
    }

}