package br.com.steventung.movinlist.ui.dialogs.profiledetailsdialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.steventung.mobilist.R
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailsDialog(
    modifier: Modifier = Modifier,
    state: ProfileDetailsDialogUiState,
    onDismiss: () -> Unit = {},
    onNavigateToUserForm: () -> Unit = {},
    onSignOut: () -> Unit = {}
) {
    BasicAlertDialog(onDismissRequest = onDismiss, modifier = modifier) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .clip(shape = RoundedCornerShape(5.dp))
                .background(color = Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (state.isLoading) {
                Column(
                    modifier = Modifier.size(250.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text(
                        text = "Deslogando...",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            } else {
                AsyncImage(
                    model = state.userImage,
                    contentDescription = "profile photo",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(shape = CircleShape)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.default_profile_picture),
                    error = painterResource(R.drawable.default_profile_picture)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = state.userName,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                TextButton(
                    onClick = onNavigateToUserForm,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Editar perfil")
                }
                TextButton(
                    onClick = onSignOut,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Sair")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileDetailsDialogPreview() {
    ProfileDetailsDialog(state = ProfileDetailsDialogUiState(userName = "José Bonifácio"))
}

@Preview(showBackground = true)
@Composable
private fun ProfileDetailsDialogSignOutPreview() {
    ProfileDetailsDialog(state = ProfileDetailsDialogUiState(isLoading = true))
}