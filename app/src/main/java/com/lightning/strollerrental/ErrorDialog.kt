package com.lightning.strollerrental

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(
    message: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "오류 발생")
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text("다시 시도")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
