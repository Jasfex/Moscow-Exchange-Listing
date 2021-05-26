package ru.jasfex.moex.data.local

import androidx.room.*
import ru.jasfex.moex.data.local.converters.CandleTimeIntervalConverter
import ru.jasfex.moex.data.local.model.CandleEntity
import ru.jasfex.moex.data.local.model.HasListingEntity
import ru.jasfex.moex.data.local.model.ListingEntity
import ru.jasfex.moex.domain.model.CandleTimeInterval

@Dao
@TypeConverters(CandleTimeIntervalConverter::class)
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

    @Query("SELECT * FROM candles_table WHERE securityId = :securityId AND date = :date AND timeInterval = :timeInterval ORDER BY timeStart")
    fun getCandles(securityId: String, date: String, timeInterval: CandleTimeInterval): List<CandleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCandles(candles: List<CandleEntity>)

}