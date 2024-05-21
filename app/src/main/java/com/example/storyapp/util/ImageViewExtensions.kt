package com.example.storyapp.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.storyapp.R

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context)
        .load(url)
        .error(R.drawable.wawan)
        .centerCrop()
        .into(this)
}