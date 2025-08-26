package ru.yeahub.selection_specializations.impl.dynamic_preview

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.core_utils.pager.YeaHubPagerLoader
import ru.yeahub.core_utils.pagerImpl.YeaHubPager
import ru.yeahub.core_utils.pagerImpl.YeaHubPagerState
import ru.yeahub.selection_specializations.impl.domain.GetSpecializationListUseCase
import ru.yeahub.selection_specializations.impl.model.DomainSpecilialization
import ru.yeahub.selection_specializations.impl.model.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.model.SpecializationsRequest
import ru.yeahub.selection_specializations.impl.presentation.SpecializationScreenEvent
import ru.yeahub.selection_specializations.impl.presentation.SpecializationScreenState
import ru.yeahub.selection_specializations.impl.presentation.SpecializationSelectionDomainToVoMapper.toVoList
import ru.yeahub.selection_specializations.impl.presentation.SpecializationSelectionScreenCommand
import ru.yeahub.selection_specializations.impl.presentation.SpecializationSelectionScreenCommand.OnBackClick
import ru.yeahub.selection_specializations.impl.presentation.SpecializationSelectionScreenCommand.SpecializationSelectionClick
import timber.log.Timber

private const val TIME_TO_CLEAN_UP_RESOURCES = 500L

//TODO: mockUseCase with return mock data
val mockRequest = SpecializationsRequest(
    page = 1,
    limit = 10
)

val mockDomainList = listOf<DomainSpecilialization>(
    DomainSpecilialization(1L, "Artificial Intelligence"),
    DomainSpecilialization(2L, "Machine Learning"),
    DomainSpecilialization(3L, "Data Science"),
    DomainSpecilialization(4L, "Cybersecurity"),
    DomainSpecilialization(5L, "Cloud Computing"),
    DomainSpecilialization(6L, "Web Development"),
    DomainSpecilialization(7L, "Mobile Development"),
    DomainSpecilialization(8L, "DevOps"),
    DomainSpecilialization(9L, "Blockchain"),
    DomainSpecilialization(10L, "Internet of Things")
)
val mockResponse = DomainSpecilializationListResponse(
    page = mockRequest.page.toLong(),
    limit = mockRequest.page.toLong(),
    data = mockDomainList,
    total = mockRequest.limit.toLong()
)