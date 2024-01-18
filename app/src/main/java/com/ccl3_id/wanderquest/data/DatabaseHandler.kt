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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DatabaseHandler(context : Context) : SQLiteOpenHelper(context, dbName, null, dbVersion) {

    companion object DatabaseConfig {
        private const val dbName : String = "WanderQuest"
        private const val dbVersion : Int = 7

        private const val playerTableName = "Player"
        private const val playerId = "playerId"
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
        private const val itemId = "itemId"
        private const val itemName = "itemName"
        private const val itemType = "itemType"
        private const val itemImg = "itemImg"
        private const val itemPlayerId = "itemPlayerId"
        private const val itemIsEquipped = "itemIsEquipped"

        private const val dungeonTableName = "Dungeons"
        private const val dungeonId = "dungeonId"
        private const val dungeonName = "dungeonName"
        private const val dungeonTotalDistance = "dungeonTotalDistance"
        private const val dungeonWalkedDistance = "dungeonWalkedDistance"
        private const val dungeonActive = "dungeonActive"
        private const val dungeonCompleted = "dungeonCompleted"
        private const val dungeonPlayerId = "dungeonPlayerId"
        private const val dungeonCreatedAt = "dungeonCreated"
        private const val dungeonExpiresIn = "dungeonExpiresIn"

    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.execSQL("PRAGMA foreign_keys = ON")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $playerTableName;")
        db?.execSQL("DROP TABLE IF EXISTS $itemTableName;")
        db?.execSQL("DROP TABLE IF EXISTS $dungeonTableName;")
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
                "$itemImg VARCHAR(256)," +
                "$itemPlayerId INTEGER, " +
                "$itemIsEquipped BOOLEAN " +
                ");")

        //insertItemsData(db);

        db?.execSQL("CREATE TABLE IF NOT EXISTS $dungeonTableName (" +
                "$dungeonId INTEGER PRIMARY KEY, " +
                "$dungeonName VARCHAR(64), " +
                "$dungeonTotalDistance INTEGER, " +
                "$dungeonWalkedDistance INTEGER, " +
                "$dungeonActive BOOLEAN, " +
                "$dungeonCompleted BOOLEAN," +
                "$dungeonCreatedAt TIMESTAMP," +
                "$dungeonExpiresIn TIMESTAMP," +
                "$dungeonPlayerId INTEGER," +
                "FOREIGN KEY ($dungeonPlayerId) REFERENCES $playerTableName ($playerId) ON DELETE CASCADE" +
                ");")
    }

    private fun insertItemsData(db: SQLiteDatabase?){
        val items = listOf(
            // Sportswear - Headpiece
            Item(name = "Head Band", type = "Headpiece", img = "head_band", itemPlayerId = 0, itemIsEquipped = false),
            Item(name = "Cap", type = "Headpiece", img = "cap", itemPlayerId = 0, itemIsEquipped = false),
            Item(name = "Beanie", type = "Headpiece", img = "beanie", itemPlayerId = 0, itemIsEquipped = false),
            Item(name = "Sun Glasses", type = "Headpiece", img = "sun_glasses", itemPlayerId = 0, itemIsEquipped = false),

            // Sportswear - Handheld
            Item(name = "Dumbbells", type = "Handheld", img = "dumbbell", itemPlayerId = 0, itemIsEquipped = false),
            Item(name = "Protein Shake", type = "Handheld", img = "protein", itemPlayerId = 0, itemIsEquipped = false),
            Item(name = "Jumping Rope", type = "Handheld", img = "jump_rope", itemPlayerId = 0, itemIsEquipped = false),
            Item(name = "Member Card", type = "Handheld", img = "member_card", itemPlayerId = 0, itemIsEquipped = false),

            // Sportswear - Upper Body
            Item(name = "Muscle Shirt", type = "Upper Body", img = "tank_top", itemPlayerId = 0, itemIsEquipped = false),
            Item(name = "Grey Hoodie", type = "Upper Body", img = "hoodie", itemPlayerId = 0, itemIsEquipped = false),
            Item(name = "Weightlift Belt", type = "Upper Body", img = "weight_belt", itemPlayerId = 0, itemIsEquipped = false),
            Item(name = "Natural", type = "Upper Body", img = "natural", itemPlayerId = 0, itemIsEquipped = false),

            // Sportswear - Lower Body
            Item(name = "Shorts", type = "Lower Body", img = "shorts", itemPlayerId = 0, itemIsEquipped = false),
            Item(name = "Sweat Pants", type = "Lower Body", img = "sweat_pants", itemPlayerId = 0, itemIsEquipped = false),
            Item(name = "Leg Warmers", type = "Lower Body", img = "socks", itemPlayerId = 0, itemIsEquipped = false),
            Item(name = "Leggings", type = "Lower Body", img = "leggings", itemPlayerId = 0, itemIsEquipped = false)
        )

        // Insert each item into the database
        items.forEach { item ->
            db?.execSQL(
                "INSERT INTO $itemTableName ($itemName, $itemType, $itemImg) VALUES (?, ?, ?, ?)",
                arrayOf(item.name, item.type, item.img)
            )
        }
    }

    fun getAllItems(playerId: Int): List<Item> {
        println("GET ALL ITEMS")
        val items = mutableListOf<Item>()
        val db = readableDatabase

        // Query the database to get all items
        val cursor = db.rawQuery("SELECT * FROM $itemTableName " +
                "WHERE $itemPlayerId = $playerId " +
                "AND $itemIsEquipped = 0", null)

        // Process the cursor and create Item objects
        with(cursor) {
            while (moveToNext()) {
                val itemId = getInt(getColumnIndexOrThrow(itemId))
                val itemName = getString(getColumnIndexOrThrow(itemName))
                val itemType = getString(getColumnIndexOrThrow(itemType))
                val itemImg = getString(getColumnIndexOrThrow(itemImg))
                val itemPlayerId = getInt(getColumnIndexOrThrow(itemPlayerId))
                val itemIsEquippedInt = getInt(getColumnIndexOrThrow(itemIsEquipped))
                val itemIsEquipped = itemIsEquippedInt != 0

                val item = Item(itemId, itemName, itemType, itemImg, itemPlayerId, itemIsEquipped)
                items.add(item)
            }
        }

        // Close the cursor
        cursor.close()

        println("RETURNING GET ALL ITEMS")

        return items
    }

    fun insertItem(item: Item){
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(itemName,item.name)
            put(itemType, item.type)
            put(itemImg, item.img)
            put(itemPlayerId, item.itemPlayerId)
            put(itemIsEquipped, item.itemIsEquipped)
        }

        db.insert(itemTableName, null, values)
    }

    fun generateItem(playerId: Int, number: Int){
        val itemType = generateItemType()
        val itemName = generateItemName(itemType)
        val itemImg = generateItemImg(itemName)

        for(i in 0..number-1){
            println("GENERATE ITEM")
            val tempItem= Item(
                name = itemName,
                type = itemType,
                img = itemImg,
                itemPlayerId = playerId
            );
            insertItem(tempItem);
        }
    }

    fun generateItemType(): String{
        val itemTypes = listOf("head", "hand", "chest", "legs")

        return itemTypes.random()
    }

    fun generateItemName(itemType: String): String{
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


    fun generateItemImg(itemName: String): String{
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
        println("Item name after Elina magic")
        println(itemImages)
        return itemImages
    }

    fun equipItem(item: Item){
        val db = this.writableDatabase

        val values = ContentValues().apply{
            put(itemName,item.name)
            put(itemType, item.type)
            put(itemImg, item.img)
            put(itemPlayerId, item.itemPlayerId)
            put(itemIsEquipped, true)
        }

        db.update(itemTableName,values,"$itemId = ?", arrayOf(item.id.toString()))
    }

    fun getEquipItems(playerId: Int): List<Item> {
        val equippedItems = mutableListOf<Item>()
        val db = this.writableDatabase

        val cursor = db.rawQuery("SELECT * FROM $itemTableName " +
                "WHERE $itemPlayerId = $playerId " +
                "AND $itemIsEquipped = 1", null)

        with(cursor){
            while (moveToNext()){
                val itemId = getInt(getColumnIndexOrThrow(itemId))
                val itemName = getString(getColumnIndexOrThrow(itemName))
                val itemType = getString(getColumnIndexOrThrow(itemType))
                val itemImg = getString(getColumnIndexOrThrow(itemImg))
                val equippedItemId = getInt(getColumnIndexOrThrow(itemPlayerId))

                val equippedItem = Item(itemId, itemName, itemType, itemImg, equippedItemId)
                equippedItems.add(equippedItem)
            }
        }

        cursor.close()

        return equippedItems
    }

    fun unequipItem(item: Item){
        val db = this.writableDatabase

        val values = ContentValues().apply{
        put(itemName,item.name)
        put(itemType, item.type)
        put(itemImg, item.img)
        put(itemPlayerId, item.itemPlayerId)
        put(itemIsEquipped, false)
    }

        db.update(itemTableName,values,"$itemId = ?", arrayOf(item.id.toString()))
    }

    fun insertPlayer(player : Player):Long{
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

        return db.insert(playerTableName, null, values)
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
    fun getActiveDungeons(playerID : Int) : List<Dungeon> {
        val db = this.readableDatabase
        val dungeons = mutableListOf<Dungeon>()
        val cursor = db.rawQuery("SELECT * FROM $dungeonTableName WHERE $dungeonActive = TRUE AND $dungeonPlayerId = $playerID;", null)

        while(cursor.moveToNext()){
            val idId = cursor.getColumnIndex(dungeonId)
            val dungeonNameId = cursor.getColumnIndex(dungeonName)
            val dungeonTotalDistanceId = cursor.getColumnIndex(dungeonTotalDistance)
            val dungeonWalkedDistanceId = cursor.getColumnIndex(dungeonWalkedDistance)
            val dungeonActiveId = cursor.getColumnIndex(dungeonActive)
            val dungeonCompletedId = cursor.getColumnIndex(dungeonCompleted)
            val dungeonCreatedAtId= cursor.getColumnIndex(dungeonCreatedAt)
            val dungeonExpiresInId= cursor.getColumnIndex(dungeonExpiresIn)
            val dungeonPlayerId= cursor.getColumnIndex(dungeonPlayerId)
            //
            if(dungeonNameId >= 0){

                var tempDungeon = Dungeon(
                    cursor.getString(dungeonNameId),
                    cursor.getInt(dungeonTotalDistanceId),
                    cursor.getInt(dungeonWalkedDistanceId),
                    cursor.getInt(dungeonActiveId) > 0,
                    cursor.getInt(dungeonCompletedId) > 0,
                    cursor.getString(dungeonCreatedAtId),
                    cursor.getString(dungeonExpiresInId),
                    cursor.getInt(idId),
                    cursor.getInt(dungeonPlayerId),
                )

                dungeons.add(tempDungeon)
            }

        }
        return dungeons;
    }

    fun getOpenDungeons(playerID : Int) : List<Dungeon> {
        val db = this.readableDatabase
        val dungeons = mutableListOf<Dungeon>()
        val cursor = db.rawQuery("SELECT * FROM $dungeonTableName WHERE $dungeonActive = FALSE AND $dungeonPlayerId = $playerID;", null)

        while(cursor.moveToNext()){
            val idId = cursor.getColumnIndex(dungeonId)
            val dungeonNameId = cursor.getColumnIndex(dungeonName)
            val dungeonTotalDistanceId = cursor.getColumnIndex(dungeonTotalDistance)
            val dungeonWalkedDistanceId = cursor.getColumnIndex(dungeonWalkedDistance)
            val dungeonActiveId = cursor.getColumnIndex(dungeonActive)
            val dungeonCompletedId = cursor.getColumnIndex(dungeonCompleted)
            val dungeonCreatedAtId= cursor.getColumnIndex(dungeonCreatedAt)
            val dungeonExpiresInId= cursor.getColumnIndex(dungeonExpiresIn)
            val dungeonPlayerId= cursor.getColumnIndex(dungeonPlayerId)
            //
            if(dungeonNameId >= 0){

                var tempDungeon = Dungeon(
                    cursor.getString(dungeonNameId),
                    cursor.getInt(dungeonTotalDistanceId),
                    cursor.getInt(dungeonWalkedDistanceId),
                    cursor.getInt(dungeonActiveId) > 0,
                    cursor.getInt(dungeonCompletedId) > 0,
                    cursor.getString(dungeonCreatedAtId),
                    cursor.getString(dungeonExpiresInId),
                    cursor.getInt(idId),
                    cursor.getInt(dungeonPlayerId),
                )

                dungeons.add(tempDungeon)
            }

        }
        return dungeons;
    }

    fun getExpiredDungeons(playerID : Int) : List<Dungeon> {
        val db = this.readableDatabase
        val dungeons = mutableListOf<Dungeon>()
        val cursor = db.rawQuery("SELECT * FROM $dungeonTableName WHERE $dungeonActive = FALSE AND $dungeonPlayerId = $playerID AND  strftime('%s',$dungeonExpiresIn) < strftime('%s',datetime('now', 'localtime'));", null)

        while(cursor.moveToNext()){
            val idId = cursor.getColumnIndex(dungeonId)
            val dungeonNameId = cursor.getColumnIndex(dungeonName)
            val dungeonTotalDistanceId = cursor.getColumnIndex(dungeonTotalDistance)
            val dungeonWalkedDistanceId = cursor.getColumnIndex(dungeonWalkedDistance)
            val dungeonActiveId = cursor.getColumnIndex(dungeonActive)
            val dungeonCompletedId = cursor.getColumnIndex(dungeonCompleted)
            val dungeonCreatedAtId= cursor.getColumnIndex(dungeonCreatedAt)
            val dungeonExpiresInId= cursor.getColumnIndex(dungeonExpiresIn)
            val dungeonPlayerId= cursor.getColumnIndex(dungeonPlayerId)
            //
            if(dungeonNameId >= 0){

                var tempDungeon = Dungeon(
                    cursor.getString(dungeonNameId),
                    cursor.getInt(dungeonTotalDistanceId),
                    cursor.getInt(dungeonWalkedDistanceId),
                    cursor.getInt(dungeonActiveId) > 0,
                    cursor.getInt(dungeonCompletedId) > 0,
                    cursor.getString(dungeonCreatedAtId),
                    cursor.getString(dungeonExpiresInId),
                    cursor.getInt(idId),
                    cursor.getInt(dungeonPlayerId),
                )

                dungeons.add(tempDungeon)
            }

        }
        return dungeons;
    }

    fun insertDungeon(dungeon: Dungeon){
        val db = this.writableDatabase
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance() // gets the current time by default
        calendar.add(Calendar.HOUR_OF_DAY, 24) // adds 24 hours

        val values = ContentValues().apply {
            put(dungeonName,dungeon.dungeonName)
            put(dungeonTotalDistance, dungeon.dungeonTotalDistance)
            put(dungeonWalkedDistance, dungeon.dungeonWalkedDistance)
            put(dungeonActive, dungeon.dungeonActive)
            put(dungeonCompleted,  dungeon.dungeonCompleted)
            put(dungeonCreatedAt,  dateFormat.format(Date()))
            put(dungeonExpiresIn,  dateFormat.format(calendar.time))
            put(dungeonPlayerId,  dungeon.dungeonPlayerID)
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
            put(dungeonPlayerId,  dungeon.dungeonPlayerID)
        }
        db.update(dungeonTableName,values,"$dungeonId = ?", arrayOf(dungeon.id.toString()))
    }

    fun deleteDungeon(dungeon: Dungeon){
        val db = this.writableDatabase
        db.delete(dungeonTableName,"$dungeonId = ?", arrayOf(dungeon.id.toString()))
    }

    fun generateDungeons(id: Long, number: Int) {
        println("CALL")
        for(i in 0..number-1){
            println("GENERATE")
            val tempDungeon = Dungeon(generateDungeonName(), generateDistance(),0);
            tempDungeon.dungeonPlayerID = id.toInt();
            insertDungeon(tempDungeon);
        }
    }

    fun generateDungeonName(): String {
        val sportAdjectives = listOf("Endless", "Mighty", "Olympic", "Raging", "Thundering")
        val sportLocations = listOf("Stadium", "Arena", "Track", "Field", "Gym")
        return sportAdjectives.random() + " " + sportLocations.random()
    }

    fun generateDistance(): Int {
        val distances = listOf(500,750,1000,5000,10000)
        return distances.random()
    }

}

