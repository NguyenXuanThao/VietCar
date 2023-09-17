package com.example.vietcar.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vietcar.data.model.search_history.SearchHistoryEntity


@Database(entities = [SearchHistoryEntity::class], version = 1, exportSchema = true)
abstract class CarDatabase : RoomDatabase() {

    abstract fun getCarDao(): CarDao

    companion object {

        @Volatile
        private var INSTANCE: CarDatabase? = null

        fun getInstance(context: Context): CarDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                CarDatabase::class.java,
                "search_database"
            ).build()
                .also {
                    INSTANCE = it
                }
        }
    }


}