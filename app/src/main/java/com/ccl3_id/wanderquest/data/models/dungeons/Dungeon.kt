package com.ccl3_id.wanderquest.data.models.dungeons

import com.ccl3_id.wanderquest.data.models.rooms.Room
import kotlin.random.Random

class Dungeon(
    var dungeonName : String,
    var dungeonTotalDistance : Int,
    var dungeonWalkedDistance : Int,
    var dungeonActive : Boolean = false,
    var dungeonCompleted : Boolean = false,
    var dungeonCreatedAt : String = "",
    var dungeonExpiresIn : String = "",
    var id : Int = 0,
    var dungeonPlayerID : Int = 0
) {

    lateinit var rooms : Array<Array<Room?>>


    init {
        rooms = Array(5) { Array(5) { null } }
    }

    fun generateRooms(slotSize: Float, rectSize: Float) {

        val dungeonSize = 12;
        var currentSize = 0;
        val createdRooms = mutableListOf<Room>()
        val directions: List<Pair<Int,Int>> =  listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1),Pair(-1, 0))


        //Starting Room
        val startingRoom = Room(2,2,"starting", slotSize, rectSize)
        currentSize++;
        createdRooms.add(startingRoom)

        while (currentSize < dungeonSize){

            val currentRoom = createdRooms.find{ room: Room -> room.hasBeenVisted == false }

            //if everything as been visted but still not desired Size --> clear vistedRooms
            if(currentRoom == null){
                    createdRooms.map {
                        it.hasBeenVisted = false
                    }
                    continue
            }

            if(currentRoom != null){
                for (direction in directions){

                    val newXIndex = currentRoom.xIndex + direction.first
                    val newYIndex = currentRoom.yIndex + direction.second
                    val chance = Random.nextInt(1,100)
                    val roomType = listOf<String>("Monster", "Item", "Empty").random()

                    // Check boundaries and Chance
                    if(!checkBoundaries(newXIndex,newYIndex) || chance < 75){
                        continue
                    }
                    // Check if a Room already exisit there
                    if(createdRooms.find { room: Room ->  room.xIndex == newXIndex && room.yIndex == newYIndex} != null){
                        continue
                    }

                    if(currentSize == dungeonSize){
                        break
                    }

                    val tempRoom = Room(newXIndex, newYIndex, roomType, slotSize, rectSize)
                    createdRooms.add(tempRoom)
                    currentSize++
                }
                currentRoom.hasBeenVisted = true
            }
        }

        for (room in createdRooms){
            this.rooms[room.yIndex][room.xIndex] = room
        }

        println("Generated Dungeon")

    }

    fun checkBoundaries(newXIndex: Int, newYIndex: Int):Boolean{
        return checkXBoundaries(newXIndex) && checkYBoundaries(newYIndex)
    }

    fun checkXBoundaries(newXIndex : Int):Boolean{
        return newXIndex in 0..4
    }
    fun checkYBoundaries(newYIndex : Int):Boolean{
        return newYIndex in 0..4
    }


    fun displayWalkedDistance():String{
        return if (dungeonWalkedDistance < 1000) {
            "${dungeonWalkedDistance}m"
        } else {
            val kilometers = dungeonWalkedDistance / 1000.0
            "${kilometers}km"
        }
    }

    fun displayTotalDistance():String{
        return if (dungeonTotalDistance < 1000) {
            "${dungeonTotalDistance}m"
        } else {
            val kilometers = dungeonTotalDistance / 1000.0
            "${kilometers}km"
        }
    }

}