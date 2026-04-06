package ru.yeahub.profile_edit.impl.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import ru.yeahub.core_ui.component.DropDownMenu
import ru.yeahub.core_ui.component.SkillButton
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

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.colors.white900),
        contentPadding = PaddingValues(top = TAB_CONTENT_TOP_PADDING),
    ) {
        item {
            SectionHeader(
                title = stringResource(R.string.skills_tab),
                description = stringResource(R.string.skills_tab_label),
            )
            Spacer(modifier = Modifier.height(SECTION_DESCRIPTION_ADDITIONAL_BOTTOM_SPACING))
        }
        item {
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
        }
        item {
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
                    SkillButton(
                        enabled = true,
                        activeButton = true,
                        onClick = { },
                        imageLeft = skill.imageRes,
                        imageRight = R.drawable.icon_button_close,
                        text = skill.name,
                        onRightIconClick = { onRemoveSkill(skill) },
                        imageSizeLeftWith = 20.dp,
                        imageSizeLeftHigh = 20.dp,
                        modifier = Modifier.height(42.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileEditSkillsPreview() {
    val skillsState = ProfileEditState.SkillsTabState(
        listOfSkills = persistentListOf(
            DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Kotlin1"),
            DomainProfileEditSkill(
                imageRes = R.drawable.icon_true_button,
                name = "Jetpack Compose",
            ),
            DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Coroutines"),
        ),
        listOfChosenSkills = persistentListOf(
            DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Kotlin2"),
            DomainProfileEditSkill(
                imageRes = R.drawable.icon_true_button,
                name = "Jetpack Compose",
            ),
            DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Coroutines"),
            DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Git"),
            DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Java"),
            DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Gradle"),
            DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Kotlin3"),
            DomainProfileEditSkill(imageRes = R.drawable.icon_true_button, name = "Coroutines"),
        ),
    )

    SkillsContent(
        state = skillsState,
        onEvent = {},
    )
}
