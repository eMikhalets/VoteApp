package com.emikhalets.voteapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emikhalets.voteapp.TestCoroutineRule
import com.emikhalets.voteapp.any
import com.emikhalets.voteapp.getOrAwaitValue
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.entities.User
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
class TopUsersViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TopUsersViewModel
    private val databaseRepository = Mockito.mock(FirebaseDatabaseRepository::class.java)

    @Before
    fun setUp() {
        viewModel = TopUsersViewModel(databaseRepository)
    }

    @Test
    fun sendLoadTopUsersRequestTest() = testCoroutineRule.runBlockingTest {
        `when`(databaseRepository.loadTopUsers(any())).thenAnswer {
            val list = listOf(User(rating = 3), User(rating = 5), User(rating = 4), User(rating = 2))
            (it.arguments[0] as ((AppResult<List<User>>) -> Unit)).invoke(AppResult.Success(list))
        }

        viewModel.sendLoadTopUsersRequest()

        viewModel.users.observeForTesting {
            val list = viewModel.users.getOrAwaitValue()
            Assert.assertEquals(list[0].rating, 5)
            Assert.assertEquals(list[1].rating, 4)
            Assert.assertEquals(list[2].rating, 3)
            Assert.assertEquals(list[3].rating, 2)
        }
    }

    @Test
    fun sendLoadTopUsersRequestTestDatabaseError() = testCoroutineRule.runBlockingTest {
        `when`(databaseRepository.loadTopUsers(any())).thenAnswer {
            (it.arguments[0] as ((AppResult<List<User>>) -> Unit)).invoke(AppResult.Error("error"))
        }

        viewModel.sendLoadTopUsersRequest()

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "error")
        }
    }
}