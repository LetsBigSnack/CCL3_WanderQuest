package com.ccl3_id.wanderquest.data.models.entities.playerSubclasses

import com.ccl3_id.wanderquest.data.models.entities.Player

class PersonalTrainer(
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
        this.abilityOneName = "Trainer 1";
        this.abilityTwoName = "Trainer 2";
        this.abilityThreeName = "Trainer 3";
        this.abilityFourName = "Trainer 4";

        this.abilityOneDescription = "Trainer Description 1";
        this.abilityTwoDescription = "Trainer Description 2";
        this.abilityThreeDescription = "Trainer Description 3";
        this.abilityFourDescription = "Trainer Description 4";

    }
}