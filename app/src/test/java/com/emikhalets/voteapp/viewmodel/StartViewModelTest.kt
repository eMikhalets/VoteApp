package com.emikhalets.voteapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emikhalets.voteapp.TestCoroutineRule
import com.emikhalets.voteapp.any
import com.emikhalets.voteapp.getOrAwaitValue
import com.emikhalets.voteapp.model.firebase.FirebaseAuthRepository
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.observeForTesting
import com.emikhalets.voteapp.utils.AppResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
class StartViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: StartViewModel
    private val authRepository = mock(FirebaseAuthRepository::class.java)
    private val databaseRepository = mock(FirebaseDatabaseRepository::class.java)

    @Before
    fun setUp() {
        viewModel = StartViewModel(authRepository, databaseRepository)
    }

    @Test
    fun sendLoadUserDataRequestTest() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.checkAuth(any())).thenAnswer {
            (it.arguments[0] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }
        `when`(databaseRepository.loadUserData(any())).thenAnswer {
            (it.arguments[0] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }

        viewModel.checkUserExistingRequest()

        viewModel.userExisting.observeForTesting {
            assertEquals(viewModel.userExisting.getOrAwaitValue(), true)
        }
    }

    @Test
    fun sendLoadUserDataRequestTestAuthError() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.checkAuth(any())).thenAnswer {
            (it.arguments[0] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Error("auth error"))
        }

        viewModel.checkUserExistingRequest()

        viewModel.error.observeForTesting {
            assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "auth error")
        }
    }

    @Test
    fun sendLoadUserDataRequestTestDatabaseError() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.checkAuth(any())).thenAnswer {
            (it.arguments[0] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }
        `when`(databaseRepository.loadUserData(any())).thenAnswer {
            (it.arguments[0] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Error("database error"))
        }

        viewModel.checkUserExistingRequest()

        viewModel.error.observeForTesting {
            assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "database error")
        }
    }
}