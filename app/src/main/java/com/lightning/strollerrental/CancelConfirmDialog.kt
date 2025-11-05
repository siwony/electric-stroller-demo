package com.lightning.strollerrental

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun CancelConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "진행 취소")
        },
        text = {
            Text(text = "진행을 취소할까요?")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("예")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("아니오")
            }
        }
    )
}
