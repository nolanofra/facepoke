package facepoke

import com.dimafeng.testcontainers.{ GenericContainer, MockServerContainer }
import org.mockserver.client.MockServerClient
import org.slf4j.LoggerFactory
import org.testcontainers.containers.Network
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import sttp.client3.HttpURLConnectionBackend

object Containers {

  val network = Network.newNetwork()
  val networkAlias = "mockserver"
  val exposedPort = 1080

  val mockServerContainer = MockServerContainer("5.13.1").configure { c =>
    c.withNetwork(network)
    c.withNetworkAliases(networkAlias)
    c.withExposedPorts(exposedPort)
    c.waitingFor(Wait.forLogMessage(".*started on port:.*", 1))
    c.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("MockServer")))
    ()
  }

  val apiContainer = GenericContainer(
    dockerImage = "nolanofra/tests",
    exposedPorts = Seq(5000),
    waitStrategy = Wait.forLogMessage(".*started at http://0.0.0.0:5000/.*", 1),
    env = Map(
      "POKE_API_BASE_URL" -> s"http://${networkAlias}:${exposedPort}/",
      "TRANSLATION_API_BASE_URL" -> s"http://${networkAlias}:${exposedPort}/"
    )
  ).configure { provider =>
    provider.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("ApiServer")))
    provider.withNetwork(network)
    ()
  }

  lazy val mockServerClient =
    new MockServerClient(mockServerContainer.container.getHost, mockServerContainer.container.getServerPort)

  val client = HttpURLConnectionBackend()

}
