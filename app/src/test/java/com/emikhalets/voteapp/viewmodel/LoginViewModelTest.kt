package com.emikhalets.voteapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emikhalets.voteapp.TestCoroutineRule
import com.emikhalets.voteapp.any
import com.emikhalets.voteapp.getOrAwaitValue
import com.emikhalets.voteapp.model.firebase.FirebaseAuthRepository
import com.emikhalets.voteapp.observeForTesting
import com.emikhalets.voteapp.utils.AppResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel
    private val authRepository = Mockito.mock(FirebaseAuthRepository::class.java)

    @Before
    fun setUp() {
        viewModel = LoginViewModel(authRepository)
    }

    @Test
    fun sendLoginRequestTest() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.login(anyString(), anyString(), any())).thenAnswer {
            (it.arguments[2] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }

        viewModel.sendLoginRequest("123", "123")

        viewModel.loginState.observeForTesting {
            assertEquals(viewModel.loginState.getOrAwaitValue(), true)
        }
    }

    @Test
    fun sendLoginRequestTestError() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.login(anyString(), anyString(), any())).thenAnswer {
            (it.arguments[2] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Error("login error"))
        }

        viewModel.sendLoginRequest("123", "123")

        viewModel.error.observeForTesting {
            assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "login error")
        }
    }

    @Test
    fun sendResetPassRequestTest() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.resetPassword(anyString(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }

        viewModel.sendResetPassRequest("login@mail.com")

        viewModel.passResetState.observeForTesting {
            assertEquals(viewModel.passResetState.getOrAwaitValue(), true)
        }
    }

    @Test
    fun sendResetPassRequestTestError() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.resetPassword(anyString(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Error("reset pass error"))
        }

        viewModel.sendResetPassRequest("login@mail.com")

        viewModel.error.observeForTesting {
            assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "reset pass error")
        }
    }
}