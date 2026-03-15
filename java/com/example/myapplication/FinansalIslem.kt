package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "finansal_islemler_tablosu")
data class FinansalIslem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // ID'yi veritabanı otomatik artıracak (Auto Increment)
    val islemTuru: String,  // "Gelir" veya "Gider"
    val miktar: Double,
    val aciklama: String,
    val tarih: String
)