package com.example.myapplication

class IslemAdapterpackage com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IslemAdapter(private var liste: List<FinansalIslem>) : RecyclerView.Adapter<IslemAdapter.IslemViewHolder>() {

    class IslemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAciklama: TextView = view.findViewById(R.id.itemAciklama)
        val tvTarih: TextView = view.findViewById(R.id.itemTarih)
        val tvMiktar: TextView = view.findViewById(R.id.itemMiktar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IslemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_islem, parent, false)
        return IslemViewHolder(view)
    }

    override fun onBindViewHolder(holder: IslemViewHolder, position: Int) {
        val islem = liste[position]
        holder.tvAciklama.text = islem.aciklama
        holder.tvTarih.text = islem.tarih

        // Renklendirme Mantığı: Gelir Yeşil, Gider Kırmızı
        if (islem.islemTuru == "Gelir") {
            holder.tvMiktar.text = "+ ${islem.miktar} TL"
            holder.tvMiktar.setTextColor(Color.parseColor("#2E7D32")) // Koyu Yeşil
        } else {
            holder.tvMiktar.text = "- ${islem.miktar} TL"
            holder.tvMiktar.setTextColor(Color.parseColor("#C62828")) // Koyu Kırmızı
        }
    }

    override fun getItemCount() = liste.size

    fun veriyiGuncelle(yeniListe: List<FinansalIslem>) {
        liste = yeniListe
        notifyDataSetChanged()
    }
} {
}