package com.ysanjeet535.photomediaapp.models

data class Post (
    var caption:String="",
    var image_url:String="",
    var creation_time :Long = 0,
    var user:User? =null
)

