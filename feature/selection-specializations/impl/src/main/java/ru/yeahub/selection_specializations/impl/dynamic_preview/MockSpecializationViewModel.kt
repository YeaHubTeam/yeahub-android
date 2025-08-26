package ru.yeahub.selection_specializations.impl.dynamic_preview

import ru.yeahub.selection_specializations.impl.model.DomainSpecilialization
import ru.yeahub.selection_specializations.impl.model.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.model.SpecializationsRequest

//TODO: mockUseCase with return mock data
val mockRequest = SpecializationsRequest(
    page = 1,
    limit = 10
)

@Suppress("MagicNumber")
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