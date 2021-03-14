package com.satragni.domain

data class CommentResponse(
    val comment: String,
    val commentId: Long,
    val imageId: String,
    val timeStamp: Long
)