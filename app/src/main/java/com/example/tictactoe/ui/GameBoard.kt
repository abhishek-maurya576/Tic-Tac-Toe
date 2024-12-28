package com.example.tictactoe.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.model.Cell
import com.example.tictactoe.model.GameState
import com.example.tictactoe.model.GameStatus

@Composable
fun GameBoard(
    gameState: GameState,
    onCellClick: (row: Int, col: Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when (gameState.gameStatus) {
                GameStatus.ONGOING -> "Current Player: ${gameState.currentPlayer}"
                GameStatus.WIN -> "Player ${gameState.currentPlayer} Wins!"
                GameStatus.DRAW -> "Game Draw!"
            },
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )

        gameState.board.forEachIndexed { rowIndex, row ->
            Row {
                row.forEachIndexed { colIndex, cell ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(1.dp, Color.Black)
                            .clickable(
                                enabled = cell == Cell.EMPTY && 
                                         gameState.gameStatus == GameStatus.ONGOING,
                                onClick = { onCellClick(rowIndex, colIndex) }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (cell) {
                                Cell.X -> "X"
                                Cell.O -> "O"
                                Cell.EMPTY -> ""
                            },
                            fontSize = 40.sp,
                            color = if (Pair(rowIndex, colIndex) in gameState.winningCells) {
                                Color.Red
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
                        )
                    }
                }
            }
        }
    }
} 