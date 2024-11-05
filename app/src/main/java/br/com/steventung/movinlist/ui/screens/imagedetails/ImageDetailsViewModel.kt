package br.com.steventung.movinlist.ui.screens.imagedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.steventung.movinlist.navigation.imageUriArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ImageDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(ImageDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val imageUrl = savedStateHandle.get<String>(imageUriArgument)

    init {
        loadImage()
    }

    private fun loadImage() {
        imageUrl?.let { image ->
            _uiState.update {
                it.copy(image = image)
            }
        }
    }
}