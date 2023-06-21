package com.example.jetpackcomposeflappybird

import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.res.loadImageResource
import com.example.jetpackcomposeflappybird.presentation.state.BackgroundState
import com.example.jetpackcomposeflappybird.presentation.state.BirdState
import com.example.jetpackcomposeflappybird.presentation.state.GameState
import com.example.jetpackcomposeflappybird.presentation.state.PipesState


@Composable
fun GameConsole(
    modifier: Modifier = Modifier,
    gameState: MutableState<GameState>,
    jumpState: MutableState<Boolean>,
    gameScore: MutableState<Int>
    ) {

    // load sprite sheet
    val bird = loadImageResource(id = R.drawable.flappy)
    val background = loadImageResource(id = R.drawable.background)
    val upPipe = loadImageResource(id = R.drawable.pipe_up)
    val downPipe = loadImageResource(id = R.drawable.pipe_down)

    val animatedFloat = animatedFloat(initVal = 0f)

    // states
    val birdState = remember(bird) { BirdState(bird) }
    val backgroundState = remember(background) { BackgroundState(background) }
    val pipesState = remember(upPipe, downPipe) { PipesState(upPipe, downPipe, gameScore) }

    // if jumpState.value == true, then execute birdState.jump()
    remember(jumpState.value) { birdState.jump() }

    // draw game: redraw ( = recompose) each items (state) changes
    Canvas(modifier = modifier) {
        // define game limit
        val gameRect = Rect(0f, 0f, size.width, size.height)

        // game over condition
        if (animatedFloat.isRunning && gameState.value.isGameOver()) {
            animatedFloat.stop()
        // game start, init
        } else if (!animatedFloat.isRunning && gameState.value.isRunning()) {
            // game clock
            animatedFloat.snapTo(0f)
            animatedFloat.animateTo(
                targetValue = 1f,
                anim = infiniteRepeatable(
                    // iterations = AnimationConstants.Infinite,
                    animation = tween(durationMillis = 250, easing = LinearEasing),
                )
            )
            // init sprites
            pipesState.init(this)
            birdState.init()
            gameScore.value = 0
        }
        // get the time node at which key frame animation is (= tick): a tick is every 250ms
        // it is got every 250ms
        val tick = animatedFloat.value

        // Game running
        if (gameState.value.isRunning()) {
            backgroundState.move()
            birdState.move()
            pipesState.move()
        } else if (gameState.value.isGoingToEnd()){
            birdState.dying()
        }

        // draw background on screen
        backgroundState.draw(this, tick)

        // define collision rect for pipes and bird
        val pipeRects = pipesState.draw(this, tick)
        val birdRect = birdState.draw(this, tick, gameState.value)

        if (gameState.value.isRunning()) {
            checkCollision(gameState, gameRect, pipeRects, birdRect)
        } else if (gameState.value.isGoingToEnd()) {
            finishFalling(gameState, gameRect, birdRect)
        }
    }
}


private fun finishFalling(
    animationStart: MutableState<GameState>,
    gameRect: Rect,
    birdRect: Rect) {
    // bird sprite out of screen
    if (!gameRect.overlaps(birdRect)) {
        animationStart.value = GameState(GameState.Status.GAMEOVER)
    }
}

private fun checkCollision(
    animationStart: MutableState<GameState>,
    gameRect: Rect,
    pipeRects: List<Rect>,
    birdRect: Rect) {
    if (!gameRect.overlaps(birdRect) || pipeRects.firstOrNull { it.overlaps(birdRect) } != null) {
        animationStart.value = GameState(GameState.Status.ENDING)
    }
}
