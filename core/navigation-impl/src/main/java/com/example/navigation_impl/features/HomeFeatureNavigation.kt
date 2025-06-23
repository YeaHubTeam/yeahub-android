package com.example.navigation_impl.features

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.navigation_api.FeatureNavigation
import com.example.navigation_api.NavigationRoutes

/**
 * Простая фича Home с двумя экранами
 */
class HomeFeatureNavigation : FeatureNavigation {
    
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        // Регистрируем экраны напрямую в главном графе
        navGraphBuilder.composable(NavigationRoutes.Home.BASE) {
            HomeScreen(
                onNavigateToDetails = {
                    navController.navigate(NavigationRoutes.Home.DETAILS)
                }
            )
        }
        
        navGraphBuilder.composable(NavigationRoutes.Home.DETAILS) {
            HomeDetailsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
    
    override fun getBaseRoute(): String = NavigationRoutes.Home.BASE
}

@Composable
private fun HomeScreen(
    onNavigateToDetails: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Домашний экран",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onNavigateToDetails
        ) {
            Text("Перейти к деталям")
        }
    }
}

@Composable
private fun HomeDetailsScreen(
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Детали Home",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onNavigateBack
        ) {
            Text("Назад")
        }
    }
} 