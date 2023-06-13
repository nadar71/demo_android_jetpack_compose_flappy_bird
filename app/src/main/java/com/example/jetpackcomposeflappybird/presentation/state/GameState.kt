package com.example.jetpackcomposeflappybird.presentation.state

data class GameState(private val status: Status = Status.STOPPED) {
    enum class Status {
        RUNNING,
        ENDING,
        STOPPED
    }

    fun isRunning() = status == Status.RUNNING
    fun isEnding() = status == Status.ENDING
    fun isFinished() = status == Status.STOPPED
}
