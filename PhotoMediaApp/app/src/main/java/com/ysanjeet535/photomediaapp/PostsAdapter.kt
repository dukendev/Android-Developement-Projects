package com.ysanjeet535.photomediaapp

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ysanjeet535.photomediaapp.models.Post


class PostsAdapter(val context: Context,val posts:List<Post>):
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindpost(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvusername = itemView.findViewById<TextView>(R.id.tvUsername)
        val relativetime = itemView.findViewById<TextView>(R.id.tvRelativetime)
        val caption = itemView.findViewById<TextView>(R.id.tvCaption)
        val image = itemView.findViewById<ImageView>(R.id.ivPost)

        fun bindpost(post: Post) {
            tvusername.text = post.user?.username
            caption.text = post.caption
            Glide.with(context).load(post.image_url).into(image)
            relativetime.text = DateUtils.getRelativeTimeSpanString(post.creation_time)

        }


    }



}