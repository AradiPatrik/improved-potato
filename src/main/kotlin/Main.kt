import building.*
import com.github.ocraft.s2client.bot.S2Agent
import com.github.ocraft.s2client.bot.S2Coordinator
import com.github.ocraft.s2client.bot.gateway.UnitInPool
import com.github.ocraft.s2client.protocol.data.Abilities
import com.github.ocraft.s2client.protocol.data.Units
import com.github.ocraft.s2client.protocol.game.BattlenetMap
import com.github.ocraft.s2client.protocol.game.Difficulty
import com.github.ocraft.s2client.protocol.game.Race
import map.S2Map

class Bot : S2Agent() {
    override fun onGameStart() {
        S2Map(observation())
    }

    override fun onUnitIdle(unitInPool: UnitInPool) {
        when (unitInPool.unit().type) {
            Units.TERRAN_COMMAND_CENTER -> actions().unitCommand(unitInPool.unit(), Abilities.TRAIN_SCV, false)
        }
    }

    override fun onStep() {
        if (foodUntilSupplyBlocked < 3 && workersBuilding(BuildingType.SupplyDepot).isEmpty()) {
            buildSupplyDepotWith(myScvs.first())
        }
    }
}

fun main() {
    val coordinator = S2Coordinator.setup()
        .loadSettings(arrayOf())
        .setWindowSize(1920, 1080)
        .setParticipants(
            S2Coordinator.createParticipant(Race.TERRAN, Bot()),
            S2Coordinator.createComputer(Race.ZERG, Difficulty.VERY_EASY)
        )
        .launchStarcraft()
        .startGame(BattlenetMap.of("Submarine LE"))

    var gameActive = true
    while (gameActive) {
        gameActive = coordinator.update()
    }

    coordinator.quit()
}

