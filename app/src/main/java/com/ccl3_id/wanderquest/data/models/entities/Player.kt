package com.ccl3_id.wanderquest.data.models.entities

import kotlinx.coroutines.flow.update
import java.lang.Math.floor
import kotlin.math.roundToInt
import kotlin.random.Random

open class Player (
    var playerName: String = "Player",
    var playerClass : String,
    var playerLevel : Int,
    var playerStats :  MutableMap<String, Int>,
    var playerAttributePoints: Int,
    var lastPlayed : Boolean,
    var playerCurrentXP : Int = 0,
    var id:Int = 0,
) : Entity(playerName){

    var playerXPToNextLevel : Int = 0;

    var savedStats : MutableMap<String, Int> = mutableMapOf()

    var abilityOneName : String = "ab1";
    var abilityTwoName : String = "ab2";
    var abilityThreeName : String = "ab3";
    var abilityFourName : String = "ab4";

    var abilityOneDescription : String = "ab1";
    var abilityTwoDescription  : String = "ab2";
    var abilityThreeDescription  : String = "ab3";
    var abilityFourDescription  : String = "ab4";



    companion object {
        val CLASS_LIST: List<String> = listOf("Bodybuilder", "Martial Artist", "Endurance Runner", "CrossFit Athlete","Personal Trainer")
        val CLASS_ATTRIBUTES = mapOf(
            "Bodybuilder" to "Strength, Constitution",
            "Martial Artist" to "Dexterity, Stamina",
            "Endurance Runner" to "Stamina, Constitution",
            "CrossFit Athlete" to "Strength, Dexterity, Stamina",
            "Personal Trainer"  to "Motivation, Constitution",
        );
        val CLASS_DESCRIPTION = mapOf(
            "Bodybuilder" to "Bodybuilders prioritize raw physical power and durability. Their training leads to immense strength and a robust constitution, making them formidable in physical challenges and combat.",
            "Martial Artist" to "Martial Artists excel in agility and endurance. Their rigorous training enhances their dexterity, allowing them to execute precise and swift movements, and their stamina enables prolonged combat and physical activity.",
            "Endurance Runner" to "Endurance Runners focus on long-lasting performance and resilience. Their incredible stamina allows them to outlast others in activities requiring sustained effort, and their constitution helps them endure physical stress and fatigue.",
            "CrossFit Athlete" to "CrossFit Athletes are the all-rounders, balancing strength, agility, and endurance. They are capable of handling a diverse set of physical challenges due to their well-rounded training.",
            "Personal Trainer"  to "Personal Trainers are experts in fitness and motivation. They not only possess a good constitution due to their regular training but are also skilled in motivating others, potentially enhancing the performance of their team.",
        )
        const val STAT_STRENGTH = "Strength"
        const val STAT_STAMINA = "Stamina"
        const val STAT_DEXTERITY = "Dexterity"
        const val STAT_CONSTITUTION = "Constitution"
        const val STAT_MOTIVATION = "Motivation"

        val STAT_LIST: List<String> = listOf(STAT_STRENGTH, STAT_STAMINA, STAT_DEXTERITY, STAT_CONSTITUTION, STAT_MOTIVATION)

    }
    init {
        getNewHealth();
        getXPtoNextLevel();
    }

    fun levelUp() {
        getNewHealth();
        getXPtoNextLevel();
        saveStats();
        playerLevel += 1;
        playerAttributePoints += 4
    }

    fun saveStats(){

        savedStats = mutableMapOf(
            Player.STAT_STRENGTH to this.playerStats[Player.STAT_STRENGTH]!!,
            Player.STAT_STAMINA to this.playerStats[Player.STAT_STAMINA]!!,
            Player.STAT_DEXTERITY to this.playerStats[Player.STAT_DEXTERITY]!!,
            Player.STAT_CONSTITUTION to this.playerStats[Player.STAT_CONSTITUTION]!!,
            Player.STAT_MOTIVATION to this.playerStats[Player.STAT_MOTIVATION]!!,
        )
    }

    open fun abilityOne(entity: Entity): String {
        val dmgDealt = Random.nextInt(1, 2);

        entity.takeDmg(dmgDealt);

        return "$entityName: Attacked for $dmgDealt DMG";

    }
    open fun abilityTwo(entity: Entity): String {
        val heal = Random.nextInt(1, 10);

        this.heal(heal);

        return "$entityName: Healed for $heal";

    }
    open fun abilityThree(entity: Entity): String {
        val dmgDealt = Random.nextInt(1, 10);

        entity.takeDmg(dmgDealt);

        return "$entityName: Attacked for $dmgDealt DMG";

    }
    open fun abilityFour(entity: Entity): String {
        val dmgDealt = Random.nextInt(1, 10);

        entity.takeDmg(dmgDealt);

        return "$entityName: Attacked for $dmgDealt DMG";

    }

    fun addStat(statName : String){
        if(this.playerAttributePoints >= 1){
            when(statName){
                Player.STAT_STRENGTH -> {
                    val currentStrength = this.playerStats[Player.STAT_STRENGTH] ?: 0
                    this.playerStats[Player.STAT_STRENGTH] = currentStrength + 1
                }
                Player.STAT_STAMINA -> {
                    val current = this.playerStats[Player.STAT_STAMINA] ?: 0
                    this.playerStats[Player.STAT_STAMINA] = current + 1
                }
                Player.STAT_DEXTERITY -> {
                    val current = this.playerStats[Player.STAT_DEXTERITY] ?: 0
                    this.playerStats[Player.STAT_DEXTERITY] = current + 1
                }
                Player.STAT_CONSTITUTION -> {
                    val current = this.playerStats[Player.STAT_CONSTITUTION] ?: 0
                    this.playerStats[Player.STAT_CONSTITUTION] = current + 1
                }
                Player.STAT_MOTIVATION -> {
                    val current = this.playerStats[Player.STAT_MOTIVATION] ?: 0
                    this.playerStats[Player.STAT_MOTIVATION] = current + 1
                }
                else -> {
                }
            }
            this.spendStatPoints();
        }
    }

    fun subStat(statName : String){
        when(statName){
            Player.STAT_STRENGTH -> {
                if ((this.playerStats[Player.STAT_STRENGTH] ?: 0) - 1 >= (savedStats[Player.STAT_STRENGTH] ?: 0)) {
                    this.playerStats[Player.STAT_STRENGTH] = (this.playerStats[Player.STAT_STRENGTH] ?: 0) - 1
                    refundStatPoints()
                }
            }
            Player.STAT_STAMINA -> {
                if ((this.playerStats[Player.STAT_STAMINA] ?: 0) - 1 >= (savedStats[Player.STAT_STAMINA] ?: 0)) {
                    this.playerStats[Player.STAT_STAMINA] = (this.playerStats[Player.STAT_STAMINA] ?: 0) - 1
                    refundStatPoints()
                }
            }
            Player.STAT_DEXTERITY -> {
                if ((this.playerStats[Player.STAT_DEXTERITY] ?: 0) - 1 >= (savedStats[Player.STAT_DEXTERITY] ?: 0)) {
                    this.playerStats[Player.STAT_DEXTERITY] = (this.playerStats[Player.STAT_DEXTERITY] ?: 0) - 1
                    refundStatPoints()
                }
            }
            Player.STAT_CONSTITUTION -> {
                if ((this.playerStats[Player.STAT_CONSTITUTION] ?: 0) - 1 >= (savedStats[Player.STAT_CONSTITUTION] ?: 0)) {
                    this.playerStats[Player.STAT_CONSTITUTION] = (this.playerStats[Player.STAT_CONSTITUTION] ?: 0) - 1
                    refundStatPoints()
                }
            }
            Player.STAT_MOTIVATION -> {
                if ((this.playerStats[Player.STAT_MOTIVATION] ?: 0) - 1 >= (savedStats[Player.STAT_MOTIVATION] ?: 0)) {
                    this.playerStats[Player.STAT_MOTIVATION] = (this.playerStats[Player.STAT_MOTIVATION] ?: 0) - 1
                    refundStatPoints()
                }
            }
            else -> {
            }
        }
    }

    fun getStats(statName: String): Int {
        return this.playerStats[statName] ?: 0
    }

    private fun refundStatPoints(){
        this.playerAttributePoints += 1;
    }

    private fun spendStatPoints(){
        this.playerAttributePoints -= 1;
    }

    fun getNewHealth(){
        entityMaxHealth = playerLevel * (this.playerStats[STAT_CONSTITUTION]!!) + 10;
        entityCurrentHealth = entityMaxHealth;
        isAlive = true
    }

    fun getXPtoNextLevel(){
        playerCurrentXP -= playerXPToNextLevel;
        playerXPToNextLevel = (playerLevel*1.6).roundToInt();
    }

    fun earnXP(xp:Int){
        playerCurrentXP += xp;
    }

}