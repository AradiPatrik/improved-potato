package building

import building.BuildingType.Companion.toBuildingType
import com.github.ocraft.s2client.bot.S2Agent
import com.github.ocraft.s2client.bot.gateway.ObservationInterface
import com.github.ocraft.s2client.bot.gateway.UnitInPool
import com.github.ocraft.s2client.protocol.data.Units
import com.github.ocraft.s2client.protocol.spatial.Point2d
import common.myUnits
import common.plus
import common.times
import kotlin.random.Random

val S2Agent.foodUntilSupplyBlocked get() = observation().foodUntilSupplyBlocked
val ObservationInterface.foodUntilSupplyBlocked get() = foodCap - foodUsed

fun S2Agent.workersBuilding(buildingType: BuildingType) = observation().workersBuilding(buildingType)
fun ObservationInterface.workersBuilding(buildingType: BuildingType) = myUnits
    .filter { poolUnit ->
        poolUnit.unit().orders.any { order ->
            order.ability.toBuildingType() == buildingType
        }
    }

val S2Agent.myScvs get() = observation().myScvs
val ObservationInterface.myScvs get() = myUnits
    .filter { unitInPool ->
        unitInPool.unit().type == Units.TERRAN_SCV
    }

val ObservationInterface.myCommandCenters get() = myUnits
        .filter { unitInPool ->
            unitInPool.unit().type == Units.TERRAN_COMMAND_CENTER
        }

fun S2Agent.buildSupplyDepotWith(
        scv: UnitInPool,
        position: Point2d? = null,
        shouldQueCommand: Boolean = false
) {
    build(scv, BuildingType.SupplyDepot, position ?: randomPositionNear(scv), shouldQueCommand)
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
