package com.ccl3_id.wanderquest.data.models.rooms

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import kotlin.random.Random

class Room(val xIndex: Int, val yIndex: Int, var roomType: String) {


    companion object{
        val ROOM_SIZE = 150
        val SLOT_SIZE = 400
    }

    var centerPos:Offset? = null

    var randomX = 0f
    var randomY = 0f
    var hasBeenVisted : Boolean

    init {
        randomX = Random.nextInt(0, (SLOT_SIZE - ROOM_SIZE).toInt()).toFloat()
        randomY = Random.nextInt(0, (SLOT_SIZE - ROOM_SIZE).toInt()).toFloat()
        hasBeenVisted = false
    }

    fun visitRoom(){
        this.hasBeenVisted = true
    }

}