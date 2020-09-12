package com.grigoar.retrofit2kotlin

import com.google.gson.annotations.SerializedName

class Post {
    var userId: Int? = 0
    var id: Int? = 0
    var title:String? = null
    @SerializedName("body")
    var text:String? = null

    constructor(userID:Int, title:String,text:String){
        this.userId=userID
        this.title=title
        this.text=text
    }

}