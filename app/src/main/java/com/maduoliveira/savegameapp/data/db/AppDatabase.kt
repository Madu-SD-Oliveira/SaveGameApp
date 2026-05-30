package com.maduoliveira.savegameapp.data.db

import com.maduoliveira.savegameapp.data.dao.AccountDao
import com.maduoliveira.savegameapp.data.dao.CategoryDao
import com.maduoliveira.savegameapp.data.dao.TransactionDao
import com.maduoliveira.savegameapp.data.entity.AccountEntity
import com.maduoliveira.savegameapp.data.entity.CategoryEntity
import com.maduoliveira.savegameapp.data.entity.ChannelEntity
import com.maduoliveira.savegameapp.data.entity.TransactionEntity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.maduoliveira.savegameapp.data.dao.ChannelDao
import com.maduoliveira.savegameapp.data.dao.ReportsDao
import com.maduoliveira.savegameapp.data.dao.SectorDao
import com.maduoliveira.savegameapp.data.entity.SectorEntity

@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        TransactionEntity::class,
        SectorEntity::class,
        ChannelEntity::class
   ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun reportsDao(): ReportsDao
    abstract fun sectorDao(): SectorDao
    abstract fun channelDao(): ChannelDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE transactions ADD COLUMN observation TEXT NOT NULL DEFAULT ''"
                )
            }
        }
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "savegame_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}