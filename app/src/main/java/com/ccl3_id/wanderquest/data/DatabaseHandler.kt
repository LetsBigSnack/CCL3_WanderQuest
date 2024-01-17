package com.ccl3_id.wanderquest.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ccl3_id.wanderquest.data.models.dungeons.Dungeon
import com.ccl3_id.wanderquest.data.models.entities.Player
import com.ccl3_id.wanderquest.data.models.entities.playerSubclasses.BodyBuilder
import com.ccl3_id.wanderquest.data.models.entities.playerSubclasses.CrossFitAthlete
import com.ccl3_id.wanderquest.data.models.entities.playerSubclasses.EnduranceRunner
import com.ccl3_id.wanderquest.data.models.entities.playerSubclasses.MartialArtist
import com.ccl3_id.wanderquest.data.models.entities.playerSubclasses.PersonalTrainer
import com.ccl3_id.wanderquest.data.models.items.Item
import java.lang.Exception

class DatabaseHandler(context : Context) : SQLiteOpenHelper(context, dbName, null, dbVersion) {

    companion object DatabaseConfig {
        private const val dbName : String = "WanderQuest"
        private const val dbVersion : Int = 3

        private const val playerTableName = "Player"
        private const val playerId = "_id"
        private const val playerName = "playerName"
        private const val playerClass = "playerClass"
        private const val playerLevel = "playerLevel"
        private const val playerStrength = "playerStrength"
        private const val playerStamina = "playerStamina"
        private const val playerDexterity = "playerDexterity"
        private const val playerConstitution = "playerConstitution"
        private const val playerMotivation = "playerMotivation"
        private const val playerAttributePoints = "playerAttributePoints"
        private const val lastPlayed = "lastPlayed"
        private const val currentXP = "currentXP"

        private const val itemTableName = "Items"
        private const val itemId = "_id"
        private const val itemName = "itemName"
        private const val itemType = "itemType"
        private const val itemDescription = "itemDescription"
        private const val itemImg = "itemImg"

        private const val dungeonTableName = "Dungeons"
        private const val dungeonId = "_id"
        private const val dungeonName = "dungeonName"
        private const val dungeonTotalDistance = "dungeonTotalDistance"
        private const val dungeonWalkedDistance = "dungeonWalkedDistance"
        private const val dungeonActive = "dungeonActive"
        private const val dungeonCompleted = "dungeonCompleted"


    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.execSQL("PRAGMA foreign_keys = ON")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $playerTableName")
        db?.execSQL("DROP TABLE IF EXISTS $itemTableName")
        db?.execSQL("DROP TABLE IF EXISTS $dungeonTableName")
        onCreate(db)
    }

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("CREATE TABLE IF NOT EXISTS $playerTableName (" +
                "$playerId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$playerName VARCHAR(128) NOT NULL, " +
                "$playerClass VARCHAR(128)," +
                "$playerLevel INTEGER," +
                "$playerStrength INTEGER," +
                "$playerStamina INTEGER," +
                "$playerDexterity INTEGER," +
                "$playerConstitution INTEGER," +
                "$playerMotivation INTEGER," +
                "$playerAttributePoints INTEGER," +
                "$lastPlayed BOOLEAN," +
                "$currentXP INTEGER" +
                ");")

        db?.execSQL("CREATE TABLE IF NOT EXISTS $itemTableName (" +
                "$itemId INTEGER PRIMARY KEY, " +
                "$itemName VARCHAR(64), " +
                "$itemType VARCHAR(64), " +
                "$itemDescription VARCHAR(256), " +
                "$itemImg VARCHAR(256))" +
                ";")

        insertItemsData(db);

