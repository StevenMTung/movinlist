package br.com.steventung.movinlist.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import br.com.steventung.movinlist.extensions.showMessage

fun Context.takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (Uri) -> Unit = {},
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(this),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedImage = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )
                val imageUri = saveImageToGallery(rotatedImage)
                imageUri?.let {
                    onPhotoTaken(it)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                showMessage("Erro: Não foi possível tirar a foto")
            }

        }
    )
}