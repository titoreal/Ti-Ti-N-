package com.example.tttapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.app.AlertDialog
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import com.example.tttapp.utils.SoundManager
import com.example.tttapp.viewmodel.GameViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: GameViewModel
    private var isVsComputer = false
    private lateinit var buttons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        buttons = listOf(
            findViewById(R.id.bu1), findViewById(R.id.bu2), findViewById(R.id.bu3),
            findViewById(R.id.bu4), findViewById(R.id.bu5), findViewById(R.id.bu6),
            findViewById(R.id.bu7), findViewById(R.id.bu8), findViewById(R.id.bu9)
        )

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            isVsComputer = checkedId == R.id.vsComputer
        }
    }

    fun buClick(view: View) {
        val buSelected = view as Button
        val cellId = buttons.indexOf(buSelected) + 1
        playGame(cellId, buSelected)
    }

    private fun playGame(cellId: Int, buSelected: Button) {
        viewModel.updateGameState(cellId)
        updateUI(buSelected)
        SoundManager.playSound(this, viewModel.getSoundForMove())

        val gameResult = viewModel.checkWinner()
        if (gameResult != 0) {
            checkWinner()
            return
        }

        if (isVsComputer && viewModel.gameState.activePlayer == 2) {
            viewModel.autoPlay()
            updateUIAfterAutoPlay()
            SoundManager.playSound(this, viewModel.getSoundForMove())
            checkWinner()
        }
    }

    private fun updateUI(buSelected: Button) {
        buSelected.isEnabled = false
        buSelected.text = ""
        buSelected.setBackgroundResource(
            if (viewModel.gameState.activePlayer == 2) R.drawable.equis else R.drawable.zero
        )
    }

    private fun updateUIAfterAutoPlay() {
        val lastMove = viewModel.gameState.player2.last()
        val buSelected = buttons[lastMove - 1]
        updateUI(buSelected)
    }

    private fun checkWinner() {
        when (val winner = viewModel.checkWinner()) {
            1, 2 -> {
                SoundManager.playSound(this, viewModel.getWinnerSound())
                showGameEndDialog(
                    if (winner == 1) "El jugador 1 ganó el juego"
                    else if (isVsComputer) "Perdiste...!"
                    else "El jugador 2 ganó el juego"
                )
            }
            3 -> {
                SoundManager.playSound(this, viewModel.getTieSound())
                showGameEndDialog("Empate...!")
            }
        }
    }

    private fun showGameEndDialog(message: String) {
        runOnUiThread {
            AlertDialog.Builder(this)
                .setTitle("Juego concluido")
                .setMessage(message)
                .setPositiveButton("Reinicio") { _, _ -> restartGame() }
                .setNegativeButton("Salir") { _, _ -> finish() }
                .setCancelable(false)
                .show()
        }
    }

    private fun restartGame() {
        viewModel.restartGame()
        buttons.forEach { button ->
            button.text = ""
            button.setBackgroundResource(R.drawable.empty)
            button.isEnabled = true
        }
    }
    }