        db?.execSQL("CREATE TABLE IF NOT EXISTS $dungeonTableName (" +
                "$dungeonId INTEGER PRIMARY KEY, " +
                "$dungeonName VARCHAR(64), " +
                "$dungeonTotalDistance INTEGER, " +
                "$dungeonWalkedDistance INTEGER, " +
                "$dungeonActive BOOLEAN, " +
                "$dungeonCompleted BOOLEAN)" +
                ";")
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
                "INSERT INTO $itemTableName ($itemName, $itemType, $itemDescription, $itemImg) VALUES (?, ?, ?, ?)",
                arrayOf(item.name, item.type, item.disc, item.img)
            )
        }
    }

    fun getAllItems(): List<Item> {

        val items = mutableListOf<Item>()
        val db = readableDatabase

        // Define the columns to retrieve
        val columns = arrayOf(itemId, itemName, itemType, itemDescription, itemImg)

        // Query the database to get all items
        val cursor = db.query(itemTableName, columns, null, null, null, null, null)

        // Process the cursor and create Item objects
        with(cursor) {
            while (moveToNext()) {
                val itemId = getInt(getColumnIndexOrThrow(itemId))
                val itemName = getString(getColumnIndexOrThrow(itemName))
                val itemType = getString(getColumnIndexOrThrow(itemType))
                val itemDisc = getString(getColumnIndexOrThrow(itemDescription))
                val itemImg = getString(getColumnIndexOrThrow(itemImg))

                val item = Item(itemId, itemName, itemType, itemDisc, itemImg)
                items.add(item)
            }
        }

        // Close the cursor
        cursor.close()

        return items
    }

    fun insertPlayer(player : Player){
        deselectAllPlayers();
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(playerName,player.playerName)
            put(playerClass, player.playerClass)
            put(playerLevel, player.playerLevel)
            put(playerStrength, player.playerStats[Player.STAT_STRENGTH])
            put(playerStamina,  player.playerStats[Player.STAT_STAMINA])
            put(playerDexterity,  player.playerStats[Player.STAT_DEXTERITY])
            put(playerConstitution,  player.playerStats[Player.STAT_CONSTITUTION])
            put(playerMotivation,  player.playerStats[Player.STAT_MOTIVATION])
            put(playerAttributePoints, player.playerAttributePoints)
            put(lastPlayed, true)
            put(currentXP, 0)
        }

        db.insert(playerTableName, null, values)
    }

    fun updatePlayer(player: Player){
        val db = this.writableDatabase
        println(player);
        val values = ContentValues().apply {
            put(playerName,player.playerName)
            put(playerClass, player.playerClass)
            put(playerLevel, player.playerLevel)
            put(playerStrength, player.playerStats[Player.STAT_STRENGTH])
            put(playerStamina,  player.playerStats[Player.STAT_STAMINA])
            put(playerDexterity,  player.playerStats[Player.STAT_DEXTERITY])
            put(playerConstitution,  player.playerStats[Player.STAT_CONSTITUTION])
            put(playerMotivation,  player.playerStats[Player.STAT_MOTIVATION])
            put(playerAttributePoints, player.playerAttributePoints)
            put(lastPlayed, player.lastPlayed)
            put(currentXP, player.playerCurrentXP)
        }
        db.update(playerTableName,values,"${playerId} = ?", arrayOf(player.id.toString()))
    }

    fun getAllSelectedPlayers() : List<Player> {
        val db = this.readableDatabase
        val players = mutableListOf<Player>()
        val cursor = db.rawQuery("SELECT * FROM $playerTableName WHERE $lastPlayed = TRUE;", null)
        while(cursor.moveToNext()){
            val idId = cursor.getColumnIndex(playerId)
            val nameId = cursor.getColumnIndex(playerName)
            val classId = cursor.getColumnIndex(playerClass)
            val levelId = cursor.getColumnIndex(playerLevel)
            val strengthId = cursor.getColumnIndex(playerStrength)
            val dexterityId = cursor.getColumnIndex(playerDexterity)
            val staminaId = cursor.getColumnIndex(playerStamina)
            val constitutionId = cursor.getColumnIndex(playerConstitution)
            val motivationId = cursor.getColumnIndex(playerMotivation)
            val attributesId = cursor.getColumnIndex(playerAttributePoints)
            val lastPlayedId = cursor.getColumnIndex(lastPlayed)
            val currentXPId = cursor.getColumnIndex(currentXP)
            //
            if(nameId >= 0){

                val stats : MutableMap<String, Int> = mutableMapOf(
                    Player.STAT_STRENGTH to cursor.getInt(strengthId),
                    Player.STAT_STAMINA to  cursor.getInt(staminaId),
                    Player.STAT_DEXTERITY to cursor.getInt(dexterityId),
                    Player.STAT_CONSTITUTION to  cursor.getInt(constitutionId),
                    Player.STAT_MOTIVATION to  cursor.getInt(motivationId)
                )

                val tempPlayer = createPlayer(
                    cursor.getString(classId),
                    cursor.getString(nameId),
                    cursor.getInt(levelId),
                    stats,
                    cursor.getInt(attributesId),
                    cursor.getInt(lastPlayedId) > 0,
                    cursor.getInt(currentXPId),
                    cursor.getInt(idId)
                )

                players.add(tempPlayer)
            }

        }
        return players;
    }

    fun getPlayers() : List<Player> {
        val db = this.readableDatabase
        val players = mutableListOf<Player>()
        val cursor = db.rawQuery("SELECT * FROM $playerTableName;", null)
        while(cursor.moveToNext()){
            val idId = cursor.getColumnIndex(playerId)
            val nameId = cursor.getColumnIndex(playerName)
            val classId = cursor.getColumnIndex(playerClass)
            val levelId = cursor.getColumnIndex(playerLevel)
            val strengthId = cursor.getColumnIndex(playerStrength)
            val dexterityId = cursor.getColumnIndex(playerDexterity)
            val staminaId = cursor.getColumnIndex(playerStamina)
            val constitutionId = cursor.getColumnIndex(playerConstitution)
            val motivationId = cursor.getColumnIndex(playerMotivation)
            val attributesId = cursor.getColumnIndex(playerAttributePoints)
            val lastPlayedId = cursor.getColumnIndex(lastPlayed)
            val currentXPId = cursor.getColumnIndex(currentXP)
            //
            if(nameId >= 0){

                val stats : MutableMap<String, Int> = mutableMapOf(
                    Player.STAT_STRENGTH to cursor.getInt(strengthId),
                    Player.STAT_STAMINA to  cursor.getInt(staminaId),
                    Player.STAT_DEXTERITY to cursor.getInt(dexterityId),
                    Player.STAT_CONSTITUTION to  cursor.getInt(constitutionId),
                    Player.STAT_MOTIVATION to  cursor.getInt(motivationId)
                )

                val tempPlayer = createPlayer(
                    cursor.getString(classId),
                    cursor.getString(nameId),
                    cursor.getInt(levelId),
                    stats,
                    cursor.getInt(attributesId),
                    cursor.getInt(lastPlayedId) > 0,
                    cursor.getInt(currentXPId),
                    cursor.getInt(idId)
                )

                players.add(tempPlayer)
            }

        }
        return players;
    }

    fun createPlayer(className: String, name: String, level: Int, stats: MutableMap<String, Int>, attributePoints: Int, lastPlayed: Boolean, currentXP: Int, id: Int): Player {
        return when (className) {
            "Bodybuilder" -> BodyBuilder(name, className, level, stats, attributePoints, lastPlayed, currentXP, id)
            "Martial Artist" -> MartialArtist(name, className, level, stats, attributePoints, lastPlayed, currentXP, id)
            "Endurance Runner" -> EnduranceRunner(name, className, level, stats, attributePoints, lastPlayed, currentXP, id)
            "CrossFit Athlete" -> CrossFitAthlete(name, className, level, stats, attributePoints, lastPlayed, currentXP, id)
            "Personal Trainer" -> PersonalTrainer(name, className, level, stats, attributePoints, lastPlayed, currentXP, id)
            else -> Player(name, className, level, stats, attributePoints, lastPlayed, currentXP, id)
        }
    }


    fun deselectAllPlayers(){

        val writeDB = this.writableDatabase
        val players = getAllSelectedPlayers();

        players.forEach { player ->
            val values = ContentValues().apply {
                put(lastPlayed, false)
            }
            writeDB.update(
                playerTableName,
                values,
                "$playerId = ?",
                arrayOf(player.id.toString())
            )
        }
    }

    fun selectPlayer(player: Player){
        deselectAllPlayers();
        val db = this.writableDatabase
        println(player);
        val values = ContentValues().apply {
            put(lastPlayed, true)
        }
        db.update(playerTableName,values,"${playerId} = ?", arrayOf(player.id.toString()))
    }

    fun getSelectedPlayer() :Player {
        val players = getAllSelectedPlayers();

        if(players.size > 1){

            throw Exception();
        }else{
            return players.first();
        }
    }

    fun deletePlayer(player: Player){
        val db = this.writableDatabase
        db.delete(playerTableName,"$playerId = ?", arrayOf(player.id.toString()))
    }

    // Dungeon


    fun getDungeons() : List<Dungeon>{
        val db = this.readableDatabase
        val dungeons = mutableListOf<Dungeon>()
        val cursor = db.rawQuery("SELECT * FROM $dungeonTableName;", null)

        while(cursor.moveToNext()){
            val idId = cursor.getColumnIndex(playerId)
            val dungeonNameId = cursor.getColumnIndex(dungeonName)
            val dungeonTotalDistanceId = cursor.getColumnIndex(dungeonTotalDistance)
            val dungeonWalkedDistanceId = cursor.getColumnIndex(dungeonWalkedDistance)
            val dungeonActiveId = cursor.getColumnIndex(dungeonActive)
            val dungeonCompletedId = cursor.getColumnIndex(dungeonCompleted)
            //
            if(dungeonNameId >= 0){

                var tempDungeon = Dungeon(
                    cursor.getString(dungeonNameId),
                    cursor.getInt(dungeonTotalDistanceId),
                    cursor.getInt(dungeonWalkedDistanceId),
                    cursor.getInt(dungeonActiveId) > 0,
                    cursor.getInt(dungeonCompletedId) > 0,
                    cursor.getInt(idId)
                )

                dungeons.add(tempDungeon)
            }

        }
        return dungeons;
    }

    fun getActiveDungeons() : List<Dungeon> {
        val db = this.readableDatabase
        val dungeons = mutableListOf<Dungeon>()
        val cursor = db.rawQuery("SELECT * FROM $dungeonTableName WHERE $dungeonActive = TRUE;", null)

        while(cursor.moveToNext()){
            val idId = cursor.getColumnIndex(playerId)
            val dungeonNameId = cursor.getColumnIndex(dungeonName)
            val dungeonTotalDistanceId = cursor.getColumnIndex(dungeonTotalDistance)
            val dungeonWalkedDistanceId = cursor.getColumnIndex(dungeonWalkedDistance)
            val dungeonActiveId = cursor.getColumnIndex(dungeonActive)
            val dungeonCompletedId = cursor.getColumnIndex(dungeonCompleted)
            //
            if(dungeonNameId >= 0){

                var tempDungeon = Dungeon(
                    cursor.getString(dungeonNameId),
                    cursor.getInt(dungeonTotalDistanceId),
                    cursor.getInt(dungeonWalkedDistanceId),
                    cursor.getInt(dungeonActiveId) > 0,
                    cursor.getInt(dungeonCompletedId) > 0,
                    cursor.getInt(idId)
                )

                dungeons.add(tempDungeon)
            }

        }
        return dungeons;
    }

    fun getOpenDungeons() : List<Dungeon> {
        val db = this.readableDatabase
        val dungeons = mutableListOf<Dungeon>()
        val cursor = db.rawQuery("SELECT * FROM $dungeonTableName WHERE $dungeonActive = FALSE;", null)

        while(cursor.moveToNext()){
            val idId = cursor.getColumnIndex(playerId)
            val dungeonNameId = cursor.getColumnIndex(dungeonName)
            val dungeonTotalDistanceId = cursor.getColumnIndex(dungeonTotalDistance)
            val dungeonWalkedDistanceId = cursor.getColumnIndex(dungeonWalkedDistance)
            val dungeonActiveId = cursor.getColumnIndex(dungeonActive)
            val dungeonCompletedId = cursor.getColumnIndex(dungeonCompleted)
            //
            if(dungeonNameId >= 0){

                var tempDungeon = Dungeon(
                    cursor.getString(dungeonNameId),
                    cursor.getInt(dungeonTotalDistanceId),
                    cursor.getInt(dungeonWalkedDistanceId),
                    cursor.getInt(dungeonActiveId) > 0,
                    cursor.getInt(dungeonCompletedId) > 0,
                    cursor.getInt(idId)
                )

                dungeons.add(tempDungeon)
            }

        }
        return dungeons;
    }

    fun insertDungeon(dungeon: Dungeon){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(dungeonName,dungeon.dungeonName)
            put(dungeonTotalDistance, dungeon.dungeonTotalDistance)
            put(dungeonWalkedDistance, dungeon.dungeonWalkedDistance)
            put(dungeonActive, dungeon.dungeonActive)
            put(dungeonCompleted,  dungeon.dungeonCompleted)
        }

        db.insert(dungeonTableName, null, values)
    }

    fun updateDungeon(dungeon: Dungeon){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(dungeonName,dungeon.dungeonName)
            put(dungeonTotalDistance, dungeon.dungeonTotalDistance)
            put(dungeonWalkedDistance, dungeon.dungeonWalkedDistance)
            put(dungeonActive, dungeon.dungeonActive)
            put(dungeonCompleted,  dungeon.dungeonCompleted)
        }
        db.update(dungeonTableName,values,"$dungeonId = ?", arrayOf(dungeon.id.toString()))
    }

    fun deleteDungeon(dungeon: Dungeon){
        val db = this.writableDatabase
        db.delete(dungeonTableName,"$dungeonId = ?", arrayOf(dungeon.id.toString()))
    }

}

