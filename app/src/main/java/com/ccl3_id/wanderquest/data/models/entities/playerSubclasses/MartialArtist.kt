package com.ccl3_id.wanderquest.data.models.entities.playerSubclasses

import com.ccl3_id.wanderquest.data.models.entities.Entity
import com.ccl3_id.wanderquest.data.models.entities.Player
import kotlin.random.Random

class MartialArtist(
    playerName: String,
    playerClass: String,
    playerLevel: Int,
    playerStats: MutableMap<String, Int>,
    playerAttributePoints: Int,
    lastPlayed: Boolean,
    playerCurrentXP: Int,
    id: Int
) : Player(
    playerName,
    playerClass,
    playerLevel,
    playerStats,
    playerAttributePoints,
    lastPlayed,
    playerCurrentXP,
    id
) {
    init {
        this.abilityOneName = "Martial 1";
        this.abilityTwoName = "Martial 2";
        this.abilityThreeName = "Martial 3";
        this.abilityFourName = "Martial 4";

        this.abilityOneDescription = "Martial Description 1";
        this.abilityTwoDescription = "Martial Description 2";
        this.abilityThreeDescription = "Martial Description 3";
        this.abilityFourDescription = "Martial Description 4";

    }

}
