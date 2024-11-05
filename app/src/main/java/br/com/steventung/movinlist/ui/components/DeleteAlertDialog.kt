package br.com.steventung.movinlist.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DeleteAlertDialog(
    modifier: Modifier = Modifier,
    title: String,
    contentText: String,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancelar")
            }
        },
        title = { Text(text = title) },
        text = { Text(text = contentText) }
    )
}

@Preview(showBackground = true)
@Composable
private fun DeleteAlertDialogPreview() {
    DeleteAlertDialog(
        title = "Deseja remover o cômodo?",
        contentText = "Ao confirmar todos os produtos inseridos neste cômodo também serão removidos."
    )
}