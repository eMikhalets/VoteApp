package com.emikhalets.voteapp.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emikhalets.voteapp.TestCoroutineRule
import com.emikhalets.voteapp.any
import com.emikhalets.voteapp.getOrAwaitValue
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
import org.mockito.Mockito.anyString

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
class UserImagesViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: UserImagesViewModel
    private val databaseRepository = Mockito.mock(FirebaseDatabaseRepository::class.java)
    private val storageRepository = Mockito.mock(FirebaseStorageRepository::class.java)

    @Before
    fun setUp() {
        viewModel = UserImagesViewModel(databaseRepository, storageRepository)
    }

    @Test
    fun sendSaveImageRequestTest() = testCoroutineRule.runBlockingTest {
        `when`(storageRepository.saveImage(anyString(), anyString(), any())).thenAnswer {
            (it.arguments[2] as ((AppResult<String>) -> Unit)).invoke(AppResult.Success("http://example.com"))
        }
        `when`(databaseRepository.saveUserImage(anyString(), anyString(), any())).thenAnswer {
            (it.arguments[2] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Success(true))
        }

        val uri = Uri.parse("http://example.com") ?: "http://example.com"
        viewModel.sendSaveImageRequest(uri.toString())

        viewModel.saveImageState.observeForTesting {
            Assert.assertEquals(viewModel.saveImageState.getOrAwaitValue(), true)
        }
    }

    @Test
    fun sendSaveImageRequestTestErrorUri() = testCoroutineRule.runBlockingTest {
        val uri = Uri.parse("") ?: ""
        viewModel.sendSaveImageRequest(uri.toString())

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "Image Uri is empty")
        }
    }

    @Test
    fun sendSaveImageRequestTestDatabaseError() = testCoroutineRule.runBlockingTest {
        `when`(storageRepository.saveImage(anyString(), anyString(), any())).thenAnswer {
            (it.arguments[2] as ((AppResult<String>) -> Unit)).invoke(AppResult.Success("http://example.com"))
        }
        `when`(databaseRepository.saveUserImage(anyString(), anyString(), any())).thenAnswer {
            (it.arguments[2] as ((AppResult<Boolean>) -> Unit)).invoke(AppResult.Error("database error"))
        }

        val uri = Uri.parse("http://example.com") ?: "http://example.com"
        viewModel.sendSaveImageRequest(uri.toString())

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "database error")
        }
    }

    @Test
    fun sendSaveImageRequestTestStorageError() = testCoroutineRule.runBlockingTest {
        `when`(storageRepository.saveImage(anyString(), anyString(), any())).thenAnswer {
            (it.arguments[2] as ((AppResult<String>) -> Unit)).invoke(AppResult.Error("storage error"))
        }

        val uri = Uri.parse("http://example.com") ?: "http://example.com"
        viewModel.sendSaveImageRequest(uri.toString())

        viewModel.error.observeForTesting {
            Assert.assertEquals(viewModel.error.getOrAwaitValue().getContentIfNotHandled(), "storage error")
        }
    }
}