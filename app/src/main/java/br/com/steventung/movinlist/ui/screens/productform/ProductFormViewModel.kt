package br.com.steventung.movinlist.ui.screens.productform

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.steventung.movinlist.domain.model.Product
import br.com.steventung.movinlist.extensions.isNetworkAvailable
import br.com.steventung.movinlist.navigation.houseAreaIdArgument
import br.com.steventung.movinlist.navigation.houseAreaNameArgument
import br.com.steventung.movinlist.navigation.productIdArgument
import br.com.steventung.movinlist.repository.FirebaseAuthRepository
import br.com.steventung.movinlist.repository.HouseAreaRepository
import br.com.steventung.movinlist.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ProductFormViewModel @Inject constructor(
    firebaseAuthRepository: FirebaseAuthRepository,
    private val houseAreaRepository: HouseAreaRepository,
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductFormUiState())
    val uiState = _uiState.asStateFlow()
    private val _isSaveProductSucess = MutableSharedFlow<Boolean>()
    val isSaveProductSuccess = _isSaveProductSucess.asSharedFlow()
    private val _isFailSendImage = MutableSharedFlow<Boolean>()
    val isFailSendImage = _isFailSendImage.asSharedFlow()

    private val houseAreaId = savedStateHandle.get<String?>(houseAreaIdArgument)
    private val houseAreaName = savedStateHandle.get<String?>(houseAreaNameArgument)
    private val productId = savedStateHandle.get<String?>(productIdArgument)

    private val userId = firebaseAuthRepository.currentUserEmail

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onNameChanged = { name ->
                    _uiState.update {
                        it.copy(name = name)
                    }
                },
                onBrandChanged = { brand ->
                    _uiState.update {
                        it.copy(brand = brand)
                    }
                },
                onDescriptionChanged = { description ->
                    _uiState.update {
                        it.copy(description = description)
                    }
                },
                onExpandedDropDownMenu = { expandedState ->
                    _uiState.update {
                        it.copy(isDropDownExpanded = expandedState)
                    }
                },
                onDropDownMenuValueChanged = { dropDownValue ->
                    _uiState.update {
                        it.copy(dropDownValue = dropDownValue)
                    }
                },
                onPriceChanged = { price ->
                    val decimalRegex = """^\d+(\.\d{0,2})?$""".toRegex()
                    if (price.matches(decimalRegex) || price.isBlank()) {
                        _uiState.update {
                            it.copy(price = price)
                        }
                    }
                },
                onLoadImage = { uri ->
                    _uiState.update {
                        it.copy(image = uri)
                    }
                }
            )
        }
        loadProductInfo()
        setDropDownMenuVisibility()
    }

    private fun loadProductInfo() {
        productId?.let { productId ->
            viewModelScope.launch {
                productRepository.getProductById(productId).collect { product ->
                    _uiState.update {
                        it.copy(
                            image = product.image,
                            name = product.name,
                            brand = product.brand,
                            description = product.description,
                            quantity = product.quantity,
                            price = product.price.toString(),
                            dropDownValue = product.houseAreaName,
                            isShowDropDownMenu = true,
                            isPurchased = product.purchased
                        )
                    }
                    loadHouseAreaList()
                }
            }
        }
    }

    private fun setDropDownMenuVisibility() {
        if (houseAreaId != null) {
            houseAreaName?.let { houseAreaName ->
                _uiState.update {
                    it.copy(
                        isShowDropDownMenu = false,
                        dropDownValue = houseAreaName
                    )
                }
            }
        } else {
            _uiState.update { it.copy(isShowDropDownMenu = true) }
            loadHouseAreaList()
        }
    }

    private fun loadHouseAreaList() {
        viewModelScope.launch {
            userId?.let { it ->
                houseAreaRepository.getHouseAreasByUserId(it).collect { houseAreaList ->
                    val houseAreaNameList = houseAreaList.map { it.houseAreaName }
                    _uiState.update {
                        it.copy(houseAreaList = houseAreaNameList)
                    }
                }
            }
        }
    }

    fun increaseQuantity() {
        _uiState.update {
            it.copy(quantity = it.quantity + 1)
        }
    }

    fun decreaseQuantity() {
        if (_uiState.value.quantity > 1) {
            _uiState.update {
                it.copy(quantity = it.quantity - 1)
            }
        }
    }

    fun setDropDownMenuExpandedState() {
        _uiState.value.onExpandedDropDownMenu(!_uiState.value.isDropDownExpanded)
    }

    fun selectedDropDownMenuItem(index: Int) {
        _uiState.update {
            it.copy(
                dropDownValue = _uiState.value.houseAreaList[index],
                isDropDownExpanded = false
            )
        }
    }

    fun setModalBottomSheet(isShow: Boolean) {
        _uiState.update {
            it.copy(isShowModalBottomSheet = isShow)
        }
    }

    fun saveProduct(context: Context) {
        viewModelScope.launch {
            with(_uiState.value) {
                userId?.let { userId ->
                    val convertedPrice = convertPriceToBigDecimal()
                    val product = productId?.let {
                        Product(
                            productId = it,
                            name = name,
                            description = description,
                            image = image,
                            brand = brand,
                            quantity = quantity,
                            price = convertedPrice,
                            userId = userId,
                            houseAreaId = "$userId-$dropDownValue",
                            houseAreaName = dropDownValue,
                            purchased = isPurchased
                        )
                    } ?: Product(
                        name = name,
                        description = description,
                        image = image,
                        brand = brand,
                        quantity = quantity,
                        price = convertedPrice,
                        userId = userId,
                        houseAreaId = "$userId-$dropDownValue",
                        houseAreaName = dropDownValue,
                        purchased = isPurchased
                    )
                    try {
                        avoidCreatingProductWithoutHouseArea()
                        showLoadingScreen()
                        withTimeout(4000) {
                            val productId = if (context.isNetworkAvailable()) {
                                productRepository.saveProduct(product = product)
                            } else {
                                if (!product.image.startsWith("http")) {
                                    productRepository.saveProduct(product = product.copy(image = ""))
                                } else {
                                    productRepository.saveProduct(product = product)
                                }
                            }

                            if (!product.image.startsWith("http")) {
                                val imageData = setImageDataToByteArray(product, context)
                                _isSaveProductSucess.emit(true)
                                productRepository.sendProductImage(
                                    imageData = imageData,
                                    productId = productId
                                )
                            } else {
                                _isSaveProductSucess.emit(true)
                            }
                        }
                    } catch (e: Exception) {
                        dismissLoadingScreen()
                        when (e) {
                            is TimeoutCancellationException -> {
                                _isFailSendImage.emit(true)
                            }

                            else -> Log.e("ProductFormViewModel", "saveProduct: Exception", e)
                        }

                    }
                }
            }
        }
    }

    private fun ProductFormUiState.convertPriceToBigDecimal(): BigDecimal =
        try {
            BigDecimal(price)
        } catch (e: NumberFormatException) {
            BigDecimal.ZERO
        }

    private fun setImageDataToByteArray(
        product: Product,
        context: Context
    ): ByteArray = if (product.image.isNotEmpty()) {
        val imageUri = Uri.parse(product.image)
        val inputStream =
            context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val baos = ByteArrayOutputStream()
        bitmap?.let {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        }
        baos.toByteArray()
    } else {
        ByteArrayOutputStream().toByteArray()
    }

    private fun ProductFormUiState.avoidCreatingProductWithoutHouseArea() {
        if (houseAreaId.isNullOrBlank()) {
            if (dropDownValue.isEmpty()) {
                _uiState.update {
                    it.copy(emptyHouseAreaNameError = true)
                }
                throw Exception()
            }
        }
    }

    private fun dismissLoadingScreen() {
        _uiState.update {
            it.copy(isLoading = false)
        }
    }

    private fun showLoadingScreen() {
        _uiState.update {
            it.copy(isLoading = true)
        }
    }

    fun loadImage(uri: String) {
        _uiState.value.onLoadImage(uri)
    }
}
