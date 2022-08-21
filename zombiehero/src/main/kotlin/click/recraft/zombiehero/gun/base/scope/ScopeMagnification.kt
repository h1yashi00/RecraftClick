package click.recraft.zombiehero.gun.base.scope

import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

enum class ScopeMagnification(val potionEffect: PotionEffect) {
    TWO     (PotionEffect(PotionEffectType.SLOW, 20*5, 1)),
    THREE   (PotionEffect(PotionEffectType.SLOW, 20*5, 2)),
    SIX     (PotionEffect(PotionEffectType.SLOW, 20*5, 4)),
    EIGHT   (PotionEffect(PotionEffectType.SLOW, 20*5, 8)),
    SIXTEEN (PotionEffect(PotionEffectType.SLOW, 20*5, 16));
}