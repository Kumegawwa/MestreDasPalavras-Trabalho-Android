package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.Palavra
import com.example.myapplication.data.repository.PalavraRepository
import com.example.myapplication.data.repository.RankingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminUiState(
    val palavras: List<Palavra> = emptyList(),
    val mensagem: String? = null
)

class AdminViewModel(
    private val palavraRepository: PalavraRepository,
    private val rankingRepository: RankingRepository
) : ViewModel() {

    private val _mensagemFlow = MutableStateFlow<String?>(null)

    val uiState: StateFlow<AdminUiState> = palavraRepository.todasPalavrasFlow
        .combine(_mensagemFlow) { palavras, mensagem ->
            AdminUiState(palavras, mensagem)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AdminUiState()
        )

    private fun setMensagem(msg: String?) {
        _mensagemFlow.update { msg }
    }

    fun addPalavra(palavra: String) {
        if (palavra.isBlank()) {
            setMensagem("Palavra não pode estar em branco")
            return
        }
        if (palavra.length != 5) {
            setMensagem("Palavra deve ter 5 letras")
            return
        }

        viewModelScope.launch {
            try {
                palavraRepository.addPalavra(palavra)
                setMensagem("Palavra '$palavra' adicionada e sincronizada.")
            } catch (e: Exception) {
                setMensagem("Erro: ${e.message}")
            }
        }
    }

    fun updatePalavra(palavraAntiga: Palavra, palavraNova: String) {
        if (palavraNova.isBlank()) {
            setMensagem("Palavra não pode estar em branco")
            return
        }
        if (palavraNova.length != 5) {
            setMensagem("Palavra deve ter 5 letras")
            return
        }
        if (palavraAntiga.palavra == palavraNova.uppercase()) {
            setMensagem("A nova palavra é igual à antiga.")
            return
        }

        viewModelScope.launch {
            try {
                palavraRepository.updatePalavra(palavraAntiga, palavraNova)
                setMensagem("Palavra atualizada e sincronizada.")
            } catch (e: Exception) {
                setMensagem("Erro: ${e.message}")
            }
        }
    }

    fun deletePalavra(palavra: Palavra) {
        viewModelScope.launch {
            try {
                palavraRepository.deletePalavra(palavra)
                setMensagem("Palavra removida")
            } catch (e: Exception) {
                setMensagem("Erro: ${e.message}")
            }
        }
    }

    fun clearRanking() {
        viewModelScope.launch {
            try {
                rankingRepository.clearAll()
                setMensagem("Ranking limpo com sucesso.")
            } catch (e: Exception) {
                setMensagem("Erro ao limpar ranking: ${e.message}")
            }
        }
    }

    fun clearMensagem() {
        setMensagem(null)
    }

    companion object {
        fun Factory(
            palavraRepo: PalavraRepository,
            rankingRepo: RankingRepository
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
                        return AdminViewModel(palavraRepo, rankingRepo) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}