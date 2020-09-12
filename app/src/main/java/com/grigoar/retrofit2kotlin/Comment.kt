package com.grigoar.retrofit2kotlin

import com.google.gson.annotations.SerializedName

class Comment {
    var postId:Int=0
    var id:Int = 0
    var name:String?=null
    var email:String? = null
    @SerializedName("body")
    var text:String? = null
}