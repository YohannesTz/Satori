package com.github.yohannestz.satori.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    val migrations: Array<Migration> by lazy {
        arrayOf(
            MIGRATION_1_2,
            MIGRATION_2_3
        )
    }

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_search_history_query ON search_history(query)")
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            val cursorTitle = db.query("PRAGMA table_info(item)")
            val hasTitleColumn = cursorTitle.use {
                var columnExists = false
                while (it.moveToNext()) {
                    val columnName = it.getString(it.getColumnIndexOrThrow("name"))
                    if (columnName == "title") {
                        columnExists = true
                        break
                    }
                }
                columnExists
            }

            if (!hasTitleColumn) {
                db.execSQL("ALTER TABLE item ADD COLUMN title TEXT")
            }

            val cursorImageUrl = db.query("PRAGMA table_info(item)")
            val hasImageUrlColumn = cursorImageUrl.use {
                var columnExists = false
                while (it.moveToNext()) {
                    val columnName = it.getString(it.getColumnIndexOrThrow("name"))
                    if (columnName == "imageUrl") {
                        columnExists = true
                        break
                    }
                }
                columnExists
            }

            if (!hasImageUrlColumn) {
                db.execSQL("ALTER TABLE item ADD COLUMN imageUrl TEXT")
            }
        }
    }

}