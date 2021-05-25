package ru.jasfex.moex.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.jasfex.moex.data.local.model.HasListingEntity
import ru.jasfex.moex.data.local.model.ListingEntity

@Database(
    entities = [ListingEntity::class, HasListingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MoscowExchangeDatabase : RoomDatabase() {

    abstract fun listingDao(): ListingDao

    companion object {
        @Volatile
        private var INSTANCE: MoscowExchangeDatabase? = null

        const val DATABASE_NAME = "moscow_exchange.db"

        fun getInstance(context: Context): MoscowExchangeDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MoscowExchangeDatabase::class.java,
                    DATABASE_NAME
                )
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}