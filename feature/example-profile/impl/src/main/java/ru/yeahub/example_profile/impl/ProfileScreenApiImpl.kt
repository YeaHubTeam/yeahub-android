package ru.yeahub.example_profile.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.yeahub.example_profile.api.ProfileScreenApi

class ProfileScreenApiImpl : ProfileScreenApi {
    @Composable
    override fun ProfileScreen(
        userId: String,
        userName: String,
        onBackClick: () -> Unit
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "ID пользователя: $userId")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Имя пользователя: $userName")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBackClick) {
                    Text(text = "Назад на Home")
                }
            }
        }
    }
} 