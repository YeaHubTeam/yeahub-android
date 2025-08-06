package ru.yeahub.selection_specializations.impl.presentation

import ru.yeahub.core_utils.BaseViewModel
import ru.yeahub.core_utils.pager.YeaHubPagerLoader
import ru.yeahub.core_utils.pagerImpl.YeaHubPager
import ru.yeahub.selection_specializations.api.domain.GetOnbordingUseCase
import ru.yeahub.selection_specializations.api.domain.GetSpecializationUseCase
import ru.yeahub.selection_specializations.impl.data.MapperDataToDomain
import ru.yeahub.selection_specializations.impl.data.SpecializationsRepository
import ru.yeahub.selection_specializations.impl.model.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.model.SpecializationsRequest

//need to end (in process)
@Suppress("UnusedPrivateProperty")
class SpecializationViewModel(
    private val voMapper: DomainToVoMapper,
    private val domainMapper: MapperDataToDomain,
    private val onBoardUseCase: GetOnbordingUseCase,
    private val specialUseCase: GetSpecializationUseCase,
    private val repository: SpecializationsRepository
) : BaseViewModel() {
    private val pagerLoader =
        object : YeaHubPagerLoader<DomainSpecilializationListResponse, SpecializationsRequest> {
            override suspend fun loadPage(
                request: SpecializationsRequest
            ): DomainSpecilializationListResponse =
                repository.getSpecilizationsList(request)

            override fun updatePage(
                request: SpecializationsRequest,
                page: Int
            ): SpecializationsRequest =
                request.copy(page = page)
        }

    private val pager = YeaHubPager(
        pagerLoader = pagerLoader,
        requestData = SpecializationsRequest(page = 1, limit = 10),
        data = { domainResponse -> domainResponse.data },
        total = { domainResponse -> domainResponse.total },
        limit = { domainResponse -> domainResponse.limit },
    )
}