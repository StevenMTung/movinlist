package br.com.steventung.movinlist.repository

import android.util.Log
import br.com.steventung.movinlist.domain.model.User
import br.com.steventung.movinlist.domain.modelDocument.UserDocument
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal const val USER_COLLECTION = "users"
internal const val USER_REPOSITORY_TAG = "UserRepository"

class UserRepository @Inject constructor(
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
) {

    private val userRef = firestore.collection(USER_COLLECTION)
    private val houseAreaRef = firestore.collection(HOUSE_AREAS_COLLECTION)
    private val productRef = firestore.collection(PRODUCT_COLLECTION)

    suspend fun saveUser(user: User) {
        try {
            val userDocument = UserDocument(
                name = user.name,
                picture = user.picture
            )

            val document = userRef.document(user.userId)
            document.set(userDocument).await()
        } catch (e: Exception) {
            Log.e(USER_REPOSITORY_TAG, "saveUser: ", e)
        }
    }

    suspend fun updateUser(user: User) {
        try {
            val userDocument = UserDocument(
                name = user.name,
                picture = user.picture
            )
            val document = userRef.document(user.userId)
            document.update(
                mapOf(
                    "name" to userDocument.name,
                    "picture" to userDocument.picture
                )
            ).await()
        } catch (e: Exception) {
            Log.e(USER_REPOSITORY_TAG, "upDateUser: ", e)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun sendUserImage(imageData: ByteArray, userId: String) {
        GlobalScope.launch {
            try {
                val reference = storage.reference.child("users/$userId.jpeg")
                reference.putBytes(imageData).await()

                val url = reference.downloadUrl.await()
                userRef
                    .document(userId)
                    .update("picture", url.toString()).await()
            } catch (e: Exception) {
                Log.e(USER_REPOSITORY_TAG, "sendUserImage: ", e)
            }
        }
    }

    fun getUserImage(userId: String) = callbackFlow {
        val listenerRegistration = userRef.document(userId)
            .addSnapshotListener { query, error ->
                error?.let {
                    close(it)
                    return@addSnapshotListener
                }
                query?.let { documentSnapshot ->
                    val userImage =
                        documentSnapshot.toObject<UserDocument>()?.toUser(userId)?.picture
                    userImage?.let { trySend(it) }
                }
            }
        awaitClose {
            listenerRegistration.remove()
        }
    }

    fun getUser(userId: String) = callbackFlow {
        val listenerRegistration = userRef.document(userId)
            .addSnapshotListener { query, error ->
                error?.let {
                    close(it)
                    return@addSnapshotListener
                }
                query?.let { documentSnapshot ->
                    val userImage = documentSnapshot.toObject<UserDocument>()?.toUser(userId)
                    userImage?.let { trySend(it) }
                }
            }
        awaitClose {
            listenerRegistration.remove()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun deleteUser(userId: String) {
        GlobalScope.launch {
            try {
                val document = userRef.document(userId)
                document.delete().await()
            } catch (e: Exception) {
                Log.e(USER_REPOSITORY_TAG, "deleteUser: ", e)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun removeImageWhenUserIsDeleted(userId: String) {
        GlobalScope.launch {
            try {
                val reference =
                    storage.reference.child("users/$userId.jpeg")
                reference.delete().await()
            } catch (e: Exception) {
                Log.e(USER_REPOSITORY_TAG, "removeImageWhenUserIsDeleted: $userId", e)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun deleteUserProducts(userId: String) {
        GlobalScope.launch {
            try {
                val query = productRef
                    .whereEqualTo("userId", userId)
                    .get().await()

                query?.let { snapshot ->
                    snapshot.documents.mapNotNull { productDocument ->
                        productRef.document(productDocument.id).delete().await()
                        removeProductImageWhenUserIsDeleted(productDocument.id)
                    }
                }
            } catch (e: Exception) {
                Log.e(USER_REPOSITORY_TAG, "deleteUserProducts: ", e)
            }
        }
    }

    private suspend fun removeProductImageWhenUserIsDeleted(productId: String) {
        try {
            val reference =
                storage.reference.child("products/$productId.jpeg")
            reference.delete().await()
        } catch (e: Exception) {
            Log.e(USER_REPOSITORY_TAG, "removeProductImageWhenUserIsDeleted: ", e)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun deleteUserHouseAreas(userId: String) {
        GlobalScope.launch {
            try {
                val query = houseAreaRef
                    .whereEqualTo("userId", userId)
                    .get().await()

                query?.let { snapshot ->
                    snapshot.documents.mapNotNull { houseAreaDocument ->
                        houseAreaRef.document(houseAreaDocument.id).delete().await()
                    }
                }
            } catch (e: Exception) {
                Log.e(USER_REPOSITORY_TAG, "deleteUserHouseAreas: ", e)
            }
        }
    }
}

