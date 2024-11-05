package br.com.steventung.movinlist.repository

import android.util.Log
import br.com.steventung.movinlist.domain.model.Product
import br.com.steventung.movinlist.domain.modelDocument.ProductDocument
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal const val PRODUCT_COLLECTION = "products"

class ProductRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    private val productRef = firestore.collection(PRODUCT_COLLECTION)

    suspend fun saveProduct(product: Product): String {
        val productDocument = ProductDocument(
            name = product.name,
            nameLowerCase = product.name.lowercase(),
            description = product.description,
            brand = product.brand,
            image = product.image,
            quantity = product.quantity,
            price = product.price.toDouble(),
            purchased = product.purchased,
            userId = product.userId,
            houseAreaId = product.houseAreaId,
            houseAreaName = product.houseAreaName
        )
        val document = product.productId?.let { id ->
            productRef.document(id)
        } ?: productRef.document()

        document.set(productDocument).await()
        return document.id
    }

    suspend fun upDateProductPurchaseState(purchasedState: Boolean, productId: String) {
        productRef
            .document(productId)
            .update(mapOf("purchased" to purchasedState)).await()
    }

    fun getNonPurchasedProductByHouseAreaId(houseAreaId: String): Flow<List<Product>> =
        callbackFlow {
            val listenerRegistration = productRef
                .whereEqualTo("houseAreaId", houseAreaId)
                .whereEqualTo("purchased", false)
                .addSnapshotListener { query, error ->
                    error?.let {
                        close(it)
                        return@addSnapshotListener
                    }
                    query?.let { snapshot ->
                        val productList = snapshot.documents.mapNotNull {
                            it.toObject<ProductDocument>()?.toProduct(productId = it.id)
                        }
                        trySend(productList)
                    }
                }
            awaitClose {
                listenerRegistration.remove()
            }
        }

    fun getNonPurchasedProductByUserId(userId: String) = callbackFlow {
        val listenerRegistration = productRef
            .whereEqualTo("userId", userId)
            .whereEqualTo("purchased", false)
            .addSnapshotListener { query, error ->
                error?.let {
                    close(it)
                    return@addSnapshotListener
                }

                query?.let { snapShot ->
                    val productList = snapShot.documents.mapNotNull {
                        it.toObject<ProductDocument>()?.toProduct(productId = it.id)
                    }
                    trySend(productList)
                }
            }
        awaitClose {
            listenerRegistration.remove()
        }
    }

    fun getNonPurchasedProductByUserIdAndProductName(userId: String, searchedText: String) =
        callbackFlow {
            val searchedTextLowerCase = searchedText.lowercase()
            val endTextLowerCase = searchedTextLowerCase + "\uf8ff"
            val listenerRegistration = productRef
                .whereEqualTo("userId", userId)
                .whereEqualTo("purchased", false)
                .whereGreaterThanOrEqualTo("nameLowerCase", searchedTextLowerCase)
                .whereLessThanOrEqualTo("nameLowerCase", endTextLowerCase)
                .addSnapshotListener { query, error ->
                    error?.let {
                        close(it)
                        return@addSnapshotListener
                    }

                    query?.let { snapShot ->
                        val productList = snapShot.documents.mapNotNull {
                            it.toObject<ProductDocument>()?.toProduct(productId = it.id)
                        }
                        trySend(productList)
                    }
                }
            awaitClose {
                listenerRegistration.remove()
            }
        }

    fun getPurchasedProductByUserIdAndProductName(userId: String, searchedText: String) =
        callbackFlow {
            val searchedTextLowerCase = searchedText.lowercase()
            val endTextLowerCase = searchedTextLowerCase + "\uf8ff"
            val listenerRegistration = productRef
                .whereEqualTo("userId", userId)
                .whereEqualTo("purchased", true)
                .whereGreaterThanOrEqualTo("nameLowerCase", searchedTextLowerCase)
                .whereLessThanOrEqualTo("nameLowerCase", endTextLowerCase)
                .addSnapshotListener { query, error ->
                    error?.let {
                        close(it)
                        return@addSnapshotListener
                    }

                    query?.let { snapShot ->
                        val productList = snapShot.documents.mapNotNull {
                            it.toObject<ProductDocument>()?.toProduct(productId = it.id)
                        }
                        trySend(productList)
                    }
                }
            awaitClose {
                listenerRegistration.remove()
            }
        }

    fun getPurchasedProductByUserId(userId: String) = callbackFlow {
        val listenerRegistration = productRef
            .whereEqualTo("userId", userId)
            .whereEqualTo("purchased", true)
            .addSnapshotListener { query, error ->
                error?.let {
                    close(it)
                    return@addSnapshotListener
                }

                query?.let { snapShot ->
                    val productList = snapShot.documents.mapNotNull {
                        it.toObject<ProductDocument>()?.toProduct(productId = it.id)
                    }
                    trySend(productList)
                }
            }
        awaitClose {
            listenerRegistration.remove()
        }
    }

    fun getProductById(productId: String) = callbackFlow {
        val documentReference = productRef.document(productId)
        val listenerRegistration = documentReference
            .addSnapshotListener { query, error ->
                error?.let {
                    close(it)
                    return@addSnapshotListener
                }

                query?.let { snapShot ->
                    val product = snapShot.toObject<ProductDocument>()?.toProduct(snapShot.id)
                    product?.let { trySend(it) }
                }
            }
        awaitClose {
            listenerRegistration.remove()
        }
    }

    suspend fun removeProductById(productId: String) {
        val document = productRef.document(productId)
        document.delete().await()
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun sendProductImage(imageData: ByteArray, productId: String) {
        GlobalScope.launch {
            try {
                val reference =
                    storage.reference.child("products/$productId.jpeg")
                reference.putBytes(imageData).await()

                val url = reference.downloadUrl.await()
                productRef
                    .document(productId)
                    .update(mapOf("image" to url.toString())).await()
            } catch (e: Exception) {
                Log.e("FirebaseStorage", "sendImage: Exception", e)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun removeProductImage(productId: String) {
        GlobalScope.launch {
            try {
                val reference =
                    storage.reference.child("products/$productId.jpeg")
                reference.delete().await()
            } catch (e: Exception) {
                Log.e("FirebaseStorage", "removeImage: Exception", e)
            }
        }
    }

}