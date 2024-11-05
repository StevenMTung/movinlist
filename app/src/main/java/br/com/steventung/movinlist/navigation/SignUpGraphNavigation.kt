package br.com.steventung.movinlist.navigation

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.steventung.movinlist.extensions.showMessage
import br.com.steventung.movinlist.media.persistUriPermission
import br.com.steventung.movinlist.media.takePhoto
import br.com.steventung.movinlist.media.verifyPermission
import br.com.steventung.movinlist.ui.components.CameraDisplay
import br.com.steventung.movinlist.ui.components.ModalBottomSheetImageFile
import br.com.steventung.movinlist.ui.screens.signup.SignUpScreen
import br.com.steventung.movinlist.ui.screens.signup.SignUpViewModel

internal const val signUpGraphRoute = "signUpGraphRoute"

fun NavGraphBuilder.signUpGraph(onNavigateBack: () -> Unit) {
    composable(route = signUpGraphRoute) {
        val viewModel = hiltViewModel<SignUpViewModel>()
        val state by viewModel.uiState.collectAsState()
        val isSignUpSuccess by viewModel.isSignUpSuccess.collectAsState(false)
        val context = LocalContext.current
        val controller = remember {
            LifecycleCameraController(context).apply {
                setEnabledUseCases(CameraController.IMAGE_CAPTURE)
            }
        }
        LaunchedEffect(isSignUpSuccess) {
            if (isSignUpSuccess) {
                onNavigateBack()
            }
        }
        SignUpScreen(
            state = state,
            onSignUpUser = {
                viewModel.signUpUser(context)
            },
            onAddUserPhoto = {
                viewModel.setModalBottomSheetState(true)
            },
            onChangePasswordVisibility = { passwordVisibility ->
                viewModel.setPasswordVisibility(passwordVisibility)
            },
            onChangePasswordVerifyVisibility = { passwordVerifyVisibility ->
                viewModel.setPasswordVerifyVisibility(passwordVerifyVisibility)
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
            onResult = { Granted ->
                if (Granted) {
                    viewModel.setModalBottomSheetState(true)
                } else {
                    Log.d("Camera", "Permission to access camera not granted")
                }
            }
        )

        val writeExternalRequestPermission = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { Granted ->
                if (Granted) {
                    context.takePhoto(
                        controller = controller,
                        onPhotoTaken = { uri ->
                            viewModel.loadImage(uri.toString())
                            viewModel.setModalBottomSheetState(false)
                        }
                    )
                } else {
                    context.showMessage("Erro: Não será possível utilizar a foto")
                }
            }
        )

        if (state.isShowModalBottomSheet) {
            ModalBottomSheetImageFile(
                onDismiss = {
                    viewModel.setModalBottomSheetState(false)
                },
                onClickCamera = {
                    if (context.verifyPermission(Manifest.permission.CAMERA)) {
                        requestCameraPermission.launch(Manifest.permission.CAMERA)
                    } else {
                        viewModel.setCameraState(true)
                    }
                    viewModel.setModalBottomSheetState(false)
                },
                onClickPhotoAlbum = {
                    pickMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                    viewModel.setModalBottomSheetState(false)
                }
            )
        }

        if (state.isCameraOpen) {
            CameraDisplay(
                modifier = Modifier.fillMaxSize(),
                controller = controller,
                onCloseCamera = {
                    viewModel.setCameraState(false)
                },
                onTakePhoto = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        context.takePhoto(
                            controller = controller,
                            onPhotoTaken = { uri ->
                                viewModel.loadImage(uri.toString())
                                viewModel.setCameraState(false)
                            }
                        )
                    } else {
                        if (context.verifyPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        ) {
                            writeExternalRequestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        } else {
                            context.takePhoto(
                                controller = controller,
                                onPhotoTaken = { uri ->
                                    viewModel.loadImage(uri.toString())
                                    viewModel.setCameraState(false)
                                }
                            )
                        }
                    }
                }
            )
        }
    }
}

fun NavController.navigateToSignUp() {
    navigate(signUpGraphRoute)
}