package click.recraft.bungee

import org.junit.jupiter.api.Test

internal class ContainerCreatorTest {
    @Test
    fun containerTest() {
//        ContainerCreator.stopAllContainers()
        ContainerCreator.create("test")
    }
    @Test
    fun stopContainer() {
        ContainerCreator.stopAllContainers()
    }
}