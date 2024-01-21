package com.ccl3_id.wanderquest.data.models.rooms

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import kotlin.random.Random

class Room(val xIndex: Int, val yIndex: Int, var roomType: String, slotSize: Float, rectSize: Float) {


    companion object{
        val ROOM_SIZE = 150.dp
        val SLOT_SIZE = 400.dp
    }

    var centerPos:Offset? = null
    var hasBeenVisted : Boolean

    init {
        val randomX = Random.nextInt(0, (slotSize - rectSize).toInt()).toFloat()
        val randomY = Random.nextInt(0, (slotSize - rectSize).toInt()).toFloat()

        val topLeft = Offset(xIndex * slotSize + randomX, yIndex * slotSize + randomY)
        centerPos = Offset(topLeft.x + rectSize / 2, topLeft.y + rectSize / 2)
        hasBeenVisted = false

    }

    fun visitRoom(){
        this.hasBeenVisted = true
    }



}