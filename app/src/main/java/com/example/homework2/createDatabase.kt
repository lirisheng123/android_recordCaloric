package com.example.homework2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

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
        db.createTable("Customer2", true,
            "id" to INTEGER+ PRIMARY_KEY+ AUTOINCREMENT,
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




