package com.satragni.datarepository

import com.satragni.datarepository.storage.Comment
import com.satragni.datarepository.storage.ImageDb
import com.satragni.domain.CommentResponse
import com.satragni.domain.ImageDetailsRepository
import com.satragni.domain.PostCommentRequest
import javax.inject.Inject

class ImageDetailsRepositoryImpl @Inject constructor(
    private val mImageDb: ImageDb
) : ImageDetailsRepository {

    private var allComments = mutableListOf<CommentResponse>()

    companion object {
        const val PAGE_SIZE = 10
    }


    override suspend fun getComments(
        imageId: String,
        page: Int
    ): Pair<List<CommentResponse>, Boolean> {
        if (page == 1) {
            allComments.clear()
        }

        val resultFromDB: Collection<CommentResponse> = mImageDb.commentDao()?.getComments(
            imageId,
            PAGE_SIZE,
            (page - 1) * PAGE_SIZE
        ) ?: emptyList()
        allComments.addAll(
            resultFromDB
        )
        return Pair(allComments, resultFromDB.isEmpty())


    }

    override suspend fun postComments(postCommentRequestDraft: PostCommentRequest):CommentResponse {
        mImageDb.commentDao()?.insert(
            Comment(
                imageId = postCommentRequestDraft.imageId,
                comment = postCommentRequestDraft.comment,
                timeStamp = System.currentTimeMillis()
            )
        )
        //fetch last comment only
        val comments = mImageDb.commentDao()?.getComments(postCommentRequestDraft.imageId, 1, 0) ?: emptyList()

        allComments.addAll(0,comments)
        return comments[0]
    }
}