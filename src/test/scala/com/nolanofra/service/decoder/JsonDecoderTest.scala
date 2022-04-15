package com.nolanofra.service.decoder

import cats.implicits._
import com.nolanofra.service.decoder.PokemonJsonDecoder._
import com.nolanofra.service.model.PokemonEndpointResponse._
import io.circe.literal._
import org.scalatest.funsuite.AnyFunSuite

class JsonDecoderTest extends AnyFunSuite {

  test("Decode pokemon information") {

    val pokemonName = "pokemonName"
    val isLegendary = false
    val habitatName = "habitatName"

    val input =
      json"""{
             "name": $pokemonName, 
             "is_legendary": $isLegendary,
             "habitat": {
               "name": $habitatName
              },
              "flavor_text_entries": [
                {
                    "flavor_text": "some flavor text",
                    "language": {
                        "name": "en"
                    }
                },
                {
                    "flavor_text": "some japanese flavor text",
                    "language": {
                        "name": "ja"
                    }
                },
                {
                    "flavor_text": "one more italian flavor text",
                    "language": {
                        "name": "it"
                    }
                }
              ]
            }"""

    val actual = pokemonDetailDecoder.decodeJson(input)

    val expectedResult = Pokemon(
      pokemonName,
      Habitat(habitatName),
      isLegendary,
      List(
        FlavorText("some flavor text", Language("en")),
        FlavorText("some japanese flavor text", Language("ja")),
        FlavorText("one more italian flavor text", Language("it"))
      )
    ).asRight

    assert(actual == expectedResult)
  }

  test("Error while decoding pokemon name") {

    val input =
      json"""{
             "wrongName": "pokemonName", 
             "is_legendary": true,
             "habitat": {
               "name": "habitatName",
               "url": "anyUrl"
              },
              "flavor_text_entries": [
                {
                    "flavor_text": "some flavor text",
                    "language": {
                        "name": "en",
                        "url": "anyUrl"
                    }
                }
              ]
            }"""

    val actual = pokemonDetailDecoder.decodeJson(input)

    assert(actual.isLeft)
  }

  test("Error while decoding is legendary pokemon") {

    val input =
      json"""{
             "name": "pokemonName", 
             "wrong_is_legendary": true,
             "habitat": {
               "name": "habitatName",
               "url": "anyUrl"
              },
              "flavor_text_entries": [
                {
                    "flavor_text": "some flavor text",
                    "language": {
                        "name": "en",
                        "url": "anyUrl"
                    }
                }
              ]
            }"""

    val actual = pokemonDetailDecoder.decodeJson(input)

    assert(actual.isLeft)
  }

  test("Error while decoding habitat pokemon") {

    val input =
      json"""{
             "name": "pokemonName", 
             "is_legendary": true,
             "wrongHabitat": {
               "name": "habitatName",
               "url": "anyUrl"
              },
              "flavor_text_entries": [
                {
                    "flavor_text": "some flavor text",
                    "language": {
                        "name": "en",
                        "url": "anyUrl"
                    }
                }
              ]
            }"""

    val actual = pokemonDetailDecoder.decodeJson(input)

    assert(actual.isLeft)
  }

  test("Error while decoding flavor text entries pokemon") {

    val input =
      json"""{
             "name": "pokemonName", 
             "is_legendary": true,
             "habitat": {
               "name": "habitatName",
               "url": "anyUrl"
              },
              "wrong_flavor_text_entries": [
                {
                    "flavor_text": "some flavor text",
                    "language": {
                        "name": "en",
                        "url": "anyUrl"
                    }
                }
              ]
            }"""

    val actual = pokemonDetailDecoder.decodeJson(input)

    assert(actual.isLeft)
  }

  test("Decode flavor text entries") {

    val flavorText = "some flavor text"
    val language = "en"

    val input =
      json"""{                                       
                "flavor_text": $flavorText,
                "language": {
                    "name": $language
                }                
            }"""

    val actual = flavorTextDecoder.decodeJson(input)

    val expectedResult = FlavorText(flavorText, Language(language)).asRight

    assert(actual == expectedResult)
  }

  test("Error while decoding flavor text entries") {

    val input =
      json"""{                                       
                "wrong_flavor_text": "flavor_text",
                "language": {
                    "name": "en"
                }                
            }"""

    val actual = flavorTextDecoder.decodeJson(input)

    assert(actual.isLeft)
  }

  test("Decode flavor text language") {

    val language = "en"

    val input =
      json"""{                                                                     
                "name": $language                                
            }"""

    val actual = languageDecoder.decodeJson(input)

    val expectedResult = Language(language).asRight

    assert(actual == expectedResult)
  }

  test("Error while decoding flavor text language") {

    val language = "en"

    val input =
      json"""{                                                                     
                "wrongName": $language                                
            }"""

    val actual = languageDecoder.decodeJson(input)

    assert(actual.isLeft)
  }

  test("Decode habitat name") {

    val habitat = "habitat"

    val input =
      json"""{                                                                     
                "name": $habitat                                
            }"""

    val actual = habitatDecoder.decodeJson(input)

    val expectedResult = Habitat(habitat).asRight

    assert(actual == expectedResult)
  }

  test("Error while decoding habitat name") {

    val input =
      json"""{                                                                     
                "wrongName": "habitat"                                
            }"""

    val actual = habitatDecoder.decodeJson(input)

    assert(actual.isLeft)
  }

}
