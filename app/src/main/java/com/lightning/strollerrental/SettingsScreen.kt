package com.lightning.strollerrental

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("설정") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Demo mode info
            ListItem(
                headlineContent = { Text("데모 모드") },
                supportingContent = { Text("이 앱은 데모용으로 실제 BLE 통신 없이 동작합니다.") }
            )

            HorizontalDivider()

            // Log export
            ListItem(
                headlineContent = { Text("로그/진단 내보내기") },
                modifier = Modifier.clickable {
                    Toast.makeText(context, "로그 내보내기 기능은 데모에서 제공되지 않습니다", Toast.LENGTH_SHORT).show()
                }
            )

            HorizontalDivider()

            // Terms
            ListItem(
                headlineContent = { Text("이용약관") },
                modifier = Modifier.clickable {
                    showTermsDialog = true
                }
            )

            HorizontalDivider()

            // Privacy policy
            ListItem(
                headlineContent = { Text("개인정보 처리방침") },
                modifier = Modifier.clickable {
                    showPrivacyDialog = true
                }
            )

            HorizontalDivider()
        }
    }

    // Terms dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = { Text("이용약관") },
            text = { Text("이 앱은 데모용으로 제작되었습니다.\n실제 서비스 이용약관은 정식 버전에서 확인하실 수 있습니다.") },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text("확인")
                }
            }
        )
    }

    // Privacy dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("개인정보 처리방침") },
            text = { Text("이 데모 앱은 어떠한 개인정보도 수집하지 않습니다.\n정식 버전의 개인정보 처리방침은 정식 출시 시 제공됩니다.") },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("확인")
                }
            }
        )
    }
}
