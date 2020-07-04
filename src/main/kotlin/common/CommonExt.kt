package common

import com.github.ocraft.s2client.bot.S2Agent
import com.github.ocraft.s2client.bot.gateway.ObservationInterface
import com.github.ocraft.s2client.protocol.unit.Alliance

val ObservationInterface.myUnits get() = getUnits(Alliance.SELF)
val ObservationInterface.neutrals get() = getUnits(Alliance.NEUTRAL)
