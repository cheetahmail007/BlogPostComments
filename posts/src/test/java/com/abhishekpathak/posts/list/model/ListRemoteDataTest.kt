package  com.abhishekpathak.posts.list.model

import com.abhishekpathak.posts.commons.data.remote.PostService
import com.abhishekpathak.posts.commons.testing.DummyData
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Tests for [ListRemoteData]
 */

@RunWith(RobolectricTestRunner::class)
class ListRemoteDataTest {

    private val postService = mock<PostService>()

    @Test
    fun getPosts() {
        whenever(postService.getPosts()).thenReturn(
            Single.just(
                listOf(
                    DummyData.Post(1, 101),
                    DummyData.Post(2, 102)
                )
            )
        )

        ListRemoteData(postService).getPosts().test().run {
            assertNoErrors()
            assertValueCount(1)
            assertEquals(values()[0].size, 2)
            assertEquals(values()[0][0].userId, 1)
            assertEquals(values()[0][0].postId, 101)
            assertEquals(values()[0][1].userId, 2)
            assertEquals(values()[0][1].postId, 102)

        }
    }

    @Test
    fun getUsers() {
        whenever(postService.getUsers()).thenReturn(
            Single.just(
                listOf(
                    DummyData.User(201),
                    DummyData.User(202)
                )
            )
        )

        ListRemoteData(postService).getUsers().test().run {
            assertNoErrors()
            assertValueCount(1)
            assertEquals(values()[0].size, 2)
            assertEquals(values()[0][0].id, 201)
            assertEquals(values()[0][1].id, 202)
        }
    }
}