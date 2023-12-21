package com.atwa.androidgpstask.designsystem.widget

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen() {
    CircularProgressIndicator(
        modifier = Modifier
            .progressSemantics()
            .size(50.dp),
    )
}