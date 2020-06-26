package scv

import building.myCommandCenters
import building.myScvs
import com.github.ocraft.s2client.bot.S2Agent
import com.github.ocraft.s2client.bot.gateway.UnitInPool
import com.github.ocraft.s2client.protocol.data.Abilities
import com.github.ocraft.s2client.protocol.data.Units
import com.github.ocraft.s2client.protocol.unit.Unit
import map.mineralFields

fun S2Agent.trainScvWith(commandCenter: UnitInPool) {
    require(commandCenter.unit().type == Units.TERRAN_COMMAND_CENTER) {
        "Command center expected, got: ${commandCenter.unit().type.unitTypeId}"
    }
    actions().unitCommand(commandCenter.unit(), Abilities.TRAIN_SCV, false)
}

fun S2Agent.trainMarineWith(barack: UnitInPool) {
    require(barack.unit().type == Units.TERRAN_BARRACKS) {
        "Barack is expected, got: ${barack.unit().type.unitTypeId}"
    }
    actions().unitCommand(barack.unit(), Abilities.TRAIN_MARINE, false)
}

fun S2Agent.mineClosestMineralFieldWith(scv: UnitInPool) {
    require(scv.unit().type == Units.TERRAN_SCV) {
        "Terran scv expected, got: ${scv.unit().type.unitTypeId}"
    }
    actions().unitCommand(
            scv.unit(),
            Abilities.SMART,
            findClosestMineralFieldTo(findClosestCommandCenterTo(scv)).unit()!!,
            false
    )
}

fun S2Agent.findClosestMineralFieldTo(unit: UnitInPool): UnitInPool = observation().mineralFields
        .minBy { it.getSquareDistanceTo(unit) }!!

fun S2Agent.findClosestCommandCenterTo(unit: UnitInPool): UnitInPool = observation().myCommandCenters
        .minBy { it.getSquareDistanceTo(unit) }!!

fun UnitInPool.getSquareDistanceTo(otherUnit: UnitInPool): Double =
        unit().position.toPoint2d().distance(otherUnit.unit().position.toPoint2d())
