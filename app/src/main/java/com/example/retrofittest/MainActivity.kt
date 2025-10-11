package com.example.retrofittest

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.retrofittest.adapter.Adapter
import com.example.retrofittest.databinding.ActivityMainBinding
import com.example.retrofittest.model.AnimeChar
import com.example.retrofittest.services.AnimeCharAPI
import com.example.retrofittest.services.AnimeCharCount
import com.example.retrofittest.services.RetrofitHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Request

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val retrofit by lazy {
        RetrofitHelper.retrofit
    }
    val listChar = mutableListOf<AnimeChar>()

    var charCount = 1
    var randomNum = 0


    lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = Adapter(mutableListOf()) { animeChar ->
            // clique no item do Recycler
            onClickRV(animeChar)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.btnRoll.isEnabled = false

        MainScope().launch {
            charCount = async { getRandomId() }.await()
            if (charCount > 1) {
                binding.btnRoll.isEnabled = true
            } else {
                Toast.makeText(
                    applicationContext,
                    "Erro ao carregar quantidade de personagens.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.btnRoll.setOnClickListener {
            MainScope().launch {
                binding.btnRoll.isEnabled = false
                binding.bigNameChar.text = "Carregando..."
                randomNum = (1..charCount).random()

                searchValidChar()

                binding.btnRoll.isEnabled = true

            }
        }

    }

    private fun applyLoadImage() {
        Glide.with(applicationContext)
            .load(R.drawable.confused_anime)
            .into(binding.bigAvatarChar)
    }

    private fun onClickRV(animeChar: AnimeChar) {
        binding.apply {
            bigNameChar.text = if (animeChar.nameChar.length > 15) {
                animeChar.nameChar.take(15) + "..."
            } else animeChar.nameChar
            animeChar.nameAnime?.length?.let { length ->
                bigNameAnime.text = if (length > 28) {
                    animeChar.nameAnime?.take(28) + "..."
                } else animeChar.nameAnime
            }
            nameKanji.text = animeChar.nameKanji
            kakeraPoints.text = animeChar.kakeraPoints.toString()
            idChar.text = animeChar.idChar.toString()
            Glide.with(applicationContext)
                .load(
                    if (animeChar.urlImage == RetrofitHelper.urlDefaultImage) {
                        R.drawable.confused_anime_404
                    } else animeChar.urlImage
                )
                .placeholder(R.drawable.confused_anime)
                .error(R.drawable.confused_anime_404)
                .into(binding.bigAvatarChar)

        }
    }

    private suspend fun searchValidChar() {
        var tentativas = 0
        var sucess = false

        applyLoadImage()

        while (!sucess && tentativas < 20) {
            binding.progressBar.visibility = View.VISIBLE
            randomNum = (1..charCount).random()
            sucess = getAnimeChar()
            tentativas++
            Log.i("info_anime", "Tentativa nº$tentativas -- id: $randomNum")
        }
        if (!sucess) {
            Log.i("info_anime", "DEU RUIM -- id: $randomNum")
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "Deu ruim memo ;-;", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private suspend fun getRandomId(): Int = withContext(Dispatchers.IO) {
        try {
            val serviceCharCount = retrofit.create(AnimeCharCount::class.java)
            val response = serviceCharCount.getAnimeCharCount()

            if (response.isSuccessful) response.body()!!.pagination.items.total
            else 1

        } catch (e: Exception) {
            e.printStackTrace()
            1
        }
    }

    private suspend fun getAnimeChar(): Boolean = withContext(Dispatchers.IO) {
        try {
            val service = retrofit.create(AnimeCharAPI::class.java)
            val response = service.getAnimeChar(randomNum)

            if (response.isSuccessful) {
                val animeCharResponse = response.body()?.data ?: return@withContext false
                animeCharResponse.apply {
                    Log.i("info_anime", "anime name : ${anime?.firstOrNull()?.anime?.title}")
                    val animeChar = AnimeChar(
                        nameAnime = if (anime?.firstOrNull()?.anime?.title == null) {
                            "Anime não encontrado"
                        } else anime?.firstOrNull()?.anime?.title,
                        nameChar = name ?: "",
                        idChar = mal_id,
                        nameKanji = name_kanji ?: "",
                        urlImage = images?.jpg?.image_url ?: "",
                        kakeraPoints = favorites ?: 0
                    ).also { animeChar ->
                        withContext(Dispatchers.Main) {
                            adapter.updateList(listChar)

                            binding.apply {
                                bigNameChar.text = if (animeChar.nameChar.length > 15) {
                                    animeChar.nameChar.take(15) + "..."
                                } else animeChar.nameChar
                                animeChar.nameAnime?.length?.let { length ->
                                    bigNameAnime.text = if (length > 28) {
                                        animeChar.nameAnime?.take(28) + "..."
                                    } else animeChar.nameAnime
                                }
                                nameKanji.text = animeChar.nameKanji
                                kakeraPoints.text = animeChar.kakeraPoints.toString()
                                idChar.text = animeChar.idChar.toString()
                                Glide.with(applicationContext)
                                    .load(
                                        if (animeChar.urlImage == RetrofitHelper.urlDefaultImage) {
                                            R.drawable.confused_anime_404
                                        } else animeChar.urlImage
                                    )
                                    .placeholder(R.drawable.confused_anime)
                                    .error(R.drawable.confused_anime_404)
                                    .listener(object: RequestListener<Drawable>{
                                        override fun onResourceReady(
                                            resource: Drawable?,
                                            model: Any?,
                                            target: Target<Drawable?>?,
                                            dataSource: DataSource?,
                                            isFirstResource: Boolean
                                        ): Boolean {
                                            binding.progressBar.visibility = View.GONE
                                            return false
                                        }

                                        override fun onLoadFailed(
                                            e: GlideException?,
                                            model: Any?,
                                            target: Target<Drawable?>?,
                                            isFirstResource: Boolean
                                        ): Boolean {
                                            binding.progressBar.visibility = View.GONE
                                            return false
                                        }
                                    })
                                    .into(bigAvatarChar)
                                listChar.add(animeChar)

                                /*animeCharResponse.apply {
                                    name?.length?.let {
                                        bigNameChar.text = if(it > 15){
                                            name?.take(15) + "..."
                                        }else name
                                    }
//                            bigNameAnime.text = anime?.firstOrNull()?.anime?.title
                                    anime?.firstOrNull()?.anime?.title?.length?.let {lenght ->
                                        bigNameAnime.text = if(lenght > 28){
                                            anime?.firstOrNull()?.anime?.title?.take(28) + "..."
                                        }else anime?.firstOrNull()?.anime?.title.toString()
                                    }
                                    idChar.text = mal_id.toString()
                                    nameKanji.text = name_kanji
                                    kakeraPoints.text = favorites.toString()

                                    Glide.with(applicationContext)
                                        .load(
                                            if ( images?.jpg?.image_url == RetrofitHelper.urlDefaultImage){
                                                R.drawable.confused_anime_404
                                            }else images?.jpg?.image_url
                                        )
                                        .placeholder(R.drawable.confused_anime)
                                        .error(R.drawable.confused_anime_404)
                                        .centerCrop()
                                        .listener(object: RequestListener<Drawable>{
                                            override fun onResourceReady(
                                                resource: Drawable?,
                                                model: Any?,
                                                target: Target<Drawable?>?,
                                                dataSource: DataSource?,
                                                isFirstResource: Boolean
                                            ): Boolean {
                                                binding.progressBar.visibility = View.GONE
                                                return false
                                            }

                                            override fun onLoadFailed(
                                                e: GlideException?,
                                                model: Any?,
                                                target: Target<Drawable?>?,
                                                isFirstResource: Boolean
                                            ): Boolean {
                                                binding.progressBar.visibility = View.GONE
                                                return false
                                            }
                                        })
                                        .into(bigAvatarChar)

                                }*/
                            }
                        }
                    }
                }



                Log.i("info_anime", "✅ Sucesso ID: $randomNum")
                true
            } else {
                Log.i("info_anime", "❌ Erro ID: $randomNum - código ${response.code()}")
                false
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("info_anime", "Erro ao buscar personagem: ${e.message}")
            false
        }
    }
}
