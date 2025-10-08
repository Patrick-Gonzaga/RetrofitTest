package com.example.retrofittest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofittest.R

import com.example.retrofittest.databinding.ItemRvBinding
import com.example.retrofittest.model.AnimeChar

class Adapter(
    private var charList: List<AnimeChar>,
    private val click:(animeChar: AnimeChar) -> Unit
) : RecyclerView.Adapter<Adapter.AdapterVH>() {
    inner class AdapterVH(
        private val binding: ItemRvBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(animeChar: AnimeChar){
            binding.nameChar.text = animeChar.nameChar
            binding.nameAnime.text = animeChar.nameAnime
            binding.idChar.text = animeChar.idChar.toString()

            Glide.with(binding.root.context)
                .load(animeChar.urlImage)
                .placeholder(R.drawable.naruto_img)
                .error(R.drawable.confused_anime)
                .centerCrop()
                .into(binding.avatarAnime)

            binding.rvItemLayout.setOnClickListener {
                click(animeChar)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterVH {
        val binding = ItemRvBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AdapterVH(binding)
    }

    override fun onBindViewHolder(
        holder: AdapterVH,
        position: Int
    ) {
        val charAnime = charList[position]
        holder.bind(charAnime)
    }

    override fun getItemCount() = charList.size

    fun updateList(newAnimeCharList: List<AnimeChar>){
        charList = newAnimeCharList
        notifyDataSetChanged()

    }

}