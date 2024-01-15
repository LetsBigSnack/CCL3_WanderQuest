package com.ccl3_id.wanderquest.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ItemHandler(context: Context) : SQLiteOpenHelper(context, dbName, null, 5) {
    companion object MainDatabase {
        internal const val dbName = "ItemDatabase"
        internal const val tableName = "Items"
        internal const val id = "_id"
        internal const val name = "name"
        internal const val type = "type"
        internal const val disc = "disc"
        internal const val img = "img"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create the Items table when the database is first created.
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS $tableName (" +
                    "$id INTEGER PRIMARY KEY, " +
                    "$name VARCHAR(64), " +
                    "$type VARCHAR(64), " +
                    "$disc VARCHAR(256), " +
                    "$img VARCHAR(256));"
        )

        insertItemsData(db)
    }

    private fun insertItemsData(db: SQLiteDatabase?){
        val items = listOf(
            //Axes
            Item(name = "Simple Stone", type = "Axe", disc = "One hand weapon", img = "axe_1"),
            Item(name = "Advanced Stone", type = "Axe", disc = "One hand weapon", img = "axe_2"),
            Item(name = "Expert Stone", type = "Axe", disc = "Two handed weapon", img = "axe_3"),
            Item(name = "Simple Iron", type = "Axe", disc = "One hand weapon", img = "axe_4"),
            Item(name = "Advanced Iron", type = "Axe", disc = "One hand weapon", img = "axe_5"),
            Item(name = "Expert Iron", type = "Axe", disc = "Two handed weapon", img = "axe_6"),

            //Clubs
            Item(name = "Bone", type = "Club", disc = "One hand weapon", img = "club_1"),
            Item(name = "Simple Wooden", type = "Club", disc = "One hand weapon", img = "club_2"),
            Item(name = "Advanced Wooden", type = "Club", disc = "One hand weapon", img = "club_3"),

            //Hammers
            Item(name = "Simple Stone", type = "Hammer", disc = "One hand weapon", img = "club_4"),
            Item(name = "Advanced Stone", type = "Hammer", disc = "Two handed weapon", img = "club_5"),
            Item(name = "Expert Stone", type = "Hammer", disc = "Two handed weapon", img = "club_6"),

            //Swords
            Item(name = "Simple Stone", type = "Sword", disc = "One hand weapon", img = "sword_1"),
            Item(name = "Advanced Stone", type = "Sword", disc = "One hand weapon", img = "sword_2"),
            Item(name = "Expert Stone", type = "Sword", disc = "One hand weapon", img = "sword_3"),
            Item(name = "Simple Iron", type = "Sword", disc = "One hand weapon", img = "sword_4"),
            Item(name = "Advanced Iron", type = "Sword", disc = "One hand weapon", img = "sword_5"),
            Item(name = "Expert Iron", type = "Sword", disc = "One hand weapon", img = "sword_6"),

            //Daggers
            Item(name = "Simple Stone", type = "Dagger", disc = "One hand weapon", img = "dagger_1"),
            Item(name = "Advanced Stone", type = "Dagger", disc = "One hand weapon", img = "dagger_2"),
            Item(name = "Simple iron", type = "Dagger", disc = "One hand weapon", img = "dagger_3"),
            Item(name = "Advanced Iron", type = "Dagger", disc = "One hand weapon", img = "dagger_4"),

            //Spears
            Item(name = "Throwing spear", type = "Spear", disc = "One hand weapon", img = "spear_1"),
            Item(name = "Simple Stone", type = "Spear", disc = "Two handed weapon", img = "spear_2"),
            Item(name = "Advanced Stone", type = "Spear", disc = "Two handed weapon", img = "spear_3"),
            Item(name = "Expert Stone", type = "Spear", disc = "Two handed weapon", img = "spear_4"),

            //Bows
            Item(name = "Simple Wooden", type = "Bow", disc = "Two handed weapon", img = "bow_1"),
            Item(name = "Advanced Wooden", type = "Bow", disc = "Two handed weapon", img = "bow_2"),
            Item(name = "Simple Hard Wood", type = "Bow", disc = "Two handed weapon", img = "bow_3"),
            Item(name = "Advanced Hard Wood", type = "Bow", disc = "Two handed weapon", img = "bow_4"),
            Item(name = "Expert Hard Wood", type = "Bow", disc = "Two handed weapon", img = "bow_5"),

            //Wands
            Item(name = "Simple Wooden", type = "Wand", disc = "Two handed weapon", img = "wand_1"),
            Item(name = "Advanced Wooden", type = "Wand", disc = "Two handed weapon", img = "wand_2"),
            Item(name = "Water Stone", type = "Wand", disc = "Two handed weapon", img = "wand_3"),
            Item(name = "Fire Stone", type = "Wand", disc = "Two handed weapon", img = "wand_4"),
            Item(name = "Grass Stone", type = "Wand", disc = "Two handed weapon", img = "wand_5"),

            //Helms
            Item(name = "Simple Leather", type = "Helm", disc = "Head gear", img = "leather_helm_1"),
            Item(name = "Advanced Leather", type = "Helm", disc = "Head gear", img = "leather_helm_2"),
            Item(name = "Simple Bonded Leather", type = "Helm", disc = "Head gear", img = "leather_helm_3"),
            Item(name = "Advanced Bonded Leather", type = "Helm", disc = "Head gear", img = "leather_helm_4"),
            Item(name = "Simple Brass", type = "Helm", disc = "Head gear", img = "iron_helm_1"),
            Item(name = "Advanced Brass", type = "Helm", disc = "Head gear", img = "iron_helm_2"),
            Item(name = "Simple Iron", type = "Helm", disc = "Head gear", img = "iron_helm_3"),
            Item(name = "Advanced Iron", type = "Helm", disc = "Head gear", img = "iron_helm_4"),
            Item(name = "Expert Iron", type = "Helm", disc = "Head gear", img = "iron_helm_5"),
            Item(name = "Simple Mage", type = "Helm", disc = "Head gear", img = "mage_helm_1"),
            Item(name = "Advanced Mage", type = "Helm", disc = "Head gear", img = "mage_helm_2"),
            Item(name = "Expert Mage", type = "Helm", disc = "Head gear", img = "mage_helm_3"),

            //Armors
            Item(name = "Simple Leather", type = "Armor", disc = "Body gear", img = "leather_armor_1"),
            Item(name = "Advanced Leather", type = "Armor", disc = "Body gear", img = "leather_armor_2"),
            Item(name = "Simple Bonded Leather", type = "Armor", disc = "Body gear", img = "leather_armor_3"),
            Item(name = "Advanced Bonded Leather", type = "Armor", disc = "Body gear", img = "leather_armor_4"),
            Item(name = "Simple Brass", type = "Armor", disc = "Body gear", img = "iron_armor_1"),
            Item(name = "Advanced Brass", type = "Armor", disc = "Body gear", img = "iron_armor_3"),
            Item(name = "Simple Iron", type = "Armor", disc = "Body gear", img = "iron_armor_2"),
            Item(name = "Advanced Iron", type = "Armor", disc = "Body gear", img = "iron_armor_4"),
            Item(name = "Simple Mage", type = "Armor", disc = "Body gear", img = "mage_armor_1"),
            Item(name = "Advanced Mage", type = "Armor", disc = "Body gear", img = "mage_armor_2"),
            Item(name = "Expert Mage", type = "Armor", disc = "Body gear", img = "mage_armor_3"),

            //Shields
            Item(name = "Simple", type = "Shield", disc = "One handed gear", img = "shield_1"),
            Item(name = "Advanced", type = "Shield", disc = "One handed gear", img = "shield_2"),
            Item(name = "Expert", type = "Shield", disc = "One handed gear", img = "shield_3"),
            Item(name = "Water", type = "Shield", disc = "One handed gear", img = "shield_6"),
            Item(name = "Fire", type = "Shield", disc = "One handed gear", img = "shield_5"),
            Item(name = "Grass", type = "Shield", disc = "One handed gear", img = "shield_4"),

            //Rings
            Item(name = "Water", type = "Ring", disc = "Accessories", img = "ring_1"),
            Item(name = "Fire", type = "Ring", disc = "Accessories", img = "ring_2"),
            Item(name = "Grass", type = "Ring", disc = "Accessories", img = "ring_3"),

            //Necklaces
            Item(name = "Water", type = "Necklaces", disc = "Accessories", img = "neck_1"),
            Item(name = "Fire", type = "Necklaces", disc = "Accessories", img = "neck_2"),
            Item(name = "Grass", type = "Necklaces", disc = "Accessories", img = "neck_3")
        )

        // Insert each item into the database
        items.forEach { item ->
            db?.execSQL(
                "INSERT INTO $tableName ($name, $type, $disc, $img) VALUES (?, ?, ?, ?)",
                arrayOf(item.name, item.type, item.disc, item.img)
            )
        }
    }

    // Function to retrieve all items from the database
    fun getAllItems(): List<Item> {
        val items = mutableListOf<Item>()
        val db = readableDatabase

        // Define the columns to retrieve
        val columns = arrayOf(MainDatabase.id, MainDatabase.name, MainDatabase.type, MainDatabase.disc, MainDatabase.img)

        // Query the database to get all items
        val cursor = db.query(tableName, columns, null, null, null, null, null)

        // Process the cursor and create Item objects
        with(cursor) {
            while (moveToNext()) {
                val itemId = getInt(getColumnIndexOrThrow(MainDatabase.id))
                val itemName = getString(getColumnIndexOrThrow(MainDatabase.name))
                val itemType = getString(getColumnIndexOrThrow(MainDatabase.type))
                val itemDisc = getString(getColumnIndexOrThrow(MainDatabase.disc))
                val itemImg = getString(getColumnIndexOrThrow(MainDatabase.img))

                val item = Item(itemId, itemName, itemType, itemDisc, itemImg)
                items.add(item)
            }
        }

        // Close the cursor
        cursor.close()

        return items
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $tableName")
        onCreate(db)
    }
}