package com.ccl3_id.wanderquest.data.models.entities.playerSubclasses

import com.ccl3_id.wanderquest.data.models.entities.Player

class BodyBuilder(
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
        this.abilityOneName = "Body 1";
        this.abilityTwoName = "Body 2";
        this.abilityThreeName = "Body 3";
        this.abilityFourName = "Body 4";

        this.abilityOneDescription = "Body Description 1";
        this.abilityTwoDescription = "Body Description 2";
        this.abilityThreeDescription = "Body Description 3";
        this.abilityFourDescription = "Body Description 4";

    }
}