package map

import com.github.ocraft.s2client.bot.S2Agent
import com.github.ocraft.s2client.bot.gateway.ObservationInterface
import com.github.ocraft.s2client.protocol.data.Units
import common.neutrals

private val mineralFieldTypes = setOf(
    Units.NEUTRAL_BATTLE_STATION_MINERAL_FIELD,
    Units.NEUTRAL_BATTLE_STATION_MINERAL_FIELD750,
    Units.NEUTRAL_LAB_MINERAL_FIELD,
    Units.NEUTRAL_LAB_MINERAL_FIELD750,
    Units.NEUTRAL_MINERAL_FIELD,
    Units.NEUTRAL_MINERAL_FIELD450,
    Units.NEUTRAL_MINERAL_FIELD750,
    Units.NEUTRAL_PURIFIER_MINERAL_FIELD,
    Units.NEUTRAL_PURIFIER_MINERAL_FIELD750,
    Units.NEUTRAL_PURIFIER_RICH_MINERAL_FIELD,
    Units.NEUTRAL_PURIFIER_RICH_MINERAL_FIELD750,
    Units.NEUTRAL_RICH_MINERAL_FIELD,
    Units.NEUTRAL_RICH_MINERAL_FIELD750,
    Units.NEUTRAL_MINERAL_FIELD_OPAQUE,
    Units.NEUTRAL_MINERAL_FIELD_OPAQUE900
)

val S2Agent.mineralFields get() = observation().mineralFields
val ObservationInterface.mineralFields
    get() = neutrals.filter { mineralFieldTypes.contains(it.unit().type) }
