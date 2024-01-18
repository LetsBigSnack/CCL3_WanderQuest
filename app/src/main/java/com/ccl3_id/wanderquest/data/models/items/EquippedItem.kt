package com.ccl3_id.wanderquest.data.models.items



class EquippedItem(
    id: Int = 0,
    name: String,
    type: String,
    img: String,
    itemPlayerId: Int,
    itemIsEquipped: Boolean = false,
    var equippedItemId: Int = 0
): Item(
    id,
    name,
    type,
    img,
    itemPlayerId,
    itemIsEquipped,
)