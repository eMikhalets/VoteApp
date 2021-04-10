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
class HomeViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    private val databaseRepository = Mockito.mock(FirebaseDatabaseRepository::class.java)

    @Before
    fun setUp() {
        viewModel = HomeViewModel(databaseRepository)
    }

    @Test
    fun sendLatestImagesRequestTest() = testCoroutineRule.runBlockingTest {
        `when`(databaseRepository.loadLatestImages(any())).thenAnswer {
            val images = listOf(Image(), Image(), Image(), Image())
            (it.arguments[0] as ((AppResult<List<Image>>) -> Unit)).invoke(AppResult.Success(images))
        }

        viewModel.sendLatestImagesRequest()

        viewModel.images.observeForTesting {
            Assert.assertEquals(viewModel.images.getOrAwaitValue().size, 4)
        }
    }

    @Test
    fun sendLatestImagesRequestTestError() = testCoroutineRule.runBlockingTest {
        `when`(databaseRepository.loadLatestImages(any())).thenAnswer {
            val images = listOf(Image(), Image(), Image(), Image())
            (it.arguments[0] as ((AppResult<List<Image>>) -> Unit)).invoke(AppResult.Error(""))
        }

        viewModel.sendLatestImagesRequest()

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "")
        }
    }
}