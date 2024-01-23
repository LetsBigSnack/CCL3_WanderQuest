package com.ccl3_id.wanderquest.data.models.items

import com.ccl3_id.wanderquest.data.models.entities.Player
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException


open class Item(
    val id: Int = 0,
    val name: String,
    val type: String,
    val img: String,
    val itemPlayerId: Int,
    val itemIsEquipped: Boolean = false,
    var itemStatsJSON: String = "",
    var itemStats : MutableMap<String, Int> = mutableMapOf()
){
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



