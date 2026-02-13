package ru.yeahub.profile_edit.impl.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.yeahub.core_ui.component.CoreTopTabs
import ru.yeahub.core_ui.component.TopAppBarWithBottomBorder
import ru.yeahub.core_ui.theme.Theme
import ru.yeahub.core_utils.common.TextOrResource
import ru.yeahub.profile_edit.impl.presentation.ProfileEditState
import ru.yeahub.profile_edit.impl.ui.tabs.PersonalInfoContent

@Composable
fun ProfileEditScreen(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
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
        Box(
            modifier = Modifier.padding(paddingValues),
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Theme.colors.white900,
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 16.dp),
                ) {
                    CoreTopTabs(
                        selectedIndex = selectedTabIndex,
                        onSelected = onTabSelected,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        tabs = tabs,
                    )

                    Spacer(Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                    ) {
                        when (selectedTabIndex) {
                            0 -> personalInfoContent()
                            1 -> aboutMeContent()
                            2 -> skillsContent()
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun ProfileEditPreview() {
    val personalInfoState = ProfileEditState.Loaded(
        avatarUrl = null,
        nickname = "John Doe",
        specializationList = emptyList(),
        specialization = "Android Разработчик",
        email = "johndoe@gmail.com",
        location = "Санкт-Петербург",
        selectedTab = ProfileEditState.ProfileEditTabs.PersonalInfo,
        socialLinksUrlMap = emptyMap(),
    )

    var selectedTab by remember { mutableIntStateOf(personalInfoState.selectedTab.ordinal) }

    ProfileEditScreen(
        onTabSelected = { selectedTab = it },
        onBackClick = {},
        personalInfoContent = {
            PersonalInfoContent(
                state = personalInfoState,
                onAction = { },
            )
        },
        aboutMeContent = { },
        skillsContent = { },
        tabs = ProfileEditState.ProfileEditTabs.entries.map { it.title },
        selectedTabIndex = selectedTab,
        headerText = TextOrResource.Text("Редактирование профиля"),
    )
}
