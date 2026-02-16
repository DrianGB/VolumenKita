package com.example.volumeKita.CostumeUi

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.volumeKita.R
import com.example.volumeKita.ui.theme.MyShapes

@Composable
fun VolumeBarHorizontal(
    modifier: Modifier = Modifier,
    title: String,
    volumeSystem: Float = 0f,
    iconSound: Painter,
    onClickIcon: () -> Unit,
    onPointerInputVolume: PointerInputScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(MyShapes.Round10Shape)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MyShapes.Round10Shape
            )
            .border(
                width = 2.dp,
                color = Color.Black.copy(alpha = 0.5f),
                shape = MyShapes.Round10Shape
            )
            .padding(5.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(10.dp),
            text = title,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Bold
        )
        Row{
            IconButton(
                onClick = {
                    onClickIcon()
                }
            ) {
                Icon(
                    painter = iconSound,
                    contentDescription = "No Sound"
                )
            }
            SoundControl(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                volumeSystem = volumeSystem,
                pointerInputSound = onPointerInputVolume
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewVolumeBarHorizontal() {
    VolumeBarHorizontal(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        volumeSystem = 0.3f,
        title = "Sound Game",
        iconSound = painterResource(R.drawable.sound),
        onClickIcon = {},
        onPointerInputVolume = {}
    )
}

@Composable
private fun SoundControl(modifier: Modifier = Modifier,volumeSystem: Float = 0f,pointerInputSound: PointerInputScope.() -> Unit) {
    Canvas(
        modifier = Modifier
            .padding(3.dp)
            .pointerInput(Unit){
                pointerInputSound()
            }
            .then(modifier)
    ) { 
        val centerPositionHeight = size.height / 2
        drawLine(
            color = Color.Gray,
            start = Offset(25f,centerPositionHeight),
            end = Offset((size.width - 5),centerPositionHeight),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.Blue.copy(0.8f),
            start = Offset(25f,centerPositionHeight),
            end = Offset((size.width - 5) * volumeSystem,centerPositionHeight),
            strokeWidth = 8f,
            cap = StrokeCap.Round
        )
        drawArc(
            color = Color.Blue,
            startAngle = 90f,
            sweepAngle = 360f,
            topLeft = Offset((size.width - 5) * volumeSystem,centerPositionHeight - 25f),
            size = Size(50f,50f),
            useCenter = true
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSoundControl() {
    SoundControl(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth(),
        volumeSystem = 0.5f,
        {}
    )
}