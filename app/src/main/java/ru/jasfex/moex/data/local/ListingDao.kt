package ru.jasfex.moex.data.local

import androidx.room.*
import ru.jasfex.moex.data.local.model.HasListingEntity
import ru.jasfex.moex.data.local.model.ListingEntity

@Dao
interface ListingDao {

    @Query("SELECT * FROM listing_table WHERE date = :date ORDER BY date, securityId")
    fun getListing(date: String): List<ListingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveListing(item: ListingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveListing(items: List<ListingEntity>)

    @Delete
    suspend fun deleteListing(item: ListingEntity)

    @Query("SELECT * FROM has_listing_table WHERE date = :date")
    fun hasListing(date: String): HasListingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setHasListing(hasListing: HasListingEntity)

    @Query("DELETE FROM has_listing_table WHERE date = :date")
    suspend fun deleteHasListing(date: String)

    @Query("SELECT * FROM has_listing_table ORDER BY date DESC")
    fun getCalendar(): List<HasListingEntity>

}