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
import com.ccl3_id.wanderquest.data.models.items.EquipedItem
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

        private const val itemPlayerTableName = "ItemPlayer"
        private const val itemPlayerId = "_id"
        private const val _itemId = "itemId"
        private const val _playerId = "playerId"

    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.execSQL("PRAGMA foreign_keys = ON")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $playerTableName")
        db?.execSQL("DROP TABLE IF EXISTS $itemTableName")
        //TODO player Item remove
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

        db?.execSQL("CREATE TABLE IF NOT EXISTS $itemPlayerTableName(" +
                "$itemPlayerId INTEGER PRIMARY KEY," +
                "$_itemId INTEGER," +
                "$_playerId INTEGER," +
                "FOREIGN KEY ($_itemId) REFERENCES $itemTableName ($itemId) ON DELETE CASCADE," +
                "FOREIGN KEY ($_playerId) REFERENCES $playerTableName ($playerId) ON DELETE CASCADE" +
                ");")

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
            // Sportswear - Headpiece
            Item(name = "Head Band", type = "Headpiece", disc = "Sportswear accessory", img = "head_band"),
            Item(name = "Cap", type = "Headpiece", disc = "Sportswear accessory", img = "cap"),
            Item(name = "Beanie", type = "Headpiece", disc = "Sportswear accessory", img = "beanie"),
            Item(name = "Sun Glasses", type = "Headpiece", disc = "Sportswear accessory", img = "sun_glasses"),

            // Sportswear - Handheld
            Item(name = "Dumbbells", type = "Handheld", disc = "Sportswear accessory", img = "dumbbell"),
            Item(name = "Protein Shake", type = "Handheld", disc = "Sportswear accessory", img = "protein"),
            Item(name = "Jumping Rope", type = "Handheld", disc = "Sportswear accessory", img = "jump_rope"),
            Item(name = "Member Card", type = "Handheld", disc = "Sportswear accessory", img = "member_card"),

            // Sportswear - Upper Body
            Item(name = "Muscle Shirt", type = "Upper Body", disc = "Sportswear clothing", img = "tank_top"),
            Item(name = "Grey Hoodie", type = "Upper Body", disc = "Sportswear clothing", img = "hoodie"),
            Item(name = "Weightlift Belt", type = "Upper Body", disc = "Sportswear accessory", img = "weight_belt"),
            Item(name = "Natural", type = "Upper Body", disc = "Sportswear option", img = "natural"),

            // Sportswear - Lower Body
            Item(name = "Shorts", type = "Lower Body", disc = "Sportswear clothing", img = "shorts"),
            Item(name = "Sweat Pants", type = "Lower Body", disc = "Sportswear clothing", img = "sweat_pants"),
            Item(name = "Leg Warmers", type = "Lower Body", disc = "Sportswear accessory", img = "socks"),
            Item(name = "Leggings", type = "Lower Body", disc = "Sportswear clothing", img = "leggings")
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

    fun equipItem(itemId: Int, playerId: Int){
        val db = this.writableDatabase

        val values = ContentValues().apply{
            put(_itemId, itemId)
            put(_playerId, playerId)
        }

        db.insert(itemPlayerTableName, null, values)
    }

    fun getEquipItems(playerId: Int): List<EquipedItem> {
        val equipedItems = mutableListOf<EquipedItem>()
        val db = this.writableDatabase

        val cursor = db.rawQuery("SELECT * FROM $itemPlayerTableName " +
                "INNER JOIN $itemTableName ON $itemPlayerTableName.$_itemId = $itemTableName.$itemId " +
                "WHERE $_playerId = $playerId", null)

        with(cursor){
            while (moveToNext()){
                val itemId = getInt(getColumnIndexOrThrow(itemId))
                val itemName = getString(getColumnIndexOrThrow(itemName))
                val itemType = getString(getColumnIndexOrThrow(itemType))
                val itemDisc = getString(getColumnIndexOrThrow(itemDescription))
                val itemImg = getString(getColumnIndexOrThrow(itemImg))
                val equipedItemId = getInt(getColumnIndexOrThrow(itemPlayerId))

                val equipedItem = EquipedItem(itemId, itemName, itemType, itemDisc, itemImg, equipedItemId)
                equipedItems.add(equipedItem)
            }
        }

        cursor.close()

        return equipedItems
    }

    fun unequipItem(itemId: Int){
        val db = this.writableDatabase
        db.delete(itemPlayerTableName,"$_itemId = ?", arrayOf(itemId.toString()))
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

