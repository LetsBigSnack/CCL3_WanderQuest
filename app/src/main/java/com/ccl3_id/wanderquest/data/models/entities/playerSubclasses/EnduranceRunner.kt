package com.ccl3_id.wanderquest.data.models.entities.playerSubclasses

import com.ccl3_id.wanderquest.data.models.entities.Player

class EnduranceRunner(
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
        this.abilityOneName = "Runner 1";
        this.abilityTwoName = "Runner 2";
        this.abilityThreeName = "Runner 3";
        this.abilityFourName = "Runner 4";

        this.abilityOneDescription = "Runner Description 1";
        this.abilityTwoDescription = "Runner Description 2";
        this.abilityThreeDescription = "Runner Description 3";
        this.abilityFourDescription = "Runner Description 4";

    }
}