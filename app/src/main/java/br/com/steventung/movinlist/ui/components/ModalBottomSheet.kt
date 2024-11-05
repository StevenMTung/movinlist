package br.com.steventung.movinlist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.steventung.mobilist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetImageFile(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onClickPhotoAlbum: () -> Unit = {},
    onClickCamera: () -> Unit = {}

) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        modifier = modifier.fillMaxWidth(),
        content = {
            BottomSheetFiles(onClickPhotoAlbum, onClickCamera)
        }
    )
}

@Composable
private fun BottomSheetFiles(
    onClickPhotoAlbum: () -> Unit = {},
    onClickCamera: () -> Unit = {}
) {
    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Adicione uma Imagem",
                modifier = Modifier
                    .padding(16.dp).align(Alignment.Center),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
                IconButton(
                    onClick = onClickPhotoAlbum,
                    modifier = Modifier.background(
                        color = colorResource(R.color.icon_photo_album),
                        shape = CircleShape
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_action_image_file),
                        contentDescription = "ícone album de fotos",
                        tint = Color.White
                    )
                }
                Text(text = "Album", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            Column {
                IconButton(
                    onClick = onClickCamera,
                    modifier = Modifier.background(
                        color = colorResource(R.color.icon_camera),
                        shape = CircleShape
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_action_camera),
                        contentDescription = "ícone câmera",
                        tint = Color.White
                    )
                }
                Text(text = "Câmera", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
        Spacer(Modifier.height(30.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun ModalBottomSheetFilePreview() {
    BottomSheetFiles()
}