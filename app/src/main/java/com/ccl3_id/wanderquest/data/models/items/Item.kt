package com.ccl3_id.wanderquest.data.models.items


open class Item(
    val id: Int = 0,
    val name: String,
    val type: String,
    val img: String,
    val itemPlayerId: Int,
    val itemIsEquipped: Boolean = false,
    var itemStatsJSON: String
)

