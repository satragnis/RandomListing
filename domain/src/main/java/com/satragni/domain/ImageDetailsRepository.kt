package com.satragni.domain

interface ImageDetailsRepository {
    suspend fun getComments(imageId: String,page:Int): Pair<List<CommentResponse>,Boolean>
    suspend fun postComments(postCommentRequestDraft: PostCommentRequest) : CommentResponse
}