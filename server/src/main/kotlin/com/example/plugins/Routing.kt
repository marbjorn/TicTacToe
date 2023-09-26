package com.example.plugins

import com.example.models.Game
import com.example.models.GameState
import com.example.models.MakeTurn
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json

fun Application.configureRouting(game : Game) {
    routing {
        socket(game)
    }
}


fun Route.socket(game : Game) {
    route("/play") {
        webSocket {
            val player = game.connectPlayer(this)

            if (player == null) {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "2 players already connect"))
                return@webSocket
            }

            try {
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val action = extractAction(frame.readText())
                        game.finishTurn(player, action.x, action.y)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                game.disconnectPlayer(player)
            }
        }
    }
}

private fun extractAction(message : String) : MakeTurn {
    val type = message.substringBefore("#")
    val body = message.substringAfter("#")
    return if (type == "make_turn") {
        Json.decodeFromString(body)
    } else MakeTurn(-1, -1)
}