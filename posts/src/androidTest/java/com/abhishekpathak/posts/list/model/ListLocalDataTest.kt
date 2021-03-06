package com.abhishekpathak.posts.list.model

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.abhishekpathak.posts.commons.testing.DummyData
import com.abhishekpathak.posts.core.testing.TestScheduler
import com.abhishekpathak.posts.commons.data.local.PostDb
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.Rule
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

/**
 *
 * Test for [ListLocalData]
 * Needs to be an instrumented test because Room needs to be tested on a physical device:
 * https://developer.android.com/training/data-storage/room/testing-db.html#android
 *
 * */
@RunWith(AndroidJUnit4::class)
class ListLocalDataTest {

    private lateinit var postDb: PostDb

    private val listLocalData: ListLocalData by lazy { ListLocalData(postDb, TestScheduler()) }

    //Necessary for Room insertions to work
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dummyUsers = listOf(DummyData.User(1), DummyData.User(2))
    private val dummyPosts = listOf(DummyData.Post(1, 1), DummyData.Post(2, 2))

    @Before
    fun init() {
        postDb = Room
                .inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, PostDb::class.java)
                .allowMainThreadQueries()
                .build()
    }

    /**
     * Test that [ListLocalData.getPostsWithUsers] fetches the posts and users in the database
    * */
    @Test
    fun testGetPostsWithUsers() {
        val postsWithUser = listOf(DummyData.PostWithUser(1), DummyData.PostWithUser(2))

        postDb.userDao().upsertAll(dummyUsers)
        postDb.postDao().upsertAll(dummyPosts)

        listLocalData.getPostsWithUsers().test().assertValue(postsWithUser)
    }


    /**
     * Test that [ListLocalData.saveUsersAndPosts] saves the passed lists into the database
     * */
    @Test
    fun saveUsersAndPosts() {

        listLocalData.saveUsersAndPosts(dummyUsers, dummyPosts)

        val users = postDb.userDao().getAll()
        users.test().assertNoErrors().assertValue(dummyUsers)

        val posts = postDb.postDao().getAll()
        posts.test().assertNoErrors().assertValue(dummyPosts)
    }

    @After
    fun clean() {
        postDb.close()
    }
}