package facepoke

import cats.effect.{ IO, Resource }
import com.dimafeng.testcontainers.{ GenericContainer, MockServerContainer }
import org.http4s.client.JavaNetClientBuilder
import org.mockserver.client.MockServerClient
import org.slf4j.LoggerFactory
import org.testcontainers.containers.Network
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait

object Containers {

  val network = Network.newNetwork
  val networkAlias = "mockserver"
  val exposedPort = 1080

  lazy val mockServer = MockServerContainer("5.13.1").configure { c =>
    c.withNetwork(network)
    c.withNetworkAliases(networkAlias)
    c.withExposedPorts(exposedPort)
    c.waitingFor(Wait.forLogMessage(".*started on port:.*", 1))
    c.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("MockServer")))
    ()
  }

  lazy val apiContainer = GenericContainer(
    dockerImage = "nolanofra/test",
    exposedPorts = Seq(5000),
    waitStrategy = Wait.forLogMessage(".*started at http://0.0.0.0:5000/.*", 1)
  ).configure { provider =>
    provider.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("ApiServer")))
    provider.withNetwork(network)
    ()
  }

  private lazy val mockServerClient =
    Resource.make(IO(new MockServerClient(mockServer.container.getHost, mockServer.container.getServerPort))) {
      client =>
        IO {
          client.reset() //I have to do this because the client shutdown the server when the resource is released
          ()
        }
    }

  def clients =
    for {
      httpClient <- JavaNetClientBuilder[IO].resource
      mockServerClient <- mockServerClient
    } yield (httpClient, mockServerClient)

}
