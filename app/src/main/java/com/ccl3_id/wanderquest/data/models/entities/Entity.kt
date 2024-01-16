package com.ccl3_id.wanderquest.data.models.entities

open class Entity (val entityName : String = "Entity")
{
    var entityMaxHealth: Int = 0;
    var entityCurrentHealth: Int = 0;
    var isAlive = true;

    fun takeDmg(dmg : Int){
        entityCurrentHealth -= dmg;
        if(entityCurrentHealth <= 0){
            isAlive = false;
        }
    }

    fun heal(heal : Int){
        entityCurrentHealth += heal;
        if(entityCurrentHealth > entityMaxHealth){
            entityCurrentHealth = entityMaxHealth;
        }
    }

    fun revive(){
        isAlive = true;
    }

}