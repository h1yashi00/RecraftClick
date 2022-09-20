package click.recraft.zombiehero.player

interface WalkSpeed {
    // ベースが200で0に近づくにつれて､足が遅くなる｡
    // walkSpeedは加算方式なので-20だった場合180となる
    val walkSpeed: Int
}