package click.recraft.zombiehero.gun.api

class Accuracy(
    _value: Int,
    _scopedValue: Int
) {
    private fun convert(num: Int): Double {
        return if (num == 0) {
            0.0
        }
        else {
            num.toDouble() / 1000
        }
    }
    val value: Double = if (_value in 0..300) {
        convert(_value)
    }
    else {
        throw IllegalArgumentException("_value must be 0 <= _value >= 300")
    }
    val scopedValue: Double = if (_scopedValue in 0 .. 300) {
        convert(_scopedValue)
    }
    else {
        throw IllegalArgumentException("_value must be 0 <= _value >= 300")
    }
}