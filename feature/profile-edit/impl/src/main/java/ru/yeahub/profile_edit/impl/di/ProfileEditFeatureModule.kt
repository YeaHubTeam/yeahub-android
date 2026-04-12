package ru.yeahub.profile_edit.impl.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.yeahub.navigation_api.FeatureApi
import ru.yeahub.profile_edit.impl.ProfileEditFeatureImpl
import ru.yeahub.profile_edit.impl.data.ProfileEditDataToDomainMapper
import ru.yeahub.profile_edit.impl.data.ProfileEditRepositoryImpl
import ru.yeahub.profile_edit.impl.domain.repository.ProfileEditRepository
import ru.yeahub.profile_edit.impl.domain.usecase.DeleteAvatarUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.DeleteAvatarUseCaseImpl
import ru.yeahub.profile_edit.impl.domain.usecase.GetProfileUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.GetProfileUseCaseImpl
import ru.yeahub.profile_edit.impl.domain.usecase.GetSkillsUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.GetSkillsUseCaseImpl
import ru.yeahub.profile_edit.impl.domain.usecase.GetSpecializationsUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.GetSpecializationsUseCaseImpl
import ru.yeahub.profile_edit.impl.domain.usecase.SaveProfileUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.SaveProfileUseCaseImpl
import ru.yeahub.profile_edit.impl.domain.usecase.UploadAvatarUseCase
import ru.yeahub.profile_edit.impl.domain.usecase.UploadAvatarUseCaseImpl
import ru.yeahub.profile_edit.impl.presentation.ProfileEditScreenMapper
import ru.yeahub.profile_edit.impl.presentation.ProfileEditViewModel

val profileEditFeatureModule = module {

    single { ProfileEditDataToDomainMapper() }

    single<ProfileEditRepository> {
        ProfileEditRepositoryImpl(
            apiService = get(),
            mapper = get(),
            context = get(),
        )
    }

    factory<GetSkillsUseCase> { GetSkillsUseCaseImpl(repository = get()) }

    factory<GetSpecializationsUseCase> { GetSpecializationsUseCaseImpl(repository = get()) }

    factory<GetProfileUseCase> {
        GetProfileUseCaseImpl(
            repository = get(),
            getSkills = get(),
            getSpecializations = get(),
        )
    }

    factory<SaveProfileUseCase> { SaveProfileUseCaseImpl(repository = get()) }

    factory<UploadAvatarUseCase> { UploadAvatarUseCaseImpl(repository = get()) }

    factory<DeleteAvatarUseCase> { DeleteAvatarUseCaseImpl(repository = get()) }

    single { ProfileEditScreenMapper() }

    viewModel {
        ProfileEditViewModel(
            getProfile = get(),
            saveProfile = get(),
            uploadAvatar = get(),
            deleteAvatar = get(),
            mapper = get(),
        )
    }

    single { ProfileEditFeatureImpl() } bind FeatureApi::class
}
