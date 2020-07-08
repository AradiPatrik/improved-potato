package debug

import com.github.ocraft.s2client.bot.S2Agent
import com.github.ocraft.s2client.bot.gateway.UnitInPool
import com.github.ocraft.s2client.protocol.debug.Color
import com.github.ocraft.s2client.protocol.spatial.Point
import com.github.ocraft.s2client.protocol.spatial.Point2d
import com.github.ocraft.s2client.protocol.unit.Unit
import map.getMapHeightAtLocation

fun S2Agent.drawBoxAround(
    unit: UnitInPool,
    diagonalLength: Float = 2f,
    color: Color = Color.TEAL
) {
    drawBoxAround(unit.unit().position, diagonalLength, color)
}

fun S2Agent.drawBoxAround(
    unit: Unit,
    diagonalLength: Float = 2f,
    color: Color = Color.TEAL
) {
    drawBoxAround(unit.position, diagonalLength, color)
}

@ExperimentalUnsignedTypes
fun S2Agent.drawBoxAround(
    point2d: Point2d,
    diagonalLength: Float = 2f,
    color: Color = Color.TEAL
) {
    drawBoxAround(Point.of(point2d.x, point2d.y, getMapHeightAtLocation(point2d)))
}

fun S2Agent.drawBoxAround(
    point: Point,
    diagonalLength: Float = 2f,
    color: Color = Color.TEAL
) {
    val halfEdgeLength = diagonalLength / 4 * SQRT_2
    debug().debugBoxOut(
        Point.of(point.x - halfEdgeLength, point.y - halfEdgeLength, point.z),
        Point.of(point.x + halfEdgeLength, point.y + halfEdgeLength, point.z + halfEdgeLength * 2),
        color
    )
}

const val SQRT_2 = 0.85090352453f
