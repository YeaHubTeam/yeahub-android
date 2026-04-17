package ru.yeahub.profile_edit.impl.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import ru.yeahub.core_ui.component.DropDownMenu
import ru.yeahub.core_ui.component.SkillButtonWithDeleteButton
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.profile_edit.impl.domain.models.DomainProfileEditSkill
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenEvent
import ru.yeahub.profile_edit.impl.ui.SECTION_DESCRIPTION_ADDITIONAL_BOTTOM_SPACING
import ru.yeahub.profile_edit.impl.ui.SectionHeader
import ru.yeahub.profile_edit.impl.ui.TAB_CONTENT_TOP_PADDING
import ru.yeahub.ui.R

@Composable
internal fun SkillsContent(
    state: ProfileEditState.SkillsTabState,
    onEvent: (ProfileEditScreenEvent) -> Unit,
) {
    val skillNames: List<String> = remember(state.listOfSkills) {
        state.listOfSkills.map { it.name }
    }

    val onRemoveSkill: (DomainProfileEditSkill) -> Unit = remember(onEvent) {
        { skill: DomainProfileEditSkill ->
            onEvent(ProfileEditScreenEvent.RemoveSkill(skill))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.colors.white900)
            .padding(top = TAB_CONTENT_TOP_PADDING)
            .verticalScroll(rememberScrollState()),
    ) {
        SectionHeader(
            title = stringResource(R.string.skills_tab),
            description = stringResource(R.string.skills_tab_label),
        )
        Spacer(modifier = Modifier.height(SECTION_DESCRIPTION_ADDITIONAL_BOTTOM_SPACING))
        DropDownMenu(
            title = stringResource(R.string.skills_dropdown_menu_label),
            placeholder = stringResource(R.string.skills_dropdown_menu_placeholder),
            items = skillNames,
            selected = "",
            onSelected = {
                onEvent(ProfileEditScreenEvent.AddSkill(skillName = it))
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.skills_selected_label),
            style = Theme.typography.body7,
            color = Theme.colors.black900,
        )
        Spacer(Modifier.height(16.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            state.listOfChosenSkills.forEach { skill ->
                SkillButtonWithDeleteButton(
                    leadingIconUrl = skill.imageUrl,
                    text = skill.name,
                    onDeleteClick = { onRemoveSkill(skill) },
                    modifier = Modifier.height(42.dp),
                )
            }
        }
    }
}

private class SkillsTabPreviewProvider : PreviewParameterProvider<ProfileEditState.SkillsTabState> {
    override val values = sequenceOf(
        ProfileEditState.SkillsTabState(
            listOfSkills = persistentListOf(
                DomainProfileEditSkill(imageUrl = "", name = "Kotlin"),
                DomainProfileEditSkill(imageUrl = "", name = "Jetpack Compose"),
                DomainProfileEditSkill(imageUrl = "", name = "Coroutines"),
            ),
            listOfChosenSkills = persistentListOf(
                DomainProfileEditSkill(imageUrl = "", name = "Kotlin"),
                DomainProfileEditSkill(imageUrl = "", name = "Jetpack Compose"),
                DomainProfileEditSkill(imageUrl = "", name = "Coroutines"),
                DomainProfileEditSkill(imageUrl = "", name = "Git"),
                DomainProfileEditSkill(imageUrl = "", name = "Java"),
                DomainProfileEditSkill(imageUrl = "", name = "Gradle"),
            ),
        ),
        ProfileEditState.SkillsTabState(
            listOfSkills = persistentListOf(),
            listOfChosenSkills = persistentListOf(),
        ),
    )
}

@StaticPreview
@Composable
internal fun ProfileEditSkillsPreview(
    @PreviewParameter(SkillsTabPreviewProvider::class) state: ProfileEditState.SkillsTabState,
) {
    SkillsContent(
        state = state,
        onEvent = {},
    )
}
