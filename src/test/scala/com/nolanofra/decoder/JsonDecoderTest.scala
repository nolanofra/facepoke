package com.nolanofra.decoder

import cats.implicits._
import com.nolanofra.model.PokemonEndpointResponse._
import com.nolanofra.model.decoder.JsonDecoder._
import io.circe.literal._
import org.http4s.Uri
import org.scalatest.funsuite.AnyFunSuite

class JsonDecoderTest extends AnyFunSuite {

  test("decode pokemon species") {

    val pokemonName = "ditto"
    val speciesUrl = "https://pokeapi.co/api/v2/pokemon-species/132/"

    val input =
      json"""{
             "species": {
                "name": $pokemonName,
                "url": $speciesUrl
              }
            }"""

    val expectedResult =
      Pokemon(Species("ditto", Uri.unsafeFromString("https://pokeapi.co/api/v2/pokemon-species/132/"))).asRight
    val actual = pokemonDecoder.decodeJson(input)

    assert(actual == expectedResult)
  }

  test("error while decoding pokemon species") {

    val input =
      json"""{
             "species": {
                
              }
            }"""

    val actual = pokemonDecoder.decodeJson(input)

    assert(actual.isLeft)
  }

  test("decode flavor text entries") {

    val input =
      json"""{
              "flavor_text_entries": [
                {
                    "flavor_text": "some flavor text",
                    "language": {
                        "name": "en",
                        "url": "anyUrl"
                    }
                },
                {
                    "flavor_text": "some japanese flavor text",
                    "language": {
                        "name": "ja",
                        "url": "anyUrl"
                    }
                },
                {
                    "flavor_text": "one more italian flavor text",
                    "language": {
                        "name": "it",
                        "url": "anyUrl"
                    }
                }
              ]
            }"""

    val actual = speciesDetailDecoder.decodeJson(input)

    val expectedResult = PokemonSpecies(
      List(
        FlavorText("some flavor text", Language("en")),
        FlavorText("some japanese flavor text", Language("ja")),
        FlavorText("one more italian flavor text", Language("it"))
      )
    ).asRight

    assert(actual == expectedResult)
  }

  test("error while decoding flavor text entries") {

    val input =
      json"""{
              "flavor_text_entries": [
                {
                "wrong_flavor_text": "some flavor text"                    
                }
              ]
            }"""

    val actual = speciesDetailDecoder.decodeJson(input)

    assert(actual.isLeft)
  }

}
