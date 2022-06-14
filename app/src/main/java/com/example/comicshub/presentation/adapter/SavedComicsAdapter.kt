package com.example.comicshub.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.comicshub.R
import com.example.comicshub.data.model.APIResponse
import com.example.comicshub.databinding.SavedComicItemBinding

class SavedComicsAdapter (
    val savedComicsList : List<APIResponse>,
    val adapterOnItemClickListener: AdapterOnItemClickListener
    ) : RecyclerView.Adapter<SavedComicsAdapter.SavedComicsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedComicsViewHolder {
        val binding = SavedComicItemBinding.inflate(LayoutInflater.from(parent.context))
        return SavedComicsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedComicsViewHolder, position: Int) {
        holder.bind(savedComicsList[position], holder.itemView)
    }

    override fun getItemCount(): Int {
        return savedComicsList.size
    }

    inner class SavedComicsViewHolder(private val binding : SavedComicItemBinding) :
        RecyclerView.ViewHolder(binding.root) {



            fun bind(savedComicItem: APIResponse, view: View){
                val date = "${savedComicItem.day}/${savedComicItem.month}/${savedComicItem.year}"
                binding.apply {
                    savedComicTitle.text = savedComicItem.title
                    savedComicPublishedAt.text = date
                    savedComicTranscript.text = savedComicItem.transcript
//                    savedComicSource.text = savedComicItem.transcript
                    Glide.with(savedComicImage.context)
                        .load(savedComicItem.img)
                        .apply( RequestOptions().override(1000, 500))
                        .fallback(R.drawable.fav_white_heart)
                        .into(savedComicImage)
                }
                adapterOnItemClickListener.onItemClicked(savedComicItem, adapterPosition, view)
            }
    }

    interface AdapterOnItemClickListener{
        fun onItemClicked(apiResponse: APIResponse, position: Int, view: View)
    }


}