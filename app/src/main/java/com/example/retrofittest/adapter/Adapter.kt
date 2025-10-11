package com.example.retrofittest.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.retrofittest.R

import com.example.retrofittest.databinding.ItemRvBinding
import com.example.retrofittest.model.AnimeChar
import com.example.retrofittest.services.RetrofitHelper

class Adapter(
    private var charList: List<AnimeChar>,
    private val click:(animeChar: AnimeChar) -> Unit
) : RecyclerView.Adapter<Adapter.AdapterVH>() {
    inner class AdapterVH(
        private val binding: ItemRvBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(animeChar: AnimeChar){
            binding.apply {
                nameChar.text = if (animeChar.nameChar.length > 15){
                    animeChar.nameChar.take(15) + "..."
                } else animeChar.nameChar

                if (!animeChar.nameAnime.isNullOrEmpty()){
                    nameAnime.text = if (animeChar.nameAnime.length > 15) {
                        animeChar.nameAnime.toString().take(24) + "..."
                    } else animeChar.nameAnime.toString()
                } else nameAnime.text = "Anime não encontrado"
                idChar.text = animeChar.idChar.toString()
                kakeraPoints.text = animeChar.kakeraPoints.toString()

            }


            Glide.with(binding.root.context)
                .load(
                    if (animeChar.urlImage == RetrofitHelper.urlDefaultImage){
                        R.drawable.confused_anime_404
                    }else animeChar.urlImage)
                .placeholder(R.drawable.confused_anime)
                .error(R.drawable.confused_anime)
                .centerCrop()
                .listener(object: RequestListener<Drawable>{
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar2.visibility = View.GONE
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar2.visibility = View.GONE
                        return false
                    }
                })
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

    /*fun addToList(newItem: AnimeChar){
        (charList as MutableList).add(newItem)
        notifyItemChanged(charList.size - 1)
        Log.i("info_anime", "------------------------")
        for (char in charList){
            Log.i("info_anime", char.nameChar)
        }
        Log.i("info_anime", "------------------------")
    }*/

    fun updateList(newAnimeCharList: List<AnimeChar>){
        charList = newAnimeCharList
        notifyDataSetChanged()


    }

}