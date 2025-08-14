package test

import ru.yeahub.network_api.models.GetSpecializationResponse
import ru.yeahub.network_api.models.GetSpecializationsResponse
import ru.yeahub.selection_specializations.impl.model.DomainSpecilialization
import ru.yeahub.selection_specializations.impl.model.DomainSpecilializationListResponse
import ru.yeahub.selection_specializations.impl.model.VoSpecilialization

data class SpecializationSelectionDataToDomainMapperTestCase1(
    val dataToTest: GetSpecializationResponse,
    val expectedResult: DomainSpecilialization
)

data class SpecializationSelectionDataToDomainMapperTestCase2(
    val dataToTest: GetSpecializationsResponse,
    val expectedResult: DomainSpecilializationListResponse
)

data class SpecializationSelectionDomainToVoMapperTestCase(
    val dataToTest: List<DomainSpecilialization>,
    val expectedResult: List<VoSpecilialization>
)
