package building

import building.BuildingType.Companion.toBuildingType
import com.github.ocraft.s2client.bot.S2Agent
import com.github.ocraft.s2client.bot.gateway.ObservationInterface
import com.github.ocraft.s2client.bot.gateway.UnitInPool
import com.github.ocraft.s2client.protocol.data.Abilities
import com.github.ocraft.s2client.protocol.data.Units
import com.github.ocraft.s2client.protocol.spatial.Point2d
import common.myUnits
import common.plus
import common.times
import map.mineralFields
import kotlin.random.Random

val S2Agent.foodUntilSupplyBlocked get() = observation().foodUntilSupplyBlocked
val ObservationInterface.foodUntilSupplyBlocked get() = foodCap - foodUsed

val S2Agent.supply get() = observation().foodUsed
val S2Agent.minerals get() = observation().minerals

val S2Agent.isEveryBuildingTraining
    get() = (myBarracks + myCommandCenters)
        .all { it.unit().orders.isNotEmpty() }

fun S2Agent.workersBuilding(buildingType: BuildingType) = observation().workersBuilding(buildingType)
fun ObservationInterface.workersBuilding(buildingType: BuildingType) = myUnits
    .filter { poolUnit ->
        poolUnit.unit().orders.any { order ->
            order.ability.toBuildingType() == buildingType
        }
    }

val S2Agent.myScvs get() = observation().myScvs
val ObservationInterface.myScvs get() = myUnits.ofType(Units.TERRAN_SCV)

val S2Agent.mineralMiningScvs
    get() = observation().myScvs
        .filter { it.unit().orders.size == 1 }
        .filter { unitInPool ->
            val order = unitInPool.unit().orders.first()
            if (order.targetedUnitTag.isPresent) {
                mineralFields
                    .map { it.tag }
                    .contains(order.targetedUnitTag.get())
            } else {
                false
            }
        }

val S2Agent.myCommandCenters get() = observation().myCommandCenters
val ObservationInterface.myCommandCenters get() = myUnits.ofType(Units.TERRAN_COMMAND_CENTER)

val S2Agent.myBarracks get() = observation().myBarracks
val ObservationInterface.myBarracks get() = myUnits.ofType(Units.TERRAN_BARRACKS)

val S2Agent.miningScvs
    get() = observation().myScvs.filter {
        it.unit().orders.firstOrNull()?.ability == Abilities.HARVEST_GATHER
    }

fun Iterable<UnitInPool>.ofType(type: Units) = filter { unitInPool ->
    unitInPool.unit().type == type
}

fun S2Agent.buildSupplyDepotWith(
    scv: UnitInPool,
    position: Point2d? = null,
    shouldQueCommand: Boolean = false
) {
    build(scv, BuildingType.SupplyDepot, position ?: randomPositionNear(scv), shouldQueCommand)
}

fun S2Agent.buildBarackWith(
    scv: UnitInPool,
    position: Point2d? = null,
    shouldQueCommand: Boolean = false
) {
    build(scv, BuildingType.Barack, position ?: randomPositionNear(scv), shouldQueCommand)
}

private fun S2Agent.build(
    scv: UnitInPool,
    buildingType: BuildingType,
    position: Point2d,
    shouldQueCommand: Boolean
) {
    actions().unitCommand(scv.unit(), buildingType.ability, position, shouldQueCommand)
}

fun randomPositionNear(unit: UnitInPool) = unit.unit().position
    .toPoint2d() + Point2d.of(
    Random.nextFloat() * 2 - 1,
    Random.nextFloat() * 2 - 1
) * 15.0f
