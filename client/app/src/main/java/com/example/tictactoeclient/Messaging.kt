package com.example.tictactoeclient

import com.example.models.GameState
import com.example.models.MakeTurn
import kotlinx.coroutines.flow.Flow

interface Messaging {
    fun getGameStateStream() : Flow<GameState>
    suspend fun sendAction(action: MakeTurn)
    suspend fun close()
}