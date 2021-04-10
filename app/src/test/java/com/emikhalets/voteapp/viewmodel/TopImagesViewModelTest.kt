package com.emikhalets.voteapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emikhalets.voteapp.TestCoroutineRule
import com.emikhalets.voteapp.any
import com.emikhalets.voteapp.getOrAwaitValue
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.observeForTesting
import com.emikhalets.voteapp.utils.AppResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
class TopImagesViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TopImagesViewModel
    private val databaseRepository = Mockito.mock(FirebaseDatabaseRepository::class.java)

    @Before
    fun setUp() {
        viewModel = TopImagesViewModel(databaseRepository)
    }

    @Test
    fun sendLoadTopImagesRequestTest() = testCoroutineRule.runBlockingTest {
        `when`(databaseRepository.loadTopImages(any())).thenAnswer {
            val list = listOf(Image(rating = 3), Image(rating = 5), Image(rating = 4), Image(rating = 2))
            (it.arguments[0] as ((AppResult<List<Image>>) -> Unit)).invoke(AppResult.Success(list))
        }

        viewModel.sendLoadTopImagesRequest()

        viewModel.images.observeForTesting {
            val list = viewModel.images.getOrAwaitValue()
            Assert.assertEquals(list[0].rating, 5)
            Assert.assertEquals(list[1].rating, 4)
            Assert.assertEquals(list[2].rating, 3)
            Assert.assertEquals(list[3].rating, 2)
        }
    }

    @Test
    fun sendLoadTopImagesRequestTestError() = testCoroutineRule.runBlockingTest {
        `when`(databaseRepository.loadTopImages(any())).thenAnswer {
            (it.arguments[0] as ((AppResult<List<Image>>) -> Unit)).invoke(AppResult.Error("error"))
        }

        viewModel.sendLoadTopImagesRequest()

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "error")
        }
    }
}