package com.example.tictactoe.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.tictactoe.model.Cell
import com.example.tictactoe.model.GameState
import com.example.tictactoe.model.GameStatus
import com.example.tictactoe.model.Player
import com.example.tictactoe.util.SoundManager

class GameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    private var soundManager: SoundManager? = null

    fun initializeSoundManager(context: Context) {
        soundManager = SoundManager(context)
    }

    fun onCellClick(row: Int, col: Int) {
        val currentState = _gameState.value
        
        if (currentState.board[row][col] != Cell.EMPTY || 
            currentState.gameStatus != GameStatus.ONGOING) {
            return
        }

        try {
            soundManager?.playMoveSound()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val newBoard = currentState.board.map { it.toMutableList() }.toMutableList()
        newBoard[row][col] = if (currentState.currentPlayer == Player.X) Cell.X else Cell.O

        val winningCells = checkWin(newBoard)
        val gameStatus = when {
            winningCells.isNotEmpty() -> {
                try {
                    soundManager?.playWinSound()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                GameStatus.WIN
            }
            newBoard.flatten().none { it == Cell.EMPTY } -> {
                try {
                    soundManager?.playDrawSound()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                GameStatus.DRAW
            }
            else -> GameStatus.ONGOING
        }

        val winner = if (gameStatus == GameStatus.WIN) currentState.currentPlayer else null

        _gameState.value = currentState.copy(
            board = newBoard,
            currentPlayer = if (gameStatus == GameStatus.WIN) {
                currentState.currentPlayer
            } else {
                if (currentState.currentPlayer == Player.X) Player.O else Player.X
            },
            gameStatus = gameStatus,
            winningCells = winningCells
        )
    }

    override fun onCleared() {
        super.onCleared()
        soundManager?.release()
        soundManager = null
    }

    fun resetGame() {
        _gameState.value = GameState()
    }

    private fun checkWin(board: List<List<Cell>>): List<Pair<Int, Int>> {
        // Check rows
        for (i in 0..2) {
            if (board[i][0] != Cell.EMPTY && 
                board[i][0] == board[i][1] && 
                board[i][1] == board[i][2]) {
                return listOf(Pair(i, 0), Pair(i, 1), Pair(i, 2))
            }
        }

        // Check columns
        for (i in 0..2) {
            if (board[0][i] != Cell.EMPTY && 
                board[0][i] == board[1][i] && 
                board[1][i] == board[2][i]) {
                return listOf(Pair(0, i), Pair(1, i), Pair(2, i))
            }
        }

        // Check diagonals
        if (board[0][0] != Cell.EMPTY && 
            board[0][0] == board[1][1] && 
            board[1][1] == board[2][2]) {
            return listOf(Pair(0, 0), Pair(1, 1), Pair(2, 2))
        }

        if (board[0][2] != Cell.EMPTY && 
            board[0][2] == board[1][1] && 
            board[1][1] == board[2][0]) {
            return listOf(Pair(0, 2), Pair(1, 1), Pair(2, 0))
        }

        return emptyList()
    }
} 