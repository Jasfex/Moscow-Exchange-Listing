package ru.jasfex.moex

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ListingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ListingDatabase : RoomDatabase() {

    abstract fun listingDao(): ListingDao

    companion object {
        @Volatile
        private var INSTANCE: ListingDatabase? = null

        fun getInstance(context: Context): ListingDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ListingDatabase::class.java,
                    "listing.db"
                )
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}