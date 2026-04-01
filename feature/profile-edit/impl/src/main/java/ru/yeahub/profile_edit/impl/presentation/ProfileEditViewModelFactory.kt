package ru.yeahub.profile_edit.impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

internal typealias ViewModelCreator = () -> ViewModel?

internal class ProfileEditViewModelFactory(
    private val viewModelCreator: ViewModelCreator = { null },
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModelCreator() as T
}

@Composable
internal inline fun <reified VM : ViewModel> profileEditViewModelCreator(noinline creator: ViewModelCreator): VM =
    viewModel(factory = remember { ProfileEditViewModelFactory(creator) })
