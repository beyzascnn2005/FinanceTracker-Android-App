package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ... mevcut importların altına şunları ekle ...
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView



class MainActivity : AppCompatActivity() {
    private lateinit var adapter: IslemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Arayüzdeki (XML) elemanları kod tarafına bağlıyoruz
        val etMiktar = findViewById<EditText>(R.id.etMiktar)
        val etAciklama = findViewById<EditText>(R.id.etAciklama)
        val rgIslemTuru = findViewById<RadioGroup>(R.id.rgIslemTuru)
        val btnKaydet = findViewById<Button>(R.id.btnKaydet)
        val tvToplamGelir = findViewById<TextView>(R.id.tvToplamGelir)
        val tvToplamGider = findViewById<TextView>(R.id.tvToplamGider)
        val rvIslemler = findViewById<RecyclerView>(R.id.rvIslemler)

        // 2. Veritabanı ve DAO nesnemizi çağırıyoruz (Singleton mimarimiz çalışıyor)
        val db = AppDatabase.getDatabase(this)
        val dao = db.islemDao()

        // RecyclerView Kurulumu
        adapter = IslemAdapter(emptyList())
        rvIslemler.layoutManager = LinearLayoutManager(this)
        rvIslemler.adapter = adapter
        // Verileri Yükleme ve Özet Hesaplama Fonksiyonu
        fun verileriYükle() {
            CoroutineScope(Dispatchers.IO).launch {
                val tumIslemler = dao.tumIslemleriGetir() // DAO'na bu metodu eklemelisin

                var gelirToplami = 0.0
                var giderToplami = 0.0

                tumIslemler.forEach {
                    if (it.islemTuru == "Gelir") gelirToplami += it.miktar
                    else giderToplami += it.miktar
                }

                withContext(Dispatchers.Main) {
                    adapter.veriyiGuncelle(tumIslemler)
                    tvToplamGelir.text = "$gelirToplami TL"
                    tvToplamGider.text = "$giderToplami TL"
                }
            }
        }

        // Uygulama ilk açıldığında verileri getir
        verileriYükle()

        // 3. Kaydet Butonuna Tıklanma Olayı (Listener)
        btnKaydet.setOnClickListener {


            val miktarStr = etMiktar.text.toString()
            val aciklama = etAciklama.text.toString()

            // Boş alan kontrolü (Kullanıcı yanlışlıkla boş basarsa uygulama çökmesin)
            if (miktarStr.isEmpty() || aciklama.isEmpty()) {
                Toast.makeText(this, "Lütfen miktar ve açıklama girin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // İşlemi burada kes
            }

            val miktar = miktarStr.toDouble()

            // Gelir mi Gider mi seçilmiş onu buluyoruz
            val seciliRadioId = rgIslemTuru.checkedRadioButtonId
            val seciliRadio = findViewById<RadioButton>(seciliRadioId)
            val islemTuru = seciliRadio.text.toString()

            // O anki tarihi otomatik alıyoruz (Örn: 04.03.2026)
            val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val tarih = format.format(Date())
            val currentDateTime = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date())

            // 4. Veritabanına kaydedilecek nesnemizi oluşturuyoruz
            val yeniIslem = FinansalIslem(
                islemTuru = islemTuru,
                miktar = miktar,
                aciklama = aciklama,
                tarih = tarih
            )

            // 5. Arka Planda (Background Thread) Veritabanına Kayıt İşlemi
            CoroutineScope(Dispatchers.IO).launch {
                dao.islemEkle(yeniIslem)

                // Kayıt bitince arayüze (Main Thread) dönüp kullanıcıya bilgi veriyoruz
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Başarıyla Kaydedildi!", Toast.LENGTH_SHORT).show()
                    // Yeni kayıt için kutuların içini temizliyoruz
                    etMiktar.text.clear()
                    etAciklama.text.clear()
                    verileriYükle() // Kayıttan sonra listeyi ve özeti yenile!
                }
            }
        }
    }
}