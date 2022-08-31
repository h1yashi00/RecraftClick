package click.recraft.zombiehero.gun.api

class Accuracy(
    _value: Int
) {
    val value: Double = if (_value in 0..300) {
        if (_value == 0) {
            0.0
        }
        else {
            _value.toDouble() / 1000
        }
    }
    else {
        throw IllegalArgumentException("_value must be 0 <= _value >= 300")
    }
}