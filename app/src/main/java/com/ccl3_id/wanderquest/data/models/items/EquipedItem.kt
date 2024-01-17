package com.ccl3_id.wanderquest.data.models.items



class EquipedItem(
    id: Int = 0,
    name: String,
    type: String,
    img: String,
    itemPlayerId: Int,
    itemIsEquipped: Boolean = false,
    var equipedItemId: Int = 0
): Item(
    id,
    name,
    type,
    img,
    itemPlayerId,
    itemIsEquipped,
)