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
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
class RegisterViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RegisterViewModel
    private val authRepository = Mockito.mock(FirebaseAuthRepository::class.java)
    private val databaseRepository = Mockito.mock(FirebaseDatabaseRepository::class.java)

    @Before
    fun setUp() {
        viewModel = RegisterViewModel(authRepository, databaseRepository)
    }

    @Test
    fun sendRegisterRequestTest() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.register(anyString(), anyString(), any())).thenAnswer {
            (it.arguments[2] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }
        `when`(databaseRepository.saveUser(anyString(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }

        viewModel.sendRegisterRequest("login", "pass")

        viewModel.registerState.observeForTesting {
            Assert.assertEquals(viewModel.registerState.getOrAwaitValue(), true)
        }
    }

    @Test
    fun sendRegisterRequestTestDatabaseError() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.register(anyString(), anyString(), any())).thenAnswer {
            (it.arguments[2] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }
        `when`(databaseRepository.saveUser(anyString(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Error("database error"))
        }

        viewModel.sendRegisterRequest("login", "pass")

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "database error")
        }
    }

    @Test
    fun sendRegisterRequestTestAuthError() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.register(anyString(), anyString(), any())).thenAnswer {
            (it.arguments[2] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Error("auth error"))
        }

        viewModel.sendRegisterRequest("login", "pass")

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "auth error")
        }
    }
}