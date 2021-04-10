package com.emikhalets.voteapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emikhalets.voteapp.TestCoroutineRule
import com.emikhalets.voteapp.any
import com.emikhalets.voteapp.getOrAwaitValue
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.observeForTesting
import com.emikhalets.voteapp.utils.AppResult
import com.emikhalets.voteapp.utils.ImageNumber
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.core.AnyOf.anyOf
import org.hamcrest.core.Is.`is`
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
class VotingViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: VotingViewModel
    private val databaseRepository = Mockito.mock(FirebaseDatabaseRepository::class.java)

    @Before
    fun setUp() {
        viewModel = VotingViewModel(databaseRepository)
    }

    @Test
    fun sendPrepareVotingRequestTest() = testCoroutineRule.runBlockingTest {
        `when`(databaseRepository.loadAllImages(any())).thenAnswer {
            val list = listOf(Image(name = "one"), Image(name = "two"), Image(name = "three"), Image(name = "four"), Image(name = "five"))
            (it.arguments[0] as ((AppResult<List<Image>>) -> Unit)).invoke(AppResult.Success(list))
        }

        viewModel.sendPrepareVotingRequest()

        viewModel.image1.observeForTesting {
            assertThat(viewModel.image1.getOrAwaitValue().name,
                    anyOf(`is`("one"), `is`("two"), `is`("three"), `is`("four"), `is`("five")))
        }
        viewModel.image2.observeForTesting {
            assertThat(viewModel.image2.getOrAwaitValue().name,
                    anyOf(`is`("one"), `is`("two"), `is`("three"), `is`("four"), `is`("five")))
        }
    }

    @Test
    fun sendPrepareVotingRequestTestError() = testCoroutineRule.runBlockingTest {
        `when`(databaseRepository.loadAllImages(any())).thenAnswer {
            (it.arguments[0] as ((AppResult<List<Image>>) -> Unit)).invoke(AppResult.Error("error"))
        }

        viewModel.sendPrepareVotingRequest()

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "error")
        }
    }

    @Test
    fun sendVoteRequestTest() = testCoroutineRule.runBlockingTest {
        `when`(databaseRepository.loadAllImages(any())).thenAnswer {
            val list = listOf(Image(name = "one"), Image(name = "two"), Image(name = "three"), Image(name = "four"), Image(name = "five"))
            (it.arguments[0] as ((AppResult<List<Image>>) -> Unit)).invoke(AppResult.Success(list))
        }
        `when`(databaseRepository.updateImageRating(anyString(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }

        viewModel.sendPrepareVotingRequest()
        viewModel.setSelectedImage(ImageNumber.FIRST)
        viewModel.sendVoteRequest()

        viewModel.voteState.observeForTesting {
            Assert.assertEquals(viewModel.voteState.getOrAwaitValue(), true)
        }
    }

    @Test
    fun sendVoteRequestTestDatabaseError() = testCoroutineRule.runBlockingTest {
        `when`(databaseRepository.loadAllImages(any())).thenAnswer {
            val list = listOf(Image(name = "one"), Image(name = "two"), Image(name = "three"), Image(name = "four"), Image(name = "five"))
            (it.arguments[0] as ((AppResult<List<Image>>) -> Unit)).invoke(AppResult.Success(list))
        }
        `when`(databaseRepository.updateImageRating(anyString(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Error("database error"))
        }

        viewModel.sendPrepareVotingRequest()
        viewModel.setSelectedImage(ImageNumber.FIRST)
        viewModel.sendVoteRequest()

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "database error")
        }
    }

    @Test
    fun sendVoteRequestTestSelectError() = testCoroutineRule.runBlockingTest {
        viewModel.sendVoteRequest()

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "Need to choose a image")
        }
    }
}