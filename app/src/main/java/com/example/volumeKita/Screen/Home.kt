package com.example.volumeKita.Screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.volumeKita.CostumeUi.LoadingScreen
import com.example.volumeKita.CostumeUi.VolumeBarHorizontal
import com.example.volumeKita.Event.EventMain
import com.example.volumeKita.State.MainState
import com.example.volumeKita.ViewModel.MainViewModel
import com.example.volumeKita.ui.theme.MyShapes
import com.example.volumeKita.R as _R


@Composable
fun Home(navController: NavHostController,modifier: Modifier = Modifier,viewModel: MainViewModel) {
    
    val stateUi by  viewModel.uiState.collectAsState()

    HomeUi(
        modifier = modifier,
        eventMain = viewModel::onEvent,
        stateUi = stateUi
    )
}

@Composable
fun HomeUi(
    modifier: Modifier = Modifier,
    loadingScreen: Boolean = false,
    eventMain: (EventMain) -> Unit,
    stateUi: MainState
           ) {
    val scrollMain = rememberScrollState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollMain),

    ){
        Surface(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(color = Color.Blue, shape = MyShapes.Round10Shape)
        ) {
            VolumeBarHorizontal(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                volumeSystem = stateUi.soundMusic,
                title = "Music",
                iconSound = painterResource(stateUi.musicIconResource),
                onClickIcon = {
                    eventMain(EventMain.onIconMusic)
                },
                onTouch = {size, offsetClicked ->
                    eventMain(EventMain.onTouchMusic(size,offsetClicked))
                },
                onUpTouch = {size , offsetClicked ->
                    eventMain(EventMain.onUpTouchMusic(size,offsetClicked))
                }
            )
        }
        Surface(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(color = Color.Blue, shape = MyShapes.Round10Shape)
        ) {
            VolumeBarHorizontal(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                volumeSystem = stateUi.soundNotification,
                title = "Notification",
                iconSound = painterResource(stateUi.notificationIconResource),
                onClickIcon = {
                    eventMain(EventMain.onIconNotification)
                },
                onTouch = {size, offsetClicked ->
                    eventMain(EventMain.onTouchNotification(size,offsetClicked))
                },
                onUpTouch = {size , offsetClicked ->
                    eventMain(EventMain.onUpTouchNotification(size,offsetClicked))
                }
            )
        }
        Surface(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(color = Color.Blue, shape = MyShapes.Round10Shape)
        ) {
            VolumeBarHorizontal(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                volumeSystem = stateUi.soundAlarm,
                title = "Alarm",
                iconSound = painterResource(stateUi.alarmIconResource),
                onClickIcon = {
                    eventMain(EventMain.onIconAlarm)
                },
                onTouch = {size, offsetClicked ->
                    eventMain(EventMain.onTouchAlarm(size,offsetClicked))
                },
                onUpTouch = {size , offsetClicked ->
                    eventMain(EventMain.onUpTouchAlarm(size,offsetClicked))
                }
            )
        }

    }

    // background
    if(loadingScreen){
        LoadingScreen(
            modifier = modifier
                .fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHome() {
    HomeUi(
        eventMain = {},
        stateUi = MainState()
    )
}

// system config

fun Float.toFixed(target: Int): Float{
    return (this * Math.pow(10.0,target.toDouble()).toInt() / Math.pow(10.0,target.toDouble()).toFloat())
}