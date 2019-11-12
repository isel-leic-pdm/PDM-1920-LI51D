package edu.isel.adeetc.pdm.tictactoe.game.local

import androidx.lifecycle.ViewModel
import edu.isel.adeetc.pdm.tictactoe.game.model.Game

/**
 * The View Model used in the [GameActivity].
 */
class GameViewModel(val game: Game) : ViewModel()
