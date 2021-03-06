package  com.abhishekpathak.posts.details.model

import com.abhishekpathak.posts.commons.data.remote.PostService
import com.abhishekpathak.posts.commons.testing.DummyData
import com.abhishekpathak.posts.list.model.ListRemoteData
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
class DetailsRemoteDataTest {
    private val postService = mock<PostService>()

    @Test
    fun getCommentsForPost() {
        val userId = 1
        whenever(postService.getComments(userId)).thenReturn(
            Single.just(
                listOf(
                    DummyData.Comment(userId, 101),
                    DummyData.Comment(userId, 102),
                    DummyData.Comment(userId, 103)
                )
            )
        )

        DetailsRemoteData(postService).getCommentsForPost(userId).test().run {
            assertNoErrors()
            assertValueCount(1)
            assertEquals(values()[0].size, 3)
            assertEquals(values()[0][0].id, 101)
            assertEquals(values()[0][1].id, 102)
            assertEquals(values()[0][2].id, 103)
        }
    }
}