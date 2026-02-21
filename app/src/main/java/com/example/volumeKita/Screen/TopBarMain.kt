package com.example.volumeKita.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TopBarMain(onIconClick: () -> Unit) {
    Row(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 5.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(
            onClick = {
                onIconClick()
            }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More Settings"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTopBarMain() {
    TopBarMain(
        {}
    )
}