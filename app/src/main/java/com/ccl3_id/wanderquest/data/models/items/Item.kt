package com.ccl3_id.wanderquest.data.models.items

import com.ccl3_id.wanderquest.data.models.GameObject
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlin.random.Random


open class Item(
    val id: Int = 0,
    val name: String,
    val type: String,
    val img: String,
    var itemPlayerId: Int,
    val itemIsEquipped: Boolean = false,
    var itemStatsJSON: String = "",
    var itemStats : MutableMap<String, Int> = mutableMapOf()
): GameObject(){

    companion object{
        fun generateItem() : Item {

            val itemType = generateItemType()
            val itemName = generateItemName(itemType)
            val itemImg = generateItemImg(itemName)
            val itemStatsJSON = generateStatJson()

            val tempItem= Item(
                name = itemName,
                type = itemType,
                img = itemImg,
                itemPlayerId = 0,
                itemStatsJSON = itemStatsJSON
            );

            return tempItem;

        }

        private fun generateItemType(): String{
            val itemTypes = listOf("head", "hand", "chest", "legs")

            return itemTypes.random()
        }

        private fun generateItemName(itemType: String): String{
            val itemAdjectives = listOf("Mighty", "Bulking", "Lifting", "Crazy")
            var itemTypeName = emptyList<String>()

            if (itemType === "head"){
                itemTypeName = listOf("Headband", "Cap", "Beanie", "Sun Glasses")
            }else if (itemType === "hand"){
                itemTypeName = listOf("Dumbbell", "Protein Shake", "Jumping Rope", "Member Card")
            }else if (itemType === "chest"){
                itemTypeName = listOf("Tank Top", "Hoodie", "Weight Belt", "Natural Chest")
            }else if (itemType === "legs"){
                itemTypeName = listOf("Shorts", "Sweat Pants", "Leg Warmers", "Leggings")
            }

            return itemAdjectives.random() + " " + itemTypeName.random()
        }


        private fun generateItemImg(itemName: String): String{
            println("GENERATE ITEM IMG")
            // Split the input string into words
            val words = itemName.split(" ")
            var itemImages = ""

            // If there is more than one word, remove the first word and concatenate the remaining words
            if (words.size > 1) {
                itemImages= words.subList(1, words.size).joinToString("_").lowercase()
            } else {
                // If there is only one word, convert it to lowercase
                itemImages = itemName.lowercase()
            }
            return itemImages
        }

        private fun generateItemStatBuffs(): Map<String, Int>{
            val possibleStats = listOf("Strength", "Stamina", "Dexterity", "Constitution", "Motivation")
            val numberOfStats = Random.nextInt(1, possibleStats.size)

            return possibleStats.shuffled().take(numberOfStats).associateWith { Random.nextInt(1,10) }
        }

        private fun generateItemAbilities(): String{
            val possibleStats = listOf("Lucky Chucky", "Health Regen", "Magic Shield", " ", " ", " ", " ")

            return possibleStats.random()
        }

        private fun generateItemStatNerfs(): Map<String, Int>{
            val possibleStats = listOf("Strength", "Stamina", "Dexterity", "Constitution", "Motivation", " ", " ", " ", " ", " ")
            val numberOfStats = Random.nextInt(1, 2)

            return possibleStats.shuffled().take(numberOfStats).associateWith { Random.nextInt(1,5) }
        }

        private fun generateStatJson(): String{
            val statBuffs = generateItemStatBuffs()
            val abilities = generateItemAbilities()
            val statNerfs = generateItemStatNerfs()

            val itemAttributes = mapOf(
                "statBuffs" to statBuffs,
                "abilities" to abilities,
                "statNerfs" to statNerfs,
            )
            println("Generate stat JSON" + Gson().toJson(itemAttributes))
            return Gson().toJson(itemAttributes)
        }
    }


    fun updateItemStatsFromJSON() {
        if (itemStatsJSON.isNotBlank()) {
            try {
                val itemStatsObj = Gson().fromJson(itemStatsJSON, ItemStats::class.java)
                // Combine buffs and nerfs into a single map for simplicity
                itemStats = itemStatsObj.statBuffs.toMutableMap()
                itemStatsObj.statNerfs.forEach { (stat, value) ->
                    itemStats[stat] = itemStats.getOrDefault(stat, 0) - value
                }
            } catch (e: JsonSyntaxException) {
                itemStats.clear() // Clear stats if JSON is not in the expected format
            }
        }
    }

    fun getStats(): ItemStats? {
        return if (itemStatsJSON.isNotBlank()) {
            try {
                Gson().fromJson(itemStatsJSON, ItemStats::class.java)
            } catch (e: JsonSyntaxException) {
                null // Return null if JSON is not in the expected format
            }
        } else {
            null
        }
    }



}

data class ItemStats(
    val statBuffs: Map<String, Int>,
    val abilities: String,
    val statNerfs: Map<String, Int>
)

