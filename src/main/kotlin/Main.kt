import building.*
import com.github.ocraft.s2client.bot.S2Agent
import com.github.ocraft.s2client.bot.S2Coordinator
import com.github.ocraft.s2client.bot.gateway.UnitInPool
import com.github.ocraft.s2client.protocol.data.Abilities
import com.github.ocraft.s2client.protocol.data.Units
import com.github.ocraft.s2client.protocol.debug.Color
import com.github.ocraft.s2client.protocol.game.BattlenetMap
import com.github.ocraft.s2client.protocol.game.Difficulty
import com.github.ocraft.s2client.protocol.game.Race
import common.myUnits
import debug.debugColorSequence
import debug.drawBoxAround
import map.resourceClusters
import map.resourceFields
import scv.mineClosestMineralFieldWith
import scv.trainMarineWith
import scv.trainScvWith

@ExperimentalStdlibApi
@ExperimentalUnsignedTypes
class Bot : S2Agent() {

    override fun onBuildingConstructionComplete(unit: UnitInPool) {
        when (unit.unit().type) {
            Units.TERRAN_SUPPLY_DEPOT -> lowerSupplyDepot(unit)
        }
    }

    override fun onUnitIdle(unit: UnitInPool) {
        when (unit.unit().type) {
            Units.TERRAN_COMMAND_CENTER -> if (myScvs.count() < 30) {
                trainScvWith(unit)
            }
            Units.TERRAN_SCV -> mineClosestMineralFieldWith(unit)
            Units.TERRAN_BARRACKS -> trainMarineWith(unit)
        }
    }

    override fun onStep() {
        if (workersBuilding(BuildingType.SupplyDepot).size < optimalNumOfWorkersBuildingSupplyDepots) {
            buildSupplyDepotWith(mineralMiningScvs.first())
        }

        myScvs.forEach {
            drawBoxAround(it)
        }

        val rc = resourceClusters

        rc.map { it.resourceFields }
            .zip(debugColorSequence.asIterable())
            .forEach { (resources, color) ->
                resources.forEach {
                    drawBoxAround(
                        unit = it,
                        diagonalLength = 2.0f,
                        color = color
                    )
                }
            }

        print(rc.size)

        debug().sendDebug()

        if (supply > 16 && minerals > 150 && isEveryBuildingTraining && workersBuilding(BuildingType.Barack).isEmpty()) {
            buildBarackWith(mineralMiningScvs.first())
        }

        if (supply > 100) {
            observation().myUnits
                .filter { it.unit().type == Units.TERRAN_MARINE }
                .forEach {
                    actions().unitCommand(it.unit(), Abilities.ATTACK_ATTACK, observation().gameInfo.startRaw.get().startLocations.last(), false)
                }
        }
    }
}

@ExperimentalUnsignedTypes
@ExperimentalStdlibApi
fun main() {
    val coordinator = S2Coordinator.setup()
        .loadSettings(arrayOf())
        .setWindowSize(1920, 1080)
        .setParticipants(
            S2Coordinator.createParticipant(Race.TERRAN, Bot()),
            S2Coordinator.createComputer(Race.ZERG, Difficulty.HARD)
        )
        .launchStarcraft()
        .startGame(BattlenetMap.of("Submarine LE"))

    var gameActive = true
    while (gameActive) {
        gameActive = coordinator.update()
    }

    coordinator.quit()
}

