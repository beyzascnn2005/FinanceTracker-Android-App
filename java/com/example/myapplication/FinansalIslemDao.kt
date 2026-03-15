package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FinansalIslemDao {

    @Insert
    fun islemEkle(islem: FinansalIslem) // Tek satırla veritabanına kayıt işlemi!

    @Query("SELECT * FROM finansal_islemler_tablosu ORDER BY id DESC")
    fun tumIslemleriGetir(): List<FinansalIslem> // Bütün verileri liste halinde çeker
}