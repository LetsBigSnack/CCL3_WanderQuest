package com.ccl3_id.wanderquest.data.models.dungeons

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