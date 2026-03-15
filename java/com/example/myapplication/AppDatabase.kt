package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FinansalIslem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // DAO'muzu veritabanına bağlıyoruz
    abstract fun islemDao(): FinansalIslemDao

    // Singleton Pattern (Tekil Nesne) uygulaması
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "finansal_veritabani" // Cihazda oluşacak fiziksel dosyanın adı
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}