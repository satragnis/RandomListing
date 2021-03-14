package com.satragni.datarepository.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.satragni.datarepository.storage.Comment
import com.satragni.datarepository.storage.CommentDao

@Database(entities = [Comment::class], version = 1, exportSchema = false)
abstract class ImageDb : RoomDatabase() {
    abstract fun commentDao(): CommentDao?
}

