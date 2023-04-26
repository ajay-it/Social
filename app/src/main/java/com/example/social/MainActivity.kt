package com.example.social

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.social.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var signedInUser: User? = null
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var news: MutableList<NewsModel>
//    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null){
            goLoginActivity()
        }
        if(auth.currentUser != null){
            supportActionBar?.title = auth.currentUser.toString()
        }

        // API Data Fetching
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://newsapi.org/v2/everything?q=apple&from=2023-04-25&to=2023-04-25&sortBy=popularity&apiKey=ca7056c914c140a78de53830824ddb3e")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val jsonObject = JSONObject(body)
                val jsonArray = jsonObject.getJSONArray("articles")

                val articles = mutableListOf<NewsModel>()

                for (i in 0 until jsonArray.length()) {
                    val articleObject = jsonArray.getJSONObject(i)
                    val articleSource = articleObject.getJSONObject("source")
                    val article = NewsModel(
                        title = articleObject.getString("title"),
                        description = articleObject.getString("description"),
                        publishedAt = articleObject.getString("publishedAt"),
                        source = articleSource.getString("name"),
                        imageUrl = articleObject.getString("urlToImage")
                    )
                    articles.add(article)
                }

                runOnUiThread {
                    binding.rvNews.adapter = NewsAdapter(applicationContext, articles)
                    binding.rvNews.layoutManager = LinearLayoutManager(applicationContext)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "Error fetching news", e)
            }
        })




    }

    private fun goLoginActivity() {
        Log.i(TAG, "goLoginActivity")
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
        finish()
    }
}