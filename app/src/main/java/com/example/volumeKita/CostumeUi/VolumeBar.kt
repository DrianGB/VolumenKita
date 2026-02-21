package com.example.volumeKita.CostumeUi

import android.R.attr.y
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
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
    onTouch: (size: IntSize, offsetClicked: Offset) -> Unit,
    onUpTouch: (size: IntSize, offsetClicked: Offset) -> Unit
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

            val newVolumeSystem = volumeSystem.toFixed(2)

            SoundControl(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                volumeSystem = newVolumeSystem,
                onTouch = onTouch,
                onUpTouch = onUpTouch
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
        volumeSystem = 1f,
        title = "Sound Game",
        iconSound = painterResource(R.drawable.sound),
        onClickIcon = {},
        onTouch = {size, offset ->

        },
        onUpTouch = {size, offset ->

        }
    )
}

@Composable
private fun SoundControl(modifier: Modifier = Modifier,
                         volumeSystem: Float = 0f,
                         onTouch: (size: IntSize, offsetClicked: Offset) -> Unit,
                         onUpTouch: (size: IntSize, offsetClicked: Offset) -> Unit
) {

    var lastDrag by remember {mutableStateOf(Offset(0f,0f))}

    Canvas(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .pointerInput(Unit){
                detectDragGestures(
                    onDragStart = {offset: Offset ->

                    },
                    onDrag = { change: PointerInputChange, dragAmount: Offset ->
                        onTouch(size,change.position)
                        lastDrag = change.position
                            change.consume()
                    },
                    onDragEnd = {
                        onUpTouch(size,lastDrag)
                    }
                )
            }
            .then(modifier)
    ) {
        val centerPositionHeight = size.height / 2
        drawLine(
            color = Color.Gray,
            start = Offset(5f,centerPositionHeight),
            end = Offset((size.width - 5),centerPositionHeight),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.Blue.copy(0.8f),
            start = Offset(5f,centerPositionHeight),
            end = Offset(
                x = if(((size.width - 25) * volumeSystem) < 25) 25f else (size.width - 25) * volumeSystem,
                y = centerPositionHeight
            ),
            strokeWidth = 8f,
            cap = StrokeCap.Round
        )
        drawArc(
            color = Color.Blue,
            startAngle = 90f,
            sweepAngle = 360f,
            topLeft = Offset(
                x = if(((size.width - 25) * volumeSystem) < 25f) 0f else (size.width - 25) * volumeSystem,
                y = centerPositionHeight - 25f
            ),
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
        volumeSystem = 0f,
        onTouch = {size, offset ->

        },
        onUpTouch = {size, offset ->

        }
    )
}


// system config

fun Float.toFixed(target: Int): Float{
    return (this * Math.pow(10.0,target.toDouble()).toInt() / Math.pow(10.0,target.toDouble()).toFloat())
}