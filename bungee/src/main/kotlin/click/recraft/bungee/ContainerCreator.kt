package click.recraft.bungee

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.*
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient

object ContainerCreator {
    private val custom = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost(System.getenv("DOCKER_HOST")).build()

    private val httpClient = ApacheDockerHttpClient
        .Builder()
        .dockerHost(custom.dockerHost)
        .build()

    private val dockerClient: DockerClient = DockerClientImpl.getInstance(custom, httpClient)

    fun stopAllContainers() {
        dockerClient.listContainersCmd().exec().forEach {
            dockerClient.stopContainerCmd(it.id).exec()
            dockerClient.removeContainerCmd(it.id).exec()
        }
    }

    fun create(containerID: String) {
        val config = HostConfig
            .newHostConfig()
            .withPublishAllPorts(true)
            .withAutoRemove(true)
            .withMemory(5368709120)
        val createContainer = dockerClient
            .createContainerCmd("zombiehero")
            .withEnv("SERVER_NAME=$containerID")
            .withName(containerID)
            .withHostConfig(config)
        createContainer.exec()
        dockerClient.connectToNetworkCmd()
            .withContainerId(containerID)
            .withNetworkId("recraftclick_recraft")
            .exec()
        dockerClient.startContainerCmd(containerID).exec()
    }

    fun delete(containerID : String){
        dockerClient.stopContainerCmd(containerID).exec()
//        dockerClient.removeContainerCmd(containerID).exec()
    }
}