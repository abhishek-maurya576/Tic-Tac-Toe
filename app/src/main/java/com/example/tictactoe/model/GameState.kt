package com.example.tictactoe.model

data class GameState(
    val board: List<List<Cell>> = List(3) { List(3) { Cell.EMPTY } },
    val currentPlayer: Player = Player.X,
    val gameStatus: GameStatus = GameStatus.ONGOING,
    val winningCells: List<Pair<Int, Int>> = emptyList()
)

enum class Cell {
    EMPTY, X, O
}

enum class Player {
    X, O
}

enum class GameStatus {
    ONGOING, DRAW, WIN
} 