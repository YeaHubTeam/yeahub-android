package ru.yeahub.profile_edit.impl.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import ru.yeahub.core_ui.component.CoreTopTabs
import ru.yeahub.core_ui.component.PrimaryButton
import ru.yeahub.core_ui.component.TopAppBarWithBottomBorder
import ru.yeahub.core_ui.component.UnsavedChangesDialog
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState.ProfileEditTabs
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState.ProfileEditTabs.AboutMe
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState.ProfileEditTabs.PersonalInfo
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState.ProfileEditTabs.Skills
import ru.yeahub.profile_edit.impl.ui.tabs.PersonalInfoContent
import ru.yeahub.ui.R

@Composable
fun ProfileEditScreen(
    tabs: List<ProfileEditTabs>,
    state: ProfileEditState.Loaded,
    showUnsavedChangesDialog: Boolean,
    onTabSelected: (ProfileEditTabs) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onLeaveConfirmed: () -> Unit,
    onLeaveDismissed: () -> Unit,
    headerText: TextOrResource,
    personalInfoContent: @Composable (() -> Unit),
    aboutMeContent: @Composable (() -> Unit),
    skillsContent: @Composable (() -> Unit),
) {
    BackHandler {
        onBackClick()
    }
    val tabTitles = remember(tabs) {
        tabs.map { tab ->
            when (tab) {
                PersonalInfo -> R.string.profile_personal_information
                AboutMe -> R.string.profile_about_me
                Skills -> R.string.profile_skills
            }
        }
    }

    Scaffold(
        containerColor = Theme.colors.black10,
        topBar = {
            TopAppBarWithBottomBorder(
                title = headerText,
                onBackClick = onBackClick,
            )
        },
        bottomBar = {
            PrimaryButton(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(R.string.save))
            }
        },
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .padding(bottom = 0.dp),
            color = Theme.colors.white900,
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 16.dp,
                    ),
            ) {
                CoreTopTabs(
                    selectedIndex = state.selectedTab.ordinal,
                    onSelected = { index ->
                        onTabSelected(ProfileEditTabs.entries[index])
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    tabs = tabTitles.map { stringResource(it) },
                    edgePadding = (-16).dp,
                    indicatorHeight = 10.dp,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Theme.colors.white900),
                ) {
                    when (state.selectedTab) {
                        PersonalInfo -> personalInfoContent()
                        AboutMe -> aboutMeContent()
                        Skills -> skillsContent()
                    }
                }
            }
        }
    }

    if (showUnsavedChangesDialog) {
        UnsavedChangesDialog(
            onLeave = onLeaveConfirmed,
            onStay = onLeaveDismissed,
        )
    }
}

@Preview
@Composable
fun ProfileEditPreview() {
    val screenState = ProfileEditState.Loaded(
        selectedTab = PersonalInfo,
        personalInfoState = ProfileEditState.PersonalInfoTabState(
            avatarUrl = null,
            nickname = "John Doe",
            specializationList = emptyList(),
            specialization = "",
            email = "johndoe@gmail.com",
            location = "Санкт-Петербург",
            socialLinksUrlMap = persistentMapOf(),
            isSpecializationEditable = false,
        ),
        aboutMeTabState = ProfileEditState.AboutMeTabState(
            aboutMeField = "",
        ),
        skillsTabState = ProfileEditState.SkillsTabState(
            listOfSkills = persistentListOf(),
            listOfChosenSkills = persistentListOf(),
        ),
        hasUnsavedChanges = true,
    )

    var state by remember { mutableStateOf(screenState) }

    ProfileEditScreen(
        state = state,
        showUnsavedChangesDialog = false,
        onTabSelected = { state = state.copy(selectedTab = it) },
        onBackClick = {},
        onLeaveConfirmed = {},
        onLeaveDismissed = {},
        personalInfoContent = {
            PersonalInfoContent(
                state = state.personalInfoState,
                onEvent = { },
            )
        },
        aboutMeContent = { },
        skillsContent = { },
        tabs = ProfileEditTabs.entries,
        headerText = TextOrResource.Text("Редактирование профиля"),
        onSaveClick = { },
    )
}

@Preview
@Composable
fun ProfileEditWithDialogPreview() {
    val screenState = ProfileEditState.Loaded(
        selectedTab = PersonalInfo,
        personalInfoState = ProfileEditState.PersonalInfoTabState(
            avatarUrl = null,
            nickname = "John Doe",
            specializationList = emptyList(),
            specialization = "Android Разработчик",
            email = "johndoe@gmail.com",
            location = "Санкт-Петербург",
            socialLinksUrlMap = persistentMapOf(),
            isSpecializationEditable = true,
        ),
        aboutMeTabState = ProfileEditState.AboutMeTabState(
            aboutMeField = "",
        ),
        skillsTabState = ProfileEditState.SkillsTabState(
            listOfSkills = persistentListOf(),
            listOfChosenSkills = persistentListOf(),
        ),
        hasUnsavedChanges = true,
    )

    var state by remember { mutableStateOf(screenState) }

    ProfileEditScreen(
        state = state,
        showUnsavedChangesDialog = true,
        onTabSelected = {},
        onBackClick = {},
        onLeaveConfirmed = {},
        onLeaveDismissed = {},
        tabs = ProfileEditTabs.entries,
        headerText = TextOrResource.Text("Редактирование профиля"),
        personalInfoContent = {
            PersonalInfoContent(
                state = state.personalInfoState,
                onEvent = { },
            )
        },
        aboutMeContent = {},
        skillsContent = {},
        onSaveClick = { },
    )
}