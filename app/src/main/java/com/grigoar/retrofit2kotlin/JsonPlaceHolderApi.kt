package com.grigoar.retrofit2kotlin

import retrofit2.Call
import retrofit2.http.*

interface JsonPlaceHolderApi {
    //GET 	/posts
    @GET("posts")
    fun getPosts(): Call<List<Post>>

    //for not hardcoded url's
    //can be added multiple paths
    //GET 	/posts/1/comments
    @GET("posts/{id}/comments")
    fun getComments(@Path("id") postId: Int): Call<List<Comment?>>

    //add a new post
    @POST("posts")
    fun createPost(@Body post: Post): Call<Post>

    /*update an existing resource
    replacing completely the object we send over
    maybe it create a new one*/
    @PUT("posts/{id}")
    fun putPost(@Path("id") id: Int, @Body post: Post): Call<Post>

    /*update an existing resource
    replacing only the field of the objects*/
    @PATCH("posts/{id}")
    fun patchPost(@Path("id") id: Int, @Body post: Post): Call<Post>


    //return an empty body for the call request
    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Call<Void>

    //----TESTING different situations---------

    /*GET 	/posts?userId=1
    The Query annotation put a ? in after the posts automatically*/
    @GET("posts")
    fun getPosts(@Query("userId") userId: Int):Call<List<Post>>


    /*getting posts with multiple query parameters
    posts?userId=1&_sort=id&_order=desc
    posts?userId=1&id=5
    if we want to pass a nullable for used parameteres we use nullable
    if we want 2 users in the same call
    the queries are send directly through URL*/
    @GET("posts")
    fun getPosts(
        @Query("userId") userId: Array<Int?>?,  // @Query("userId") userId2:Int ,
        @Query("_sort") sort: String?,
        @Query("_order") order: String?
    ): Call<List<Post>>


    /*if we want to put the parameters only when we call the method we use the Map and define
     later the parameters*/
    @GET("posts")
    fun getPosts(@QueryMap parameters: MutableMap<String?, String?>?): Call<List<Post>>


    /*if we want to send the fields of an object field by field
    for sending the data encoded when the server ask for this type
    type: userId=23&title=New%20Title&body=New%20Text
    the data is send through the http body not through the json*/
    @FormUrlEncoded
    @POST("posts")
    fun createPost(
        @Field("userId") userId: Int,
        @Field("title") title: String?,
        @Field("body") text: String?
    ): Call<Post?>

    /*we can send a list @field new list
    can use the paths{id}...
    we can send all the fields through the FieldMap*/
    @FormUrlEncoded
    @POST("posts")
    fun createPost(@FieldMap fields: MutableMap<String?, String?>?): Call<Post>


    //update an existing resource
    //replacing completly the objecty we send over
    //maybe it create a new one
    @Headers("Static-Header: 123", "Static-Header2: 456")
    @PUT("posts/{id}")
    fun putPost(@Header("Dynamic-Header") header: String, @Path("id") id: Int, @Body post: Post):Call<Post>


    //for putting the parameters as a map
    @PATCH("posts/{id}")
    fun patchPost(
        @HeaderMap headers: Map<String?, String?>?,
        @Path("id") id: Int,
        @Body post: Post?
    ): Call<Post>
}