package com.example.feeds

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.file.FileResource
import kotlinx.android.synthetic.main.item_news.view.*

class NewsListAdapter(private val listener: NewItemClicked) : RecyclerView.Adapter<NewsViewHolder> (){

    private val items: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener{
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        viewHolder.share.setOnClickListener{
            listener.onShareButtonClicked(items[viewHolder.adapterPosition], viewHolder.image)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.title
        holder.content.text = currentItem.content
        holder.source.text = currentItem.source["name"].toString()
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.image)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateNews(updatedNews: ArrayList<News>){
        items.clear()
        items.addAll(updatedNews)

        notifyDataSetChanged()
    }
}

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleView: TextView = itemView.title
    val image: ImageView = itemView.image
    val source: TextView = itemView.source
    val content: TextView = itemView.content
    val share: Button = itemView.share
}

interface NewItemClicked{
    fun onItemClicked(item : News)
    fun onShareButtonClicked(item: News, imageView: ImageView)
}
