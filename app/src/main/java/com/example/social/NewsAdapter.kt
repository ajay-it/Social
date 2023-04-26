package com.example.social

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.social.databinding.ItemLayoutBinding
import okhttp3.Callback
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class NewsAdapter(val context: Context, val news: List<NewsModel>) :
RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = news[position]
        holder.binding.textView14.text = news.title
        holder.binding.textView15.text = news.description
        holder.binding.textView12.text = formatTime(news.publishedAt.toString())
        holder.binding.textView13.text = news.source
        Glide.with(context).load(news.imageUrl).into(holder.binding.imageView4)
    }

    inner class NewsViewHolder(val binding: ItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTime(time: String): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val parsedTime = LocalDateTime.parse(time, formatter)
        val now = LocalDateTime.now()
        val timeDiff = parsedTime.until(now, ChronoUnit.MINUTES)

        return when {
            timeDiff < 1 -> "Just now"
            timeDiff < 60 -> "$timeDiff min ago"
            timeDiff < 1440 -> "${timeDiff / 60} hours ago"
            else -> "${timeDiff / 1440} days ago"
        }
    }
}