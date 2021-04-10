package com.emikhalets.voteapp.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emikhalets.voteapp.TestCoroutineRule
import com.emikhalets.voteapp.any
import com.emikhalets.voteapp.getOrAwaitValue
import com.emikhalets.voteapp.model.firebase.FirebaseAuthRepository
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.model.firebase.FirebaseStorageRepository
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
class ProfileViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ProfileViewModel
    private val authRepository = Mockito.mock(FirebaseAuthRepository::class.java)
    private val databaseRepository = Mockito.mock(FirebaseDatabaseRepository::class.java)
    private val storageRepository = Mockito.mock(FirebaseStorageRepository::class.java)

    @Before
    fun setUp() {
        viewModel = ProfileViewModel(authRepository, databaseRepository, storageRepository)
    }

    @Test
    fun sendLogOutRequestTest() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.logOut(any())).thenAnswer {
            (it.arguments[0] as (() -> Unit)).invoke()
        }

        viewModel.sendLogOutRequest()

        viewModel.logoutState.observeForTesting {
            Assert.assertEquals(viewModel.logoutState.getOrAwaitValue(), true)
        }
    }

    @Test
    fun sendUpdateUserPhotoRequestTest() = testCoroutineRule.runBlockingTest {
        `when`(storageRepository.saveUserPhoto(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<String>) -> Unit)).invoke(AppResult.Success("http://example.com"))
        }
        `when`(authRepository.updateUserPhoto(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }
        `when`(databaseRepository.updateUserPhoto(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }

        val uri = Uri.parse("http://example.com") ?: "http://example.com"
        val uriStr = uri.toString()
        viewModel.sendUpdateUserPhotoRequest(uriStr)

        viewModel.saveImageState.observeForTesting {
            Assert.assertEquals(viewModel.saveImageState.getOrAwaitValue(), "http://example.com")
        }
    }

    @Test
    fun sendUpdateUserPhotoRequestTestDatabaseError() = testCoroutineRule.runBlockingTest {
        `when`(storageRepository.saveUserPhoto(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<String>) -> Unit)).invoke(AppResult.Success("http://example.com"))
        }
        `when`(authRepository.updateUserPhoto(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }
        `when`(databaseRepository.updateUserPhoto(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Error("database error"))
        }

        val uri = Uri.parse("http://example.com") ?: "http://example.com"
        val uriStr = uri.toString()
        viewModel.sendUpdateUserPhotoRequest(uriStr)

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "database error")
        }
    }

    @Test
    fun sendUpdateUserPhotoRequestTestAuthError() = testCoroutineRule.runBlockingTest {
        `when`(storageRepository.saveUserPhoto(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<String>) -> Unit)).invoke(AppResult.Success("http://example.com"))
        }
        `when`(authRepository.updateUserPhoto(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Error("auth error"))
        }

        val uri = Uri.parse("http://example.com") ?: "http://example.com"
        viewModel.sendUpdateUserPhotoRequest(uri.toString())

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "auth error")
        }
    }

    @Test
    fun sendUpdateUserPhotoRequestTestStorageError() = testCoroutineRule.runBlockingTest {
        `when`(storageRepository.saveUserPhoto(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Error("storage error"))
        }

        val uri = Uri.parse("http://example.com") ?: "http://example.com"
        viewModel.sendUpdateUserPhotoRequest(uri.toString())

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "storage error")
        }
    }

    @Test
    fun sendUpdateUserPhotoRequestTestUriError() = testCoroutineRule.runBlockingTest {
        val uri = Uri.parse("") ?: ""
        viewModel.sendUpdateUserPhotoRequest(uri.toString())

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "Image Uri is empty")
        }
    }

    @Test
    fun sendUpdatePassRequestTest() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.updateUserPassword(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }

        viewModel.sendUpdatePassRequest("1234567")

        viewModel.passwordState.observeForTesting {
            Assert.assertEquals(viewModel.passwordState.getOrAwaitValue(), true)
        }
    }

    @Test
    fun sendUpdatePassRequestTestAuthError() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.updateUserPassword(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Error("auth error"))
        }

        viewModel.sendUpdatePassRequest("1234567")

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "auth error")
        }
    }

    @Test
    fun sendUpdateUsernameRequestTest() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.updateUsername(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }
        `when`(databaseRepository.updateUsername(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }

        viewModel.sendUpdateUsernameRequest("newName")

        viewModel.usernameState.observeForTesting {
            Assert.assertEquals(viewModel.usernameState.getOrAwaitValue(), true)
        }
    }

    @Test
    fun sendUpdateUsernameRequestTestDatabaseError() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.updateUsername(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }
        `when`(databaseRepository.updateUsername(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Error("database error"))
        }

        viewModel.sendUpdateUsernameRequest("newName")

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "database error")
        }
    }

    @Test
    fun sendUpdateUsernameRequestTestAuthError() = testCoroutineRule.runBlockingTest {
        `when`(authRepository.updateUsername(any(), any())).thenAnswer {
            (it.arguments[1] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Error("auth error"))
        }

        viewModel.sendUpdateUsernameRequest("newName")

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "auth error")
        }
    }
}