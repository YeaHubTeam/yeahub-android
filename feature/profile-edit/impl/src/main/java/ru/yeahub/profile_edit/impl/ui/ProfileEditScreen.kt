package ru.yeahub.profile_edit.impl.ui

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.CoreTopTabs
import ru.yeahub.core_ui.component.TopAppBarWithBottomBorder
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.ui.tabs.AboutMeContent
import ru.yeahub.profile_edit.impl.ui.tabs.PersonalInfoContent

@Composable
fun ProfileEditScreen(
    tabs: List<ProfileEditState.ProfileEditTabs>,
    state: ProfileEditState.Loaded,
    onTabSelected: (ProfileEditState.ProfileEditTabs) -> Unit,
    onBackClick: () -> Unit,
    headerText: TextOrResource,
    personalInfoContent: @Composable (() -> Unit),
    aboutMeContent: @Composable (() -> Unit),
    skillsContent: @Composable (() -> Unit),
) {
    Scaffold(
        containerColor = Theme.colors.black10,
        topBar = {
            TopAppBarWithBottomBorder(
                title = headerText,
                onBackClick = onBackClick,
            )
        },
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .padding(bottom = 16.dp),
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
                    selectedIndex = state.selectedTab.index,
                    onSelected = { index ->
                        onTabSelected(ProfileEditState.ProfileEditTabs.entries[index])
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    tabs = tabs.map { stringResource(it.title.resource) },
                    edgePadding = (-16).dp,
                    indicatorHeight = 10.dp,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Theme.colors.white900),
                ) {
                    when (state.selectedTab.index) {
                        0 -> personalInfoContent()
                        1 -> aboutMeContent()
                        2 -> skillsContent()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ProfileEditPreview() {
    val screenState = ProfileEditState.Loaded(
        selectedTab = ProfileEditState.ProfileEditTabs.PersonalInfo,
        personalInfoState = ProfileEditState.PersonalInfoTabState(
            avatarUrl = null,
            nickname = "John Doe",
            specializationList = emptyList(),
            specialization = "Android Разработчик",
            email = "johndoe@gmail.com",
            location = "Санкт-Петербург",
            socialLinksUrlMap = emptyMap(),
        ),
        aboutMeTabState = ProfileEditState.AboutMeTabState(
            aboutMeField = "",
        ),
        skillsTabState = ProfileEditState.SkillsTabState(
            listOfSkills = emptyList(),
            listOfChosenSkills = emptyList(),
        ),
    )

    var state by remember { mutableStateOf(screenState) }

    ProfileEditScreen(
        state = state,
        onTabSelected = { state = state.copy(selectedTab = it) },
        onBackClick = {},
        personalInfoContent = {
            PersonalInfoContent(
                state = state.personalInfoState,
                onAction = { },
            )
        },
        aboutMeContent = {
            AboutMeContent(
                state = state.aboutMeTabState
            )
        },
        skillsContent = {
        },
        tabs = ProfileEditState.ProfileEditTabs.entries,
        headerText = TextOrResource.Text("Редактирование профиля"),
    )
}