package com.example.retrofittest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.retrofittest.adapter.Adapter
import com.example.retrofittest.databinding.ActivityMainBinding
import com.example.retrofittest.model.AnimeChar

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        val adapter = Adapter(emptyList()){animeChar ->
            with(binding) {
                bigNameChar.text = animeChar.nameChar
                bigNameAnime.text = animeChar.nameAnime
                idChar.text = animeChar.idChar.toString()

                if (animeChar.sexChar == 'M'){
                    imgSex.setImageResource(R.drawable.sex_masc)
                    /*Glide.with(binding.root.context)
                        .load(R.drawable.sex_masc)
                        .placeholder(R.drawable.sex_masc)
                        .centerCrop()
                        .into(binding.imgSex)*/
                }else{
                    imgSex.setImageResource(R.drawable.sex_fem)
                }

            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter


    }
}