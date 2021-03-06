package com.abhishekpathak.posts.commons.data

import android.annotation.SuppressLint
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@SuppressLint("ParcelCreator")
data class PostWithUser(@PrimaryKey val postId: Int,
                        val postTitle: String,
                        val postBody: String,
                        val userName: String) : Parcelable {

    fun getFormattedPostBody(): String {
        return if (postBody.length <= 70)
            postBody
        else
            postBody.substring(0, 67) + "..."
    }

    fun getAvatarPhoto() = "https://avatarfiles.alphacoders.com/103/103373.png"
}