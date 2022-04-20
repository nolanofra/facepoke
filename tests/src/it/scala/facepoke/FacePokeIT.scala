package facepoke

import com.dimafeng.testcontainers.{ Container, ForAllTestContainer, MultipleContainers }
import com.nolanofra.api.model.PokemonEndpointResponse.{ FlavorText, Habitat, Language, Pokemon }
import facepoke.Containers.{ client, mockServerClient }
import io.circe.literal.JsonStringContext
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.scalatest.funsuite.AnyFunSuite
import sttp.client3.quick._
import sttp.model.StatusCode

class FacePokeIT extends AnyFunSuite with ForAllTestContainer {

  override def container: Container = MultipleContainers(
    Containers.mockServerContainer,
    Containers.apiContainer
  )

  test("test health check route") {
    val request = basicRequest.get(
      uri"http://${Containers.apiContainer.container.getHost}:${Containers.apiContainer.mappedPort(5000)}/health"
    )

    val response = request.send(client)
    assert(response.code == StatusCode.Ok)
  }

  test("Retrieve basic info") {

    val expectedPokemon =
      Pokemon(
        "pokemonName",
        Habitat("pokemonHabitat"),
        isLegendary = false,
        List(FlavorText("pokemonDescription", Language("en")))
      )

    mockServerClient
      .when(request().withPath("/pokemon-species/pokemonName"))
      .respond(response().withBody(Expectations.pokemonSpeciesResponse(expectedPokemon)))

    val pokemonRequest = basicRequest
      .get(
        uri"http://${Containers.apiContainer.container.getHost}:${Containers.apiContainer
          .mappedPort(5000)}/pokemon/${expectedPokemon.name}"
      )

    val pokemonResponse = pokemonRequest.send(client)

    val expectedBody =
      json"""{
        "name": ${expectedPokemon.name},
        "description":${expectedPokemon.descriptionFor("en")},
        "habitat":${expectedPokemon.habitat.name},
        "isLegendary": ${expectedPokemon.isLegendary}
        }""".noSpaces

    pokemonResponse.body.map(r => assert(r == expectedBody))
  }
}
