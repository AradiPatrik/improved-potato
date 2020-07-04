package map

import com.github.ocraft.s2client.bot.S2Agent
import com.github.ocraft.s2client.bot.gateway.ObservationInterface
import com.github.ocraft.s2client.bot.gateway.UnitInPool
import com.github.ocraft.s2client.protocol.debug.Color
import com.github.ocraft.s2client.protocol.observation.spatial.ImageData
import com.github.ocraft.s2client.protocol.spatial.Point
import com.github.ocraft.s2client.protocol.spatial.Point2d
import common.neutrals

data class ResourceCluster(
    val minerals: Set<UnitInPool>,
    val vespeneGeysers: Set<UnitInPool>
)

val ResourceCluster.resourceFields get() = minerals + vespeneGeysers

val S2Agent.mineralFields get() = observation().mineralFields
val ObservationInterface.mineralFields get() = neutrals.filter { isMineralField(it) }
fun isMineralField(unit: UnitInPool) = mineralFieldTypes.contains(unit.unit().type)

val S2Agent.vespeneGeysers get() = observation().vespeneGeysers
val ObservationInterface.vespeneGeysers get() = neutrals.filter { isVespeneGeyser(it) }
fun isVespeneGeyser(unit: UnitInPool) = vespeneGasTypes.contains(unit.unit().type)

val S2Agent.resourceFields get() = mineralFields + vespeneGeysers

fun S2Agent.isBuildable(point: Point2d) = observation()
    .gameInfo
    .startRaw.get()
    .placementGrid
    .getBit(point.y.toInt() * mapWidth + point.x.toInt()) != 0

@ExperimentalUnsignedTypes
fun S2Agent.getMapHeightAtLocation(point: Point2d) = observation()
    .gameInfo
    .startRaw.get()
    .terrainHeight
    .data[point.y.toInt() * mapWidth + point.x.toInt()].toUByte().toInt().minus(127) / 8.0f

@ExperimentalUnsignedTypes
fun S2Agent.showPlacementGrid() {
    for (y in 0 until mapHeight) {
        for (x in 0 until mapWidth) {
            val point = Point2d.of(x.toFloat(), y.toFloat())
            val z = getMapHeightAtLocation(point)
            debug().debugSphereOut(
                Point.of(x.toFloat(), y.toFloat(), z),
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

val S2Agent.mapWidth
    get() = observation()
        .gameInfo
        .startRaw.get()
        .mapSize.x

val S2Agent.mapHeight
    get() = observation()
        .gameInfo
        .startRaw.get()
        .mapSize.y

fun ImageData.getBit(index: Int) = data[index / 8].toInt() shr 7 - (index % 8) and 1

@ExperimentalStdlibApi
val S2Agent.resourceClusters: Set<ResourceCluster>
    get() {
        val averageXDistance = resourceFields.averageByDifferenceOf { it.unit().position.x }
        val averageYDistance = resourceFields.averageByDifferenceOf { it.unit().position.y }
        val sortedByX = resourceFields.sortedBy { it.unit().position.x }
        val clusteredByX = sortedByX.clusterBy(averageXDistance * 10) { it.unit().position.x }

        val clusterGrid = clusteredByX.map { xCluster ->
            val sortedByY = xCluster.sortedBy { it.unit().position.y }
            sortedByY.clusterBy(averageYDistance * 10) { it.unit().position.y }
        }

        return clusterGrid
            .flatten()
            .map { cluster ->
                ResourceCluster(
                    minerals = cluster.filter { isMineralField(it) }.toSet(),
                    vespeneGeysers = cluster.filter { isVespeneGeyser(it) }.toSet()
                )
            }
            .toSet()
    }

fun List<UnitInPool>.averageByDifferenceOf(block: (UnitInPool) -> Float) = map { block(it) }
    .sorted()
    .chunked(2)
    .map { (first, second) -> second - first }
    .average()
    .toFloat()

@ExperimentalStdlibApi
fun List<UnitInPool>.clusterBy(threshold: Float, keyExtractor: (UnitInPool) -> Float) =
    fold(listOf(listOf<UnitInPool>())) { clustered, current ->
        when {
            clustered.last().isEmpty() -> listOf(listOf(current))
            keyExtractor(current) - keyExtractor(clustered.last().last()) <= threshold ->
                clustered.mapLast { it + current }
            else -> buildList {
                addAll(clustered)
                add(listOf(current))
            }
        }
    }

@ExperimentalStdlibApi
fun <T> List<T>.mapLast(mapper: (T) -> T): List<T> = buildList<T> {
    addAll(this@mapLast.dropLast(1))
    add(mapper(this@mapLast.last()))
}
