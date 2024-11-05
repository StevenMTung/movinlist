package br.com.steventung.movinlist.ui.appstate

import br.com.steventung.movinlist.domain.model.User

data class AppStateUiState(
    val user: User? = null,
    val isInitLoading: Boolean = true
)

