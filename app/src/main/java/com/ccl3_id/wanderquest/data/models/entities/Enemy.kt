package com.ccl3_id.wanderquest.data.models.entities

import kotlin.random.Random

class Enemy(monsterName: String = "DIE-abitits") : Entity(monsterName) {

    var entityDMG = 4;

    init {
        entityMaxHealth = 10;
        entityCurrentHealth = entityMaxHealth;
    }

    fun battle(entity: Entity): String{
        val dmgDealt = Random.nextInt(1, entityDMG);

        entity.takeDmg(dmgDealt);

        return "$entityName: Attacked for $dmgDealt DMG";

    }

    fun copy(): Enemy {
        return Enemy(this.entityName)
    }

}