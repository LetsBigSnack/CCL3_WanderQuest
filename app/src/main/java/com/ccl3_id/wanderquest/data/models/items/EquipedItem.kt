package com.ccl3_id.wanderquest.data.models.items



class EquipedItem(
    id: Int = 0,
    name: String,
    type: String,
    disc: String,
    img: String,
    var equipedItemId: Int = 0
): Item(
    id,
    name,
    type,
    disc,
    img
)