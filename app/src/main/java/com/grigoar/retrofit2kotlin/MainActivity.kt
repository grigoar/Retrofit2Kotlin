package com.grigoar.retrofit2kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var jsonPlaceHolderApi:JsonPlaceHolderApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //for setting the loggin interceptor
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        //without adding the headers
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        //with logging interceptor
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)
        getPosts()
    }

    private fun getPosts() {
        val parameters: MutableMap<String, String> = HashMap()
        parameters["userId"] = "1"
        parameters["_sort"] = "id"
        parameters["_order"] = "desc"

        //Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{3,4,5},"id", "desc");
        //val call = jsonPlaceHolderApi.getPosts(parameters)
        val call = jsonPlaceHolderApi.getPosts()
        call!!.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    text_view_result.setText("Code: " + response.code())
                    return
                }
                val posts = response.body()
                if (posts != null) {
                    for (post in posts) {
                        text_view_result.text = post.text
                    }
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                text_view_result.setText(t.message)
            }
        })
    }
}