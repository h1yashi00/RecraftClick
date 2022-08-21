package click.recraft.zombiehero.gun.base

data class GunStats(
    val gun: GunBase,
    val currentArmo: Int,
    val maxArmo:     Int,
    val reloading:   Boolean = false,
    val twoType:     Boolean   = false,
    val subCurrentArmo: Int = 0,
    val subMaxArmo:  Int = 0
) {
}