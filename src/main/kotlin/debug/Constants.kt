package debug

import com.github.ocraft.s2client.protocol.debug.Color

val colors = listOf(
    Color.of(0, 0, 255),
    Color.of(0, 255, 0),
    Color.of(0, 255, 255),
    Color.of(255, 0, 0),
    Color.of(255, 0, 255),
    Color.of(255, 255, 0),
    Color.of(255, 255, 255),
    Color.of(125, 125, 255),
    Color.of(125, 255, 125),
    Color.of(125, 255, 255),
    Color.of(255, 125, 125),
    Color.of(255, 125, 255),
    Color.of(255, 255, 125)
)

val debugColorSequence = generateSequence(0) { it + 1 }
    .map { colors[it % colors.size] }
