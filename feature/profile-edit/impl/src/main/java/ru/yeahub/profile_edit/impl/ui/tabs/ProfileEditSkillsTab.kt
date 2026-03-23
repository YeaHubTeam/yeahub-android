package ru.yeahub.profile_edit.impl.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.intents.ProfileEditScreenEvent
import ru.yeahub.profile_edit.impl.ui.FieldLabel
import ru.yeahub.profile_edit.impl.ui.SectionTitle
import ru.yeahub.ui.R

private val SKILLS_TOP_PADDING = 2.dp
private val SKILLS_TOP_SPACER = 10.dp
private val SKILLS_SPACER_MIDDLE = 5.dp
private val SKILLS_SPACER_SMALL = 3.dp

@Composable
fun SkillsContent(
    state: ProfileEditState.SkillsTabState,
    onEvent: (ProfileEditScreenEvent) -> Unit
) {
    val skillNames: List<String> = remember {
        state.listOfSkills.map { it.name }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.colors.white900)
            .padding(top = SKILLS_TOP_PADDING),
    ) {
        item {
            Spacer(Modifier.padding(SKILLS_TOP_SPACER))
            SectionTitle(title = stringResource(R.string.skills_tab))

            Spacer(Modifier.padding(SKILLS_SPACER_MIDDLE))
            Text(
                text = stringResource(R.string.skills_tab_label),
                style = Theme.typography.body7Alt,
                color = Theme.colors.black900,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            Spacer(Modifier.padding(SKILLS_SPACER_MIDDLE))
            FieldLabel(text = stringResource(R.string.skills_dropdown_menu_label))

            Spacer(Modifier.padding(SKILLS_SPACER_SMALL))
            DropDownMenu(
                placeholder = stringResource(R.string.skills_dropdown_menu_placeholder),
                items = skillNames,
                selected = "",
                onSelected = {
                    onEvent(ProfileEditScreenEvent.ToDo)
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            Spacer(Modifier.padding(SKILLS_SPACER_MIDDLE))
            FieldLabel(text = stringResource(R.string.skills_selected_label))

            Spacer(Modifier.padding(SKILLS_SPACER_SMALL))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.listOfChosenSkills.forEach { skill ->
                    SkillButton(
                        enabled = true,
                        activeButton = true,
                        onClick = { },
                        imageLeft = skill.image,
                        imageRight = R.drawable.icon_button_close,
                        text = skill.name,
                        onRightIconClick = {
                            onEvent(ProfileEditScreenEvent.ToDo)
                        },
                        imageSizeLeftWith = 20.dp,
                        imageSizeLeftHigh = 20.dp,
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
            ProfileEditState.Skill(
                image = R.drawable.icon_true_button,
                name = "Kotlin1",
            ),
            ProfileEditState.Skill(
                image = R.drawable.icon_true_button,
                name = "Jetpack Compose",
            ),
            ProfileEditState.Skill(
                image = R.drawable.icon_true_button,
                name = "Coroutines",
            ),
        ),
        listOfChosenSkills = persistentListOf(
            ProfileEditState.Skill(
                image = R.drawable.icon_true_button,
                name = "Kotlin2",
            ),
            ProfileEditState.Skill(
                image = R.drawable.icon_true_button,
                name = "Jetpack Compose",
            ),
            ProfileEditState.Skill(
                image = R.drawable.icon_true_button,
                name = "Coroutines",
            ),
            ProfileEditState.Skill(
                image = R.drawable.icon_true_button,
                name = "Git",
            ),
            ProfileEditState.Skill(
                image = R.drawable.icon_true_button,
                name = "Java",
            ),
            ProfileEditState.Skill(
                image = R.drawable.icon_true_button,
                name = "Gradle",
            ),
            ProfileEditState.Skill(
                image = R.drawable.icon_true_button,
                name = "Kotlin3",
            ),
            ProfileEditState.Skill(
                image = R.drawable.icon_true_button,
                name = "Coroutines",
            ),
        ),
    )

    SkillsContent(
        state = skillsState,
        onEvent = {},
    )
}
