package facepoke

import com.dimafeng.testcontainers.{ Container, ForAllTestContainer, MultipleContainers }
import com.nolanofra.api.model.PokemonEndpointResponse.{ FlavorText, Habitat, Language, Pokemon }
import facepoke.Containers.{ client, mockServerClient }
import io.circe.literal.JsonStringContext
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import sttp.client3.quick._
import sttp.model.StatusCode

class FacePokeIT extends AnyFunSuite with ForAllTestContainer with BeforeAndAfterEach {

  override def container: Container = MultipleContainers(
    Containers.mockServerContainer,
    Containers.apiContainer
  )

  override def beforeEach() {
    mockServerClient.reset()
  }

  test("test health check route") {
    val request = basicRequest.get(
      uri"http://${Containers.apiContainer.container.getHost}:${Containers.apiContainer.mappedPort(5000)}/health"
    )

    val response = request.send(client)
    assert(response.code == StatusCode.Ok)
  }

  test("Retrieve basic pokemon information") {

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

  test("Unknown pokemonName: 404, not found") {

    val notExistingPokemonName = "notExistingPokemon"

    mockServerClient
      .when(request().withPath("/pokemon-species/" + notExistingPokemonName))
      .respond(response().withStatusCode(404))

    val pokemonRequest = basicRequest
      .get(
        uri"http://${Containers.apiContainer.container.getHost}:${Containers.apiContainer
          .mappedPort(5000)}/pokemon/${notExistingPokemonName}"
      )

    val pokemonResponse = pokemonRequest.send(client)
    assert(pokemonResponse.code == StatusCode.NotFound)
  }

  test("payload not valid, 500 internal server error") {

    val pokemonName = "pokemonName"

    mockServerClient
      .when(request().withPath("/pokemon-species/" + pokemonName))
      .respond(response().withBody("this is an invalid json"))

    val pokemonRequest = basicRequest
      .get(
        uri"http://${Containers.apiContainer.container.getHost}:${Containers.apiContainer
          .mappedPort(5000)}/pokemon/${pokemonName}"
      )

