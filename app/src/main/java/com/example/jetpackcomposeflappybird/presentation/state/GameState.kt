package com.example.jetpackcomposeflappybird.presentation.state

data class GameState(private val status: Status = Status.GAMEOVER) {
    enum class Status {
        RUNNING,
        ENDING,
        GAMEOVER
    }

    fun isRunning() = status == Status.RUNNING     // playing
    fun isGoingToEnd() = status == Status.ENDING   // bird dead, game is going to ending
    fun isGameOver() = status == Status.GAMEOVER   // at start and when bird is dead = game over
}
