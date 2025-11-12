package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.PalavraRepository
import com.example.myapplication.data.repository.RankingRepository
import com.example.myapplication.ui.screens.TelaAdminDashboard
import com.example.myapplication.ui.screens.TelaGerenciarPalavras
import com.example.myapplication.ui.screens.TelaInicial
import com.example.myapplication.ui.screens.TelaJogo
import com.example.myapplication.ui.screens.TelaLoginAdmin
import com.example.myapplication.ui.screens.TelaRanking
import com.example.myapplication.ui.theme.MestreDasPalavrasTheme
import com.example.myapplication.ui.viewmodel.AdminViewModel
import com.example.myapplication.ui.viewmodel.JogoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MestreDasPalavrasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val palavraRepository = PalavraRepository(db.palavraDao())
    val rankingRepository = RankingRepository(db.rankingDao())

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicial"
    ) {
        composable("inicial") {
            TelaInicial(navController = navController)
        }

        composable("jogo") {
            val viewModel: JogoViewModel = viewModel(
                factory = JogoViewModel.Factory(palavraRepository, rankingRepository)
            )
            TelaJogo(navController = navController, viewModel = viewModel)
        }

        composable("ranking") {
            TelaRanking(
                navController = navController,
                repository = rankingRepository
            )
        }

        composable("login_admin") {
            TelaLoginAdmin(navController = navController)
        }

        composable("admin_dashboard") {
            val viewModel: AdminViewModel = viewModel(
                factory = AdminViewModel.Factory(palavraRepository, rankingRepository)
            )
            TelaAdminDashboard(navController = navController, viewModel = viewModel)
        }

        composable("gerenciar_palavras") {
            val viewModel: AdminViewModel = viewModel(
                factory = AdminViewModel.Factory(palavraRepository, rankingRepository)
            )
            TelaGerenciarPalavras(navController = navController, viewModel = viewModel)
        }
    }
}