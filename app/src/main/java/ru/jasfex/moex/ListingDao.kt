package ru.jasfex.moex

import androidx.room.*

@Dao
interface ListingDao {

    @Query("SELECT * FROM listing_table ORDER BY date, securityId")
    fun getListing(): List<ListingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveListing(item: ListingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveListing(items: List<ListingEntity>)

    @Delete
    suspend fun deleteListing(item: ListingEntity)

}