package com.ccl3_id.wanderquest.data.models.entities.playerSubclasses

import com.ccl3_id.wanderquest.data.models.entities.Player

class CrossFitAthlete(
    playerName: String,
    playerClass: String,
    playerLevel: Int,
    playerStats: MutableMap<String, Int>,
    playerAttributePoints: Int,
    lastPlayed: Boolean,
    playerCurrentXP: Int,
    id: Int,
    tut1: Boolean,
    tut2: Boolean,
    tut3: Boolean,
    tut4: Boolean,

    ) : Player(
    playerName,
    playerClass,
    playerLevel,
    playerStats,
    playerAttributePoints,
    lastPlayed,
    playerCurrentXP,
    id,
    tut1,
    tut2,
    tut3,
    tut4
) {
}