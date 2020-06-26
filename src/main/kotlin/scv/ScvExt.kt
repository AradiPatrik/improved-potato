package training

import com.github.ocraft.s2client.bot.S2Agent
import com.github.ocraft.s2client.bot.gateway.UnitInPool
import com.github.ocraft.s2client.protocol.data.Abilities
import com.github.ocraft.s2client.protocol.data.Units

fun S2Agent.trainScvWith(commandCenter: UnitInPool) {
    require(commandCenter.unit().type == Units.TERRAN_COMMAND_CENTER) {
        "Command center expected, got: ${commandCenter.unit().type}"
    }
    actions().unitCommand(commandCenter.unit(), Abilities.TRAIN_SCV, false)
}
