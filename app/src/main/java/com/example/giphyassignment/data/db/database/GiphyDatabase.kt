package com.example.giphyassignment.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.giphyassignment.data.db.dao.FavoriteDao
import com.example.giphyassignment.data.db.entity.FavoriteItem
import com.example.giphyassignment.utilities.Constants.DATABASE_NAME

@Database(entities = [FavoriteItem::class], version = 2, exportSchema = false)
abstract class GiphyDatabase: RoomDatabase() {
    abstract fun getFavoritesDao(): FavoriteDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: GiphyDatabase? = null

        fun getDatabase(context: Context): GiphyDatabase {
            // if the INSTANCE is not null, then return it, if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GiphyDatabase::class.java, DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}