    val pokemonResponse = pokemonRequest.send(client)
    assert(pokemonResponse.code == StatusCode.InternalServerError)
  }

  test("Service not available") {

    val pokemonName = "pokemonName"

    mockServerClient
      .when(request().withPath("/pokemon-species/" + pokemonName))
      .respond(response().withStatusCode(500))

    val pokemonRequest = basicRequest
      .get(
        uri"http://${Containers.apiContainer.container.getHost}:${Containers.apiContainer
          .mappedPort(5000)}/pokemon/${pokemonName}"
      )

    val pokemonResponse = pokemonRequest.send(client)
    assert(pokemonResponse.code == StatusCode.ServiceUnavailable)
  }

  test("If Pokemon's habitat is cave then apply the Yoda translation") {

    val expectedPokemon =
      Pokemon(
        "pokemonName",
        Habitat("cave"),
        isLegendary = false,
        List(FlavorText("pokemonDescription", Language("en")))
      )

    val expectedTranslation = "YODAExpectedTranslation"

    mockServerClient
      .when(request().withPath("/pokemon-species/" + expectedPokemon.name))
      .respond(response().withBody(Expectations.pokemonSpeciesResponse(expectedPokemon)))

    mockServerClient
      .when(request().withPath("/translate/yoda.json"))
      .respond(response().withBody(Expectations.translationResponse(expectedTranslation)))

    val pokemonRequest = basicRequest
      .get(
        uri"http://${Containers.apiContainer.container.getHost}:${Containers.apiContainer
          .mappedPort(5000)}/pokemon/translated/${expectedPokemon.name}"
      )

    val pokemonResponse = pokemonRequest.send(client)
    val expectedBody =
      json"""{
        "name": ${expectedPokemon.name},
        "description":${expectedTranslation},
        "habitat":${expectedPokemon.habitat.name},
        "isLegendary": ${expectedPokemon.isLegendary}
        }""".noSpaces

    pokemonResponse.body.map(r => assert(r == expectedBody))
  }

  test("If Pokemon is legendary then apply the Yoda translation") {

    val expectedPokemon =
      Pokemon(
        "pokemonName",
        Habitat("habitat"),
        isLegendary = true,
        List(FlavorText("pokemonDescription", Language("en")))
      )

    val expectedTranslation = "YODAExpectedTranslation"

    mockServerClient
      .when(request().withPath("/pokemon-species/" + expectedPokemon.name))
      .respond(response().withBody(Expectations.pokemonSpeciesResponse(expectedPokemon)))

    mockServerClient
      .when(request().withPath("/translate/yoda.json"))
      .respond(response().withBody(Expectations.translationResponse(expectedTranslation)))

    val pokemonRequest = basicRequest
      .get(
        uri"http://${Containers.apiContainer.container.getHost}:${Containers.apiContainer
          .mappedPort(5000)}/pokemon/translated/${expectedPokemon.name}"
      )

    val pokemonResponse = pokemonRequest.send(client)
    val expectedBody =
      json"""{
        "name": ${expectedPokemon.name},
        "description":${expectedTranslation},
        "habitat":${expectedPokemon.habitat.name},
        "isLegendary": ${expectedPokemon.isLegendary}
        }""".noSpaces

    pokemonResponse.body.map(r => assert(r == expectedBody))
  }

  test("For all other Pokemon apply the Shakespeare translation") {

    val expectedPokemon =
      Pokemon(
        "pokemonName",
        Habitat("habitat"),
        isLegendary = false,
        List(FlavorText("pokemonDescription", Language("en")))
      )

    val expectedTranslation = "SHAKESPEAREExpectedTranslation"

    mockServerClient
      .when(request().withPath("/pokemon-species/" + expectedPokemon.name))
      .respond(response().withBody(Expectations.pokemonSpeciesResponse(expectedPokemon)))

    mockServerClient
      .when(request().withPath("/translate/shakespeare.json"))
      .respond(response().withBody(Expectations.translationResponse(expectedTranslation)))

    val pokemonRequest = basicRequest
      .get(
        uri"http://${Containers.apiContainer.container.getHost}:${Containers.apiContainer
          .mappedPort(5000)}/pokemon/translated/${expectedPokemon.name}"
      )

    val pokemonResponse = pokemonRequest.send(client)
    val expectedBody =
      json"""{
        "name": ${expectedPokemon.name},
        "description":${expectedTranslation},
        "habitat":${expectedPokemon.habitat.name},
        "isLegendary": ${expectedPokemon.isLegendary}
        }""".noSpaces

    pokemonResponse.body.map(r => assert(r == expectedBody))
  }

  test("Use the standard description if translate endpoint returns too many request") {

    val expectedPokemon =
      Pokemon(
        "pokemonName",
        Habitat("habitat"),
        isLegendary = false,
        List(FlavorText("standardDescription", Language("en")))
      )

    val expectedTranslation = "standardDescription"

    mockServerClient
      .when(request().withPath("/pokemon-species/" + expectedPokemon.name))
      .respond(response().withBody(Expectations.pokemonSpeciesResponse(expectedPokemon)))

    mockServerClient
      .when(request().withPath("/translate/shakespeare.json"))
      .respond(response().withStatusCode(429))

    val pokemonRequest = basicRequest
      .get(
        uri"http://${Containers.apiContainer.container.getHost}:${Containers.apiContainer
          .mappedPort(5000)}/pokemon/translated/${expectedPokemon.name}"
      )

    val pokemonResponse = pokemonRequest.send(client)
    val expectedBody =
      json"""{
        "name": ${expectedPokemon.name},
        "description":${expectedTranslation},
        "habitat":${expectedPokemon.habitat.name},
        "isLegendary": ${expectedPokemon.isLegendary}
        }""".noSpaces

    pokemonResponse.body.map(r => assert(r == expectedBody))
  }
}
