package com.emikhalets.voteapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = HomeViewModel(FirebaseDatabaseRepository(FirebaseDatabase.getInstance().reference))
    }

    @After
    fun tearDown() {
    }

    @Test
    fun sendLatestImagesRequest() = runBlocking {
        viewModel.sendLatestImagesRequest()
    }
}