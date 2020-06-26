package building

import com.github.ocraft.s2client.protocol.data.Abilities
import com.github.ocraft.s2client.protocol.data.Ability
import com.github.ocraft.s2client.protocol.data.Units

enum class BuildingType(val ability: Abilities) {
    SupplyDepot(Abilities.BUILD_SUPPLY_DEPOT),
    Barack(Abilities.BUILD_BARRACKS);

    companion object {
        fun Ability.toBuildingType() = values().firstOrNull {
            it.ability.abilityId == abilityId
        }
    }
}
