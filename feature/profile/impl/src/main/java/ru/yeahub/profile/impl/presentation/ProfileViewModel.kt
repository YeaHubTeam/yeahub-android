package ru.yeahub.profile.impl.presentation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.profile.impl.domain.GetProfileUseCase
import java.io.IOException

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val screenMapper: ProfileScreenMapper
) : BaseViewModel() {

    companion object {
        private const val TIME_TO_CLEAN_UP_RESOURCES = 5000L
    }

    val screenState = flow {
        emit(screenMapper.mapToLoading())

        try {
            val profile = getProfileUseCase()
            emit(screenMapper.mapToSuccess(profile))
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: IOException) {
            emit(screenMapper.mapThrowableToState(e))
        } catch (e: HttpException) {
            emit(screenMapper.mapThrowableToState(e))
        }
    }.stateIn(
        scope = viewModelScopeSafe,
        started = SharingStarted.WhileSubscribed(TIME_TO_CLEAN_UP_RESOURCES),
        initialValue = ProfileScreenState.Loading
    )

    private val _commands = MutableSharedFlow<ProfileCommand>()
    val commands: SharedFlow<ProfileCommand> = _commands

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnBackClick -> onBackClick()

            is ProfileEvent.OnSocialNetworkClicked -> onSocialNetworkOpened(
                code = event.code,
                url = event.url
            )
        }
    }

    private fun onBackClick() {
        viewModelScopeSafe.launch(Dispatchers.IO) {
            _commands.emit(ProfileCommand.NavigateBack)
        }
    }

    private fun onSocialNetworkOpened(code: String, url: String) {
        viewModelScopeSafe.launch(Dispatchers.IO) {
            _commands.emit(
                ProfileCommand.OpenSocialNetwork(
                    code = code,
                    url = url
                )
            )
        }
    }
}