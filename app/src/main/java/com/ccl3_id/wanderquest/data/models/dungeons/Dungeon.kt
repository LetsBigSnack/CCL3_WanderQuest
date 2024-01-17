package com.ccl3_id.wanderquest.data.models.dungeons

class Dungeon(
    var dungeonName : String,
    var dungeonTotalDistance : Int,
    var dungeonWalkedDistance : Int,
    var dungeonActive : Boolean = false,
    var dungeonCompleted : Boolean = false,
    var id : Int = 0
) {
}