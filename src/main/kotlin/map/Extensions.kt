package map

import com.github.ocraft.s2client.bot.gateway.ObservationInterface
import com.github.ocraft.s2client.protocol.data.Units
import common.neutralUnits

private val mineralFieldTypes = listOf(
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

val ObservationInterface.mineralFields
    get() = neutralUnits.filter { mineralFieldTypes.contains(it.unit().type) }
