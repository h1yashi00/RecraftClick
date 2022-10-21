package click.recraft.share.protocol

import org.junit.jupiter.api.Test

internal class TextureItemTest {
    @Test
    fun a() {
        val gun = TextureItem.getMain()
        check(gun.isNotEmpty())
    }
    @Test
    fun b() {
        val melees= TextureItem.getMelee()
        check(melees.isNotEmpty())
        println(melees)
    }
    @Test
    fun c() {
        val skills = TextureItem.getSkill()
        check(skills.isNotEmpty())
        println(skills)
    }
    @Test
    fun d() {
        TextureItem.values().forEach {
            println(it.displayName)
        }
    }
}