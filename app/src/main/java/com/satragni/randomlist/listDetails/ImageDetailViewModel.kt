package com.satragni.randomlist.listDetails

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.satragni.domain.CommentResponse
import com.satragni.domain.GetCommentUseCase
import com.satragni.domain.PostCommentRequest
import com.satragni.domain.PostCommentUseCase
import com.satragni.randomlist.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageDetailViewModel @Inject constructor(
    private val postCommentUseCase: PostCommentUseCase,
    private val getCommentUseCase: GetCommentUseCase
) : BaseViewModel() {

    private lateinit var mImageId: String
    var imageUrl = ObservableField<String>()
    var snackBarMessage = ObservableField<String>()

    var comments = MutableLiveData<List<CommentResponse>>()

    private var isLastPage = false
    private var page = 1

    fun getCommentsFromDb() {
        if (isLastPage.not()) {
            viewModelScope.launch {
                val (commentListFromDb, lastPage) = withContext(Dispatchers.IO) {
                    getCommentUseCase(
                        mImageId,
                        page
                    )
                }
                comments.value = commentListFromDb
                isLastPage = lastPage
                if (lastPage.not()) {
                    page++
                }
            }
        }
    }

    fun postComment(comment: String?) {
        if (comment.isNullOrBlank()) {
            this.snackBarMessage.set("Please enter comment")
            return
        }
        viewModelScope.launch() {
            val lastComment = withContext(Dispatchers.IO) {
                postCommentUseCase(
                    PostCommentRequest(
                        mImageId,
                        comment
                    )
                )
            }
            val toMutableList = comments.value?.toMutableList()
            toMutableList?.add(0, lastComment)
            comments.value = toMutableList
        }
    }

    fun shareImage() {
        viewModelScope.launch() {
          withContext(Dispatchers.IO) {

            }
        }
    }




    fun setImageData(imageId: String?, imageUrl: String?) {
        this.imageUrl.set(imageUrl)
        this.mImageId = imageId ?: ""
        getCommentsFromDb()
    }
}