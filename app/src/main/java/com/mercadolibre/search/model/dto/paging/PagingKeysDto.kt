package com.mercadolibre.search.model.dto.paging

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paging_keys")
data class PagingKeysDto(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "prev_key") var prevKey: Int?,
    @ColumnInfo(name = "next_key") var nextKey: Int?
)