package com.example.feeds

import org.json.JSONObject

data class News(
    val title: String,
    val author: String,
    val url: String,
    val content: String,
    val imageUrl: String,
    val source: JSONObject
)