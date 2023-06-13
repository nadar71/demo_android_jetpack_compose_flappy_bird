package com.example.jetpackcomposeflappybird

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.jetpackcomposeflappybird.presentation.state.GameState

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameScreen()
        }
    }
}

@Composable
fun GameScreen() {
    val gameState  = remember { mutableStateOf(GameState()) }
    val jumpState: MutableState<Boolean> = mutableStateOf(false)
    val gameScore  = remember { mutableStateOf(0) }

    GameConsole(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (gameState.value.isRunning())
                            jumpState.value = !jumpState.value
                    }
                }
                true
            },
        gameState,
        jumpState,
        gameScore
    )
    Text(
        modifier = Modifier.fillMaxWidth(),
        fontSize = 48.sp,
        text = gameScore.value.toString(),
        textAlign = TextAlign.Center
    )
    Toggle(
        modifier = Modifier.fillMaxSize(),
        animationStart = gameState
    ) {
        if (it)
            gameState.value = GameState(GameState.Status.RUNNING)
        else
            gameState.value = GameState(GameState.Status.STOPPED)
    }

}
