import building.*
import com.github.ocraft.s2client.bot.S2Agent
import com.github.ocraft.s2client.bot.S2Coordinator
import com.github.ocraft.s2client.bot.gateway.UnitInPool
import com.github.ocraft.s2client.protocol.data.Abilities
import com.github.ocraft.s2client.protocol.data.Units
import com.github.ocraft.s2client.protocol.game.BattlenetMap
import com.github.ocraft.s2client.protocol.game.Difficulty
import com.github.ocraft.s2client.protocol.game.Race
import common.myUnits
import scv.mineClosestMineralFieldWith
import scv.trainMarineWith
import scv.trainScvWith

class Bot : S2Agent() {

    override fun onGameStart() {

    }

    override fun onUnitIdle(unit: UnitInPool) {
        when (unit.unit().type) {
            Units.TERRAN_COMMAND_CENTER -> trainScvWith(unit)
            Units.TERRAN_SCV -> mineClosestMineralFieldWith(unit)
            Units.TERRAN_BARRACKS -> trainMarineWith(unit)
        }
    }

    override fun onStep() {
        if (workersBuilding(BuildingType.SupplyDepot).size < getOptimalWorkersForSupplyBuilding(supply)) {
            buildSupplyDepotWith(mineralMiningScvs.first())
        }

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

    fun getOptimalWorkersForSupplyBuilding(currentSupply: Int): Int {
        return when {
            currentSupply < 40 && foodUntilSupplyBlocked < 3 -> 1
            currentSupply < 100 && foodUntilSupplyBlocked < 6 -> 2
            currentSupply < 170 && foodUntilSupplyBlocked < 12 -> 3
            else -> 0
        }
    }
}

fun main() {
    val coordinator = S2Coordinator.setup()
        .loadSettings(arrayOf())
        .setWindowSize(1920, 1080)
        .setParticipants(
            S2Coordinator.createParticipant(Race.TERRAN, Bot()),
            S2Coordinator.createComputer(Race.ZERG, Difficulty.HARDER)
        )
        .launchStarcraft()
        .startGame(BattlenetMap.of("Submarine LE"))

    var gameActive = true
    while (gameActive) {
        gameActive = coordinator.update()
    }

    coordinator.quit()
}

