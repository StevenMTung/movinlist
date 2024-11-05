package br.com.steventung.movinlist.navigation

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.steventung.movinlist.media.persistUriPermission
import br.com.steventung.movinlist.media.verifyPermission
import br.com.steventung.movinlist.ui.components.ModalBottomSheetImageFile
import br.com.steventung.movinlist.ui.screens.userform.UserFormScreen
import br.com.steventung.movinlist.ui.screens.userform.UserFormViewModel

internal const val userFormGraphRoute = "userFormGraphRoute"
internal const val imageUriArgument = "imageUriArgument"

fun NavGraphBuilder.userFormGraph(
    onNavigateBack: () -> Unit,
    onNavigateToPasswordFormDialog: () -> Unit,
    onNavigateToDeletePasswordDialog: () -> Unit,
    onNavigateToCameraDisplay: () -> Unit,
    onNavigateBackWithSendImageError: () -> Unit
) {
    composable(
        route = userFormGraphRoute,
        arguments = listOf(navArgument(imageUriArgument) { nullable = true })
    ) { backStackEntry ->
        val imageUrl = backStackEntry.savedStateHandle.get<String>(imageUriArgument)
        Log.i("imageVerif", "imageUrl: $imageUrl")
        val viewModel = hiltViewModel<UserFormViewModel>()
        val state by viewModel.uiState.collectAsState()
        val isUpdateSuccess by viewModel.isUpdateSuccess.collectAsState(false)
        val isSendUserImageFailed by viewModel.isSendUserImageFailed.collectAsState(false)
        val context = LocalContext.current
        LaunchedEffect(imageUrl) {
            imageUrl?.let {
                viewModel.loadImage(it)
            }
        }

        LaunchedEffect(isUpdateSuccess) {
            if (isUpdateSuccess) {
                onNavigateBack()
            }
        }

        LaunchedEffect(isSendUserImageFailed) {
            if (isSendUserImageFailed) {
                if (state.userImage.startsWith("http")) {
                    onNavigateBack()
                } else {
                    onNavigateBackWithSendImageError()
                }
            }
        }

        UserFormScreen(
            state = state,
            onChangeUserPhoto = {
                viewModel.setModalBottomSheetState(true)
            },
            onSaveUserUpdates = {
                viewModel.updateUser(context)
            },
            onOpenChangePasswordDialog = {
                onNavigateToPasswordFormDialog()
            },
            onDeleteUser = {
                onNavigateToDeletePasswordDialog()
            }
        )

        val pickMedia = rememberLauncherForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                context.persistUriPermission(uri)
                viewModel.loadImage(uri.toString())
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        val requestCameraPermission = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                if (granted) {
                    viewModel.setModalBottomSheetState(true)
                } else {
                    Log.d("Camera", "Permission to access camera not granted")
                }
            }
        )

        if (state.isShowModalBottomSheet) {
            ModalBottomSheetImageFile(
                onDismiss = {
                    viewModel.setModalBottomSheetState(false)
                },
                onClickPhotoAlbum = {
                    pickMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                    viewModel.setModalBottomSheetState(false)
                },
                onClickCamera = {
                    if (context.verifyPermission(Manifest.permission.CAMERA)) {
                        requestCameraPermission.launch(Manifest.permission.CAMERA)
                    } else {
                        onNavigateToCameraDisplay()
                    }
                    viewModel.setModalBottomSheetState(false)
                }
            )
        }
    }
}

fun NavController.navigateToUserForm() {
    navigate(userFormGraphRoute)
}


