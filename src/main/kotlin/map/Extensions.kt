package map

import SC2APIProtocol.Common
import com.github.ocraft.s2client.bot.S2Agent
import com.github.ocraft.s2client.bot.gateway.ObservationInterface
import com.github.ocraft.s2client.protocol.data.Units
import com.github.ocraft.s2client.protocol.debug.Color
import com.github.ocraft.s2client.protocol.observation.spatial.ImageData
import com.github.ocraft.s2client.protocol.spatial.Point
import com.github.ocraft.s2client.protocol.spatial.Point2d
import common.neutrals
import kotlin.math.roundToInt

private val mineralFieldTypes = setOf(
    Units.NEUTRAL_BATTLE_STATION_MINERAL_FIELD,
    Units.NEUTRAL_BATTLE_STATION_MINERAL_FIELD750,
    Units.NEUTRAL_LAB_MINERAL_FIELD,
    Units.NEUTRAL_LAB_MINERAL_FIELD750,
    Units.NEUTRAL_MINERAL_FIELD,
    Units.NEUTRAL_MINERAL_FIELD450,
    Units.NEUTRAL_MINERAL_FIELD750,
    Units.NEUTRAL_PURIFIER_MINERAL_FIELD,
    Units.NEUTRAL_PURIFIER_MINERAL_FIELD750,
    Units.NEUTRAL_PURIFIER_RICH_MINERAL_FIELD,
    Units.NEUTRAL_PURIFIER_RICH_MINERAL_FIELD750,
    Units.NEUTRAL_RICH_MINERAL_FIELD,
    Units.NEUTRAL_RICH_MINERAL_FIELD750,
    Units.NEUTRAL_MINERAL_FIELD_OPAQUE,
    Units.NEUTRAL_MINERAL_FIELD_OPAQUE900
)

val S2Agent.mineralFields get() = observation().mineralFields
val ObservationInterface.mineralFields
    get() = neutrals.filter { mineralFieldTypes.contains(it.unit().type) }

fun S2Agent.isBuildable(point: Point2d) = observation()
    .gameInfo
    .startRaw.get()
    .placementGrid
    .getBit(point.y.toInt() * mapWidth + point.x.toInt()) != 0

fun S2Agent.getMapHeightAtLocation(point: Point2d) = observation()
    .gameInfo
    .startRaw.get()
    .terrainHeight
    .data[point.y.toInt() * mapWidth + point.x.toInt()] - 127 / 8.0f

fun S2Agent.showPlacementGrid() {
    for (y in 0 until mapHeight) {
        for (x in 0 until mapWidth) {
            val point = Point2d.of(x.toFloat(), y.toFloat())
            val z = getMapHeightAtLocation(point)
            debug().debugSphereOut(
                Point.of(x.toFloat(), y.toFloat(), z.toFloat()),
                0.3f,
                if (isBuildable(point)) {
                    Color.TEAL
                } else {
                    Color.PURPLE
                }
            )
        }
    }
    debug().sendDebug()
}


val S2Agent.mapWidth get() = observation()
    .gameInfo
    .startRaw.get()
    .mapSize.x

val S2Agent.mapHeight get() = observation()
    .gameInfo
    .startRaw.get()
    .mapSize.y

// 0000001 = 1
// 1000000 = 64
// 0000001
fun ImageData.getBit(index: Int) = data[index / 8].toInt() shr 7 - (index % 8) and 1
