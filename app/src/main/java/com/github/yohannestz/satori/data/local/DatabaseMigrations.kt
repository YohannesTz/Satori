package com.github.yohannestz.satori.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    val migrations: Array<Migration> by lazy {
        arrayOf(
            MIGRATION_1_2
        )
    }

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE UNIQUE INDEX index_search_history_query ON search_history(query)")
        }
    }
}