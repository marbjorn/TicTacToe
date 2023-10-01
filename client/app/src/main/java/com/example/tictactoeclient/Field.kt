package com.example.tictactoeclient

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontSynthesis.Companion.Style
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.models.GameState

@Composable
fun Field(
    state : GameState,
    modifier: Modifier = Modifier,
    playerXColor : Color = Color.Green,
    playerYColor : Color = Color.Red,
    onTapField : (x : Int, y : Int) -> Unit = {_,_->}
) {
    Canvas(modifier = modifier.pointerInput(true){
        detectTapGestures {
            val x = (3*it.x.toInt() / size.width)
            val y = (3*it.y.toInt() / size.height)
            onTapField(x, y)
        }
    }) {
        drawGrid()

        state.field.forEachIndexed { y, chars ->
            chars.forEachIndexed { x, c ->
                val offset = Offset(
                    x = size.width * (1 / 6f + x / 3f),
                    y = size.height * (1 / 6f + y / 3f),
                )
                when (c) {
                    'X' -> drawX(
                        color = playerXColor,
                        center = offset
                    )
                    'O' -> drawO(
                        color = playerYColor,
                        center = offset
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawGrid() {
    drawFieldLine(
        start = Offset(
            x = size.width * (1 / 3f),
            y = 0f
        ),
        end = Offset(
            x = size.width * (1 / 3f),
            y = size.height
        )
    )
    drawFieldLine(
        start = Offset(
            x = size.width * (2 / 3f),
            y = 0f
        ),
        end = Offset(
            x = size.width * (2 / 3f),
            y = size.height
        )
    )
    drawFieldLine(
        start = Offset(
            x = 0f,
            y = size.height * (1 / 3f)
        ),
        end = Offset(
            x = size.width,
            y = size.height * (1 / 3f)
        )
    )

    drawFieldLine(
        start = Offset(
            x = 0f,
            y = size.height * (2 / 3f)
        ),
        end = Offset(
            x = size.width,
            y = size.height * (2 / 3f)
        )
    )
}

private fun DrawScope.drawFieldLine(
    start : Offset,
    end : Offset
) {
    drawLine(
        color = Color.Black,
        start = start,
        end = end,
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawX(
    color : Color,
    center : Offset,
    size : Size = Size(50.dp.toPx(), 50.dp.toPx())
) {
    drawLine(
        color = color,
        start = Offset(
            x = center.x - size.width/2f,
            y = center.y - size.height/2f
        ),
        end = Offset(
            x = center.x + size.width/2f,
            y = center.y + size.height/2f
        ),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(
            x = center.x + size.width/2f,
            y = center.y - size.height/2f
        ),
        end = Offset(
            x = center.x - size.width/2f,
            y = center.y + size.height/2f
        ),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawO(
    color : Color,
    center : Offset,
    size : Size = Size(50.dp.toPx(), 50.dp.toPx())
) {
    drawCircle(
        color = color,
        center = center,
        radius = size.width/2f,
        style = Stroke(
            width = 3.dp.toPx()
        )
    )
}

@Preview
@Composable
fun FieldPreview() {
    Field(
        state = GameState(
            field = arrayOf(
                arrayOf('X', null, null),
                arrayOf(null, 'O', null),
                arrayOf(null, 'X', 'X')
            )
        ),
        onTapField = { _, _ ->},
        modifier = Modifier.size(300.dp)
    )
}