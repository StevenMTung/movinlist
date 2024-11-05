package br.com.steventung.movinlist.navigation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.steventung.movinlist.extensions.showMessage
import br.com.steventung.movinlist.media.takePhoto
import br.com.steventung.movinlist.media.verifyPermission
import br.com.steventung.movinlist.ui.components.CameraDisplay

internal const val cameraGraphRoute = "cameraGraphRoute"

fun NavGraphBuilder.cameraGraph(
    onNavigateBack: () -> Unit,
    onPhotoTaken: (String) -> Unit
) {
    composable(
        route = cameraGraphRoute,
        arguments = listOf(
            navArgument(houseAreaIdArgument) { nullable = true },
            navArgument(houseAreaNameArgument) { nullable = true },
            navArgument(productIdArgument) { nullable = true }
        )
    ) {
        val context = LocalContext.current
        val controller = remember {
            LifecycleCameraController(context).apply {
                setEnabledUseCases(CameraController.IMAGE_CAPTURE)
            }
        }

        val writeExternalRequestPermission = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                if (granted) {
                    context.takePhoto(
                        controller = controller,
                        onPhotoTaken = { uri ->
                            onPhotoTaken(uri.toString())
                        }
                    )
                } else {
                    context.showMessage("Erro: Não será possível utilizar a foto")
                }
            }
        )

        CameraDisplay(
            modifier = Modifier.fillMaxSize(),
            controller = controller,
            onCloseCamera = {
                onNavigateBack()
            },
            onTakePhoto = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    context.takePhoto(
                        controller = controller,
                        onPhotoTaken = { uri ->
                            onPhotoTaken(uri.toString())
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
                                onPhotoTaken(uri.toString())
                            }
                        )
                    }
                }
            }
        )
    }
}

fun NavController.navigateToCameraDisplay() {
    navigate(route = cameraGraphRoute)
}
