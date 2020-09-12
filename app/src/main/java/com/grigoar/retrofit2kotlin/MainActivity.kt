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
        //getComments()
        //createPost()
        //updatePost()
        //deletePost()
    }

    private fun getPosts() {
        val parameters: MutableMap<String?, String?> = HashMap()
        parameters["userId"] = "1"
        parameters["_sort"] = "id"
        parameters["_order"] = "desc"

        //Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{3,4,5},"id", "desc");
        //val call = jsonPlaceHolderApi.getPosts(parameters)
        //val call = jsonPlaceHolderApi.getPosts(1)
        //val call = jsonPlaceHolderApi.getPosts(arrayOf(1,2,3),"id","desc")
        val call = jsonPlaceHolderApi.getPosts(parameters)

        call.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    text_view_result.setText("Code: " + response.code())
                    return
                }
                val posts = response.body()
                if (posts != null) {
                    for (post in posts) {
                        val content: StringBuilder = StringBuilder("")
                        content.append("ID: " + post.id + "\n")
                        content.append("UserId: " + post.userId + "\n")
                        content.append("Title: " + post.title + "\n")
                        content.append("Text: " + post.text + "\n\n")
                        text_view_result.append(content)
                    }
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                text_view_result.setText(t.message)
            }
        })
    }

    private fun getComments() {
        //it can be done with or without the base url /posts/3/comments
        //val call =jsonPlaceHolderApi.getComments("https://jsonplaceholder.typicode.com/posts/3/comments")
        val call = jsonPlaceHolderApi.getComments(3)
        call.enqueue(object : Callback<List<Comment?>> {
            override fun onResponse(
                call: Call<List<Comment?>>,
                response: Response<List<Comment?>>
            ) {
                if (!response.isSuccessful) {
                    text_view_result.setText("Code: " + response.code())
                    return
                }
                val comments = response.body()!!
                for (comment in comments) {
                    val content: StringBuilder = StringBuilder("")
                    content.append("Post Id: " + comment?.postId + "\n")
                    content.append("ID: " + comment?.id + "\n")
                    content.append("User name: " + comment?.name + "\n")
                    content.append("User email: " + comment?.email + "\n")
                    content.append("Text: " + comment?.text + "\n\n")
                    text_view_result.append(content)
                }
            }

            override fun onFailure(call: Call<List<Comment?>>, t: Throwable) {
                text_view_result.setText(t.message)
            }
        })
    }

    private fun createPost() {
        val post = Post(23, "New Title", "New Text")
        val fields: MutableMap<String?, String?> = HashMap()
        fields["userId"] = "25"
        fields["title"] = "New title"
        //val call:Call<Post> = jsonPlaceHolderApi.createPost(post)
         val call = jsonPlaceHolderApi.createPost(23,"New Title", "New Text");
        //val call: Call<Post> = jsonPlaceHolderApi.createPost(fields)

        //for executing asynchronously
        call.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                if (!response.isSuccessful) {
                    text_view_result.setText("Code: " + response.code())
                }
                val postResponse: Post? = response.body()

                val content: StringBuilder = StringBuilder("")
                content.append("Server response code: " + response.code() + "\n")
                content.append("ID: " + postResponse?.id + "\n")
                content.append("User ID: " + postResponse?.userId + "\n")
                content.append("Title: " + postResponse?.title + "\n")
                content.append("Text: " + postResponse?.text + "\n\n")
                text_view_result.append(content)
            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                text_view_result.setText(t.message)
            }
        })
    }

    private fun updatePost() {
        val post = Post(12, null, "New Text")

        //patch don't put another object, just update the data sent
        //val call = jsonPlaceHolderApi.patchPost(5,post)
        //val call:Call<Post> = jsonPlaceHolderApi.putPost(3, post);

        //call with header
        val call:Call<Post> = jsonPlaceHolderApi.putPost("abc", 5, post);

        //call with dinamic header
        val headers: MutableMap<String?, String?> = HashMap()
        headers["Map-Header1"] = "def"
        headers["Map-Header2"] = "ghi"
        //val call: Call<Post> = jsonPlaceHolderApi.patchPost(headers, 5, post)

        call.enqueue(object : Callback<Post?> {
            override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
                if (!response.isSuccessful) {
                    text_view_result.setText("Code: " + response.code())
                }
                val postResponse = response.body()

                val content: StringBuilder = StringBuilder("")
                content.append("Server response code: " + response.code() + "\n")
                content.append("ID: " + postResponse?.id + "\n")
                content.append("User ID: " + postResponse?.userId + "\n")
                content.append("Title: " + postResponse?.title + "\n")
                content.append("Text: " + postResponse?.text + "\n\n")
                text_view_result.append(content)

            }

            override fun onFailure(call: Call<Post?>, t: Throwable) {
                text_view_result.setText(t.message)
            }
        })
    }

    private fun deletePost() {
        val call: Call<Void> = jsonPlaceHolderApi.deletePost(5)
        call.enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                text_view_result.setText("Code:" + response.code())
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                text_view_result.setText(t.message)
            }
        })
    }
}