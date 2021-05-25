package ru.jasfex.moex.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * @param date date in format YYYY-MM-DD.
 * @param value true, if not empty listing was loaded; false, if empty listing was loaded;
 */
@Entity(
    tableName = "has_listing_table"
)
data class HasListingEntity(
    @PrimaryKey val date: String,
    val value: Boolean
)