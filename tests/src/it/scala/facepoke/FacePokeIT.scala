package facepoke

import cats.effect.IO
import com.dimafeng.testcontainers.{ Container, ForAllTestContainer, MultipleContainers }
import org.http4s.{ Response, Status, Uri }
import org.scalatest.funsuite.AnyFunSuite

class FacePokeIT extends AnyFunSuite with ForAllTestContainer {

  override def container: Container = MultipleContainers(
    Containers.mockServer,
    Containers.apiContainer
  )

  test("Health Check") {
    val actual: IO[Status] = Containers.clients.use { case (httpClient, _) =>
      val healthCheck: Uri = Uri.unsafeFromString(
        s"http://${Containers.apiContainer.container.getHost}:${Containers.apiContainer.mappedPort(5000)}/health"
      )

      for {
        response <- httpClient.get(healthCheck)((response: Response[IO]) => IO(response.status))
      } yield response
    }

    actual.map(status => assert(status == Status.Ok))
  }

}
