package common

import com.github.ocraft.s2client.protocol.spatial.Point2d

operator fun Point2d.plus(other: Point2d) = this.add(other)

operator fun Point2d.times(other: Float) = this.mul(other)

fun Point2d.squareDistance(other: Point2d): Float = (x - other.x) * (y - other.y)