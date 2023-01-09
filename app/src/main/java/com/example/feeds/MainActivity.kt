package com.example.feeds

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_news.*

class MainActivity : AppCompatActivity(), NewItemClicked {

    private lateinit var mAdapter: NewsListAdapter
    private lateinit var mNewsArray: ArrayList<News>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter
    }
    private fun fetchData(){
        val url = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=307219f4f3944c26ab19ec8ee17c37d7"
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                val mNewsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("content"),
                        newsJsonObject.getString("urlToImage"),
                        newsJsonObject.getJSONObject("source")
                    )
                    mNewsArray.add(news)
                }
                mAdapter.updateNews(mNewsArray)
            },
            {
                Log.d("Error: ", it.toString())
            }
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["User-Agent"] = "Mozilla/5.0"
                return params
            }
        }
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }

    override fun onShareButtonClicked(item: News, imageView: ImageView) {
        val url = item.url
        val bitmapDrawable = imageView!!.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap,"tempimage", null)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/*"
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_TEXT, item.title+"\n"+url)
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))

        val chooser = Intent.createChooser(intent, "Share News")
        startActivity(chooser)
    }

}