package common

import com.github.ocraft.s2client.bot.gateway.ObservationInterface
import com.github.ocraft.s2client.protocol.unit.Alliance

val ObservationInterface.myUnits get() = getUnits(Alliance.SELF)
