package com.example.jetpackcomposeflappybird

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.jetpackcomposeflappybird.presentation.state.GameState

@Composable
fun Toggle(
    modifier: Modifier = Modifier,
    gameStatus: MutableState<GameState>,
    onToggle: (Boolean) -> Unit
) {
    val toggleState = mutableStateOf(false)

    // at start game and everytime the bird is dead
    if (gameStatus.value.isGameOver()) {
        TextButton(
            modifier = modifier,
            onClick = {
                toggleState.value = !gameStatus.value.isRunning()
                onToggle(toggleState.value)
            }
        ) {
            Text(
                fontSize = 32.sp,
                text = if (!gameStatus.value.isGameOver())
                    "\n\n\nClick to Stop" // ? maybe in debug ?
                else
                    "\n\n\nClick to Start"
            )
        }
    }
}
