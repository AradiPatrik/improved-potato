package map

import com.github.ocraft.s2client.bot.gateway.ObservationInterface
import java.io.File
import javax.imageio.ImageIO

class S2Map(observation: ObservationInterface) {
    private val startRaw = observation.gameInfo.startRaw.get()
    val width = startRaw.mapSize.x
    val height = startRaw.mapSize.y
    val myStartLocation = observation.startLocation
    val startLocations = startRaw.startLocations

    init {
        println("placementGrid: ${startRaw.placementGrid.bitsPerPixel}")
        println("terrainHeight: ${startRaw.terrainHeight.bitsPerPixel}")
        println("pathingGrid: ${startRaw.pathingGrid.bitsPerPixel}")
        println("placementGrid: ${startRaw.placementGrid.data.size}")
        println("terrainHeight: ${startRaw.terrainHeight.data.size}")
        println("mapWidth: $width")
        println("mapHeight: $height")
        println("mapwidthheight: ${width * height}")

        observation.gameInfo.startRaw.get().placementGrid.data

        ImageIO.write(startRaw.placementGrid.image, "png", File("placementGrid.png"))
        ImageIO.write(startRaw.terrainHeight.image, "png", File("terrainHeight.png"))
        ImageIO.write(startRaw.pathingGrid.image, "png", File("pathGrid.image"))
    }
}