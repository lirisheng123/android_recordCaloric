package com.example.homework2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.jetbrains.anko.db.*
import java.sql.Types.VARCHAR

//class test() : SQLiteOpenHelper() {
////    override fun onCreate(db: SQLiteDatabase?) {
////        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID Integer PRIMARY KEY, $FIRST_NAME TEXT, $LAST_NAME TEXT)"
////        db?.execSQL(CREATE_TABLE)
////    }
//}
//fun main(args:Array<String>){
//
//}
class MySqlHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "mydb") {

    companion object {
        private var instance: MySqlHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MySqlHelper {
            if (instance == null) {
                instance = MySqlHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("Customer1", true,
//            "id" to INTEGER+ PRIMARY_KEY,
            "year" to INTEGER,
            "month" to INTEGER,
            "day" to INTEGER,
            "calorie" to INTEGER
            )

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    val Context.database: MySqlHelper
        get() = MySqlHelper.getInstance(applicationContext)

}




