package click.recraft.zombiehero.gun.base

class Tick private constructor (
    val tick: Int
)
{
    companion object {
        // double だが､第一小数点数までしか入力で対応していないので､それ以上だと強制的に､丸める
        fun sec(sec: Double): Tick {
            return Tick((20 * sec).toInt())
        }
    }
    fun getMilliseconds(): Int {
        return tick * 50
    }
}