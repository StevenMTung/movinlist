package br.com.steventung.movinlist.repository

import android.util.Log
import br.com.steventung.movinlist.domain.model.HouseArea
import br.com.steventung.movinlist.domain.modelDocument.HouseAreaDocument
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

internal const val HOUSE_AREA_REPOSITORY_TAG = "HouseAreaRepository"
internal const val HOUSE_AREAS_COLLECTION = "houseAreas"

class HouseAreaRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    private val houseAreaRef = firestore.collection(HOUSE_AREAS_COLLECTION)
    private val productRef = firestore.collection(PRODUCT_COLLECTION)

    suspend fun saveHouseArea(houseArea: HouseArea) {
        val houseAreaDocument = HouseAreaDocument(
            houseAreaName = houseArea.houseAreaName,
            userId = houseArea.userId
        )

        if (verifyHouseAreaAlreadyExist(houseArea)) {
            throw IllegalArgumentException()
        } else {
            val document = if (!houseArea.houseAreaId.isNullOrBlank()) {
                upDateProductsWhenHouseAreaIsEdited(houseArea)
                Log.i("SaveHouseAreaTeste", "Rodou 3")
                houseAreaRef.document(houseArea.houseAreaId).delete().await()
                houseAreaRef.document("${houseArea.userId}-${houseArea.houseAreaName}")
            } else {
                houseAreaRef.document("${houseArea.userId}-${houseArea.houseAreaName}")
            }
            document.set(houseAreaDocument).await()
        }
    }

    private suspend fun verifyHouseAreaAlreadyExist(houseArea: HouseArea): Boolean {
        val query = houseAreaRef
            .whereEqualTo("userId", houseArea.userId)
            .whereEqualTo("houseAreaName", houseArea.houseAreaName)
            .get().await()
        return query.documents.isNotEmpty()
    }

    private suspend fun upDateProductsWhenHouseAreaIsEdited(houseArea: HouseArea) {
        val query = productRef
            .whereEqualTo("houseAreaId", houseArea.houseAreaId)
            .get().await()

        query?.let { snapshot ->
            snapshot.documents.mapNotNull { productDocument ->
                val product =
                    productDocument.toObject<ProductDocument>()?.toProduct(productDocument.id)
                        ?.copy(
                            houseAreaName = houseArea.houseAreaName,
                            houseAreaId = "${houseArea.userId}-${houseArea.houseAreaName}"
                        )
                product?.let { productNotNull ->
                    Log.i("SaveHouseAreaTeste", "Rodou 1")
                    productRef.document(productDocument.id).update(
                        mapOf(
                            "houseAreaName" to productNotNull.houseAreaName,
                            "houseAreaId" to productNotNull.houseAreaId
                        )
                    ).await()
                    Log.i("SaveHouseAreaTeste", "Rodou 2")
                }
            }
        }
    }

    fun getHouseAreaById(houseAreaId: String): Flow<HouseArea> = callbackFlow {
        val document = houseAreaRef.document(houseAreaId)
        val listenerRegistration = document
            .addSnapshotListener { query, error ->
                error?.let {
                    close(it)
                    return@addSnapshotListener
                }
                query?.let { documentSnapshot ->
                    val houseArea =
                        documentSnapshot.toObject<HouseAreaDocument>()
                            ?.toHouseArea(documentSnapshot.id)
                    houseArea?.let { trySend(it) }
                }
            }
        awaitClose {
            listenerRegistration.remove()
        }
    }

    fun getHouseAreasByUserId(userId: String): Flow<List<HouseArea>> = callbackFlow {
        // Adiciona o SnapshotListener para ouvir mudanças no Firestore
        val listenerRegistration =
            houseAreaRef
                .whereEqualTo("userId", userId)
                .addSnapshotListener { query, error ->
                    error?.let {
                        close(it)  // Fecha o flow em caso de erro
                        return@addSnapshotListener
                    }
                    query?.let { snapShot ->
                        val updatedList = snapShot.documents.mapNotNull {
                            it.toObject<HouseAreaDocument>()?.toHouseArea(houseAreaId = it.id)
                        }
                        trySend(updatedList)  // Emite a nova lista de HouseAreas
                    }
                }

        // Garante que o listener será removido quando o flow for cancelado
        awaitClose {
            listenerRegistration.remove()
        }
    }

    suspend fun removeHouseArea(houseAreaId: String) {
        try {
            val document = houseAreaRef.document(houseAreaId)
            document.delete().await()
        } catch (e: Exception) {
            Log.e(HOUSE_AREA_REPOSITORY_TAG, "removeHouseArea: ", e)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun removeProductsWhenHouseAreaIsDeleted(houseAreaId: String) {
        GlobalScope.launch {
            try {
                val query = productRef
                    .whereEqualTo("houseAreaId", houseAreaId)
                    .get().await()

                query?.let { querySnapshot ->
                    querySnapshot.documents.mapNotNull { documentSnapshot ->
                        productRef.document(documentSnapshot.id).delete().await()
                        removeProductImageWhenHouseAreaIsDeleted(productId = documentSnapshot.id)
                    }
                }
            } catch (e: Exception) {
                Log.e(HOUSE_AREA_REPOSITORY_TAG, "removeProductsWhenHouseAreaIsDeleted: ", e)
            }
        }
    }

    private suspend fun removeProductImageWhenHouseAreaIsDeleted(productId: String) {
        try {
            val reference =
                storage.reference.child("products/$productId.jpeg")
            reference.delete().await()
        } catch (e: Exception) {
            Log.e(HOUSE_AREA_REPOSITORY_TAG, "removeProductImageWhenHouseAreaIsDeleted: ", e)
        }
    }
}


