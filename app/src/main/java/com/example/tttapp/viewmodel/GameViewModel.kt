package com.example.tttapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tttapp.R
import com.example.tttapp.model.GameState

class GameViewModel : ViewModel() {
    val gameState = GameState()

    private val winningCombinations = listOf(
        listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9), // Filas
        listOf(1, 4, 7), listOf(2, 5, 8), listOf(3, 6, 9), // Columnas
        listOf(1, 5, 9), listOf(3, 5, 7) // Diagonales
    )

    fun updateGameState(cellId: Int) {
        if (gameState.activePlayer == 1) {
            gameState.player1.add(cellId)
            gameState.activePlayer = 2
        } else {
            gameState.player2.add(cellId)
            gameState.activePlayer = 1
        }
    }

    fun checkWinner(): Int {
        return when {
            winningCombinations.any { gameState.player1.containsAll(it) } -> 1
            winningCombinations.any { gameState.player2.containsAll(it) } -> 2
            gameState.player1.size + gameState.player2.size == 9 -> 3 // Empate
            else -> 0
        }
    }

    fun autoPlay() {
        val winningMove = findWinningMove(gameState.player2)
        val blockingMove = findWinningMove(gameState.player1)
        val move = when {
            winningMove != null -> winningMove
            blockingMove != null -> blockingMove
            5 !in gameState.player1 && 5 !in gameState.player2 -> 5
            else -> {
                val corners = listOf(1, 3, 7, 9)
                val availableCorners = corners.filter { it !in gameState.player1 && it !in gameState.player2 }
                if (availableCorners.isNotEmpty()) {
                    availableCorners.random()
                } else {
                    (1..9).filter { it !in gameState.player1 && it !in gameState.player2 }.random()
                }
            }
        }
        updateGameState(move)
    }

    private fun findWinningMove(player: ArrayList<Int>): Int? {
        for (combo in winningCombinations) {
            val playerCells = combo.filter { it in player }
            val emptyCells = combo.filter { it !in gameState.player1 && it !in gameState.player2 }
            if (playerCells.size == 2 && emptyCells.size == 1) {
                return emptyCells.first()
            }
        }
        return null
    }

    fun restartGame() {
        gameState.activePlayer = 1
        gameState.player1.clear()
        gameState.player2.clear()
    }

    fun getSoundForMove(): Int {
        return if (gameState.activePlayer == 1) R.raw.jugador1 else R.raw.jugador2
    }

    fun getWinnerSound(): Int {
        return R.raw.jugadorgana
    }

    fun getTieSound(): Int {
        return R.raw.ganadevice
    }
}