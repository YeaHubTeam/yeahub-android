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
import timber.log.Timber
import java.io.IOException

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val screenMapper: ProfileScreenMapper
) : BaseViewModel() {

    companion object {
        private const val TIME_TO_CLEAN_UP_RESOURCES = 5000L
        private const val HTTP_UNAUTHORIZED = 401
        private const val HTTP_FORBIDDEN = 403
        private const val HTTP_NOT_FOUND = 404
    }

    val screenState = flow {
        emit(screenMapper.mapToLoading())

        try {
            val profile = getProfileUseCase()
            emit(screenMapper.mapToSuccess(profile))
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: IOException) {
            emit(screenMapper.mapToError("network_error"))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> emit(screenMapper.mapToUnauthorized())
                403 -> emit(screenMapper.mapToError("access_denied"))
                404 -> emit(screenMapper.mapToUserDeleted())
                else -> emit(screenMapper.mapToError("server_error:${e.code()}"))
            }
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