package com.example.tttapp.model



data class GameState(
    var activePlayer: Int = 1,
    val player1: ArrayList<Int> = ArrayList(),
    val player2: ArrayList<Int> = ArrayList(),
    var isVsComputer: Boolean = false
)