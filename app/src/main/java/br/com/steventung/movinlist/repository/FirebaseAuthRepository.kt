package br.com.steventung.movinlist.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class AuthResult(
    val currentUser: FirebaseUser? = null,
    val isInitLoading: Boolean = true
)

class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    private val _currentUser = MutableStateFlow(AuthResult())
    val currentUser = _currentUser.asStateFlow()

    init {
            firebaseAuth.addAuthStateListener { firebaseAuth ->
                _currentUser.update {
                    it.copy(
                        currentUser = firebaseAuth.currentUser,
                        isInitLoading = false
                    )
                }
            }
    }

    val currentUserEmail = firebaseAuth.currentUser?.email

    suspend fun signUpUser(email: String, senha: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, senha).await()
    }

    suspend fun signIn(email: String, senha: String) {
        firebaseAuth.signInWithEmailAndPassword(email, senha).await()
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

//    fun isUserLogged(): Boolean {
//        return firebaseAuth.currentUser != null
//    }

    suspend fun changePassword(currentPassword: String, newPassword: String) {
        val user = firebaseAuth.currentUser
        user?.let { currentUser ->
            currentUser.email?.let { email ->
                val credential = EmailAuthProvider.getCredential(email, currentPassword)
                currentUser.reauthenticate(credential).await()
                currentUser.updatePassword(newPassword).await()
            }
        }
    }

    suspend fun deleteUserAccount(currentPassword: String) {
        val user = firebaseAuth.currentUser
        user?.let {
            val email = it.email
            email?.let { userEmail ->
                val credential = EmailAuthProvider.getCredential(userEmail, currentPassword)
                it.reauthenticate(credential).await()
                it.delete().await()
            }
        }
    }
}