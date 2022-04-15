package com.nolanofra.service.decoder

import com.nolanofra.service.model.PokemonEndpointResponse.{ FlavorText, Language, Pokemon, PokemonSpecies, Species }
import io.circe.Decoder
import org.http4s.Uri

object PokemonJsonDecoder {

  implicit val uriDecoder = Decoder.decodeString.map(Uri.unsafeFromString)
  implicit val speciesDecoder = Decoder.forProduct2("name", "url")(Species)
  implicit val pokemonDecoder = Decoder.forProduct1("species")(Pokemon)
  implicit val languageDecoder = Decoder.forProduct1("name")(Language)
  implicit val descriptionDecoder = Decoder.forProduct2("flavor_text", "language")(FlavorText)
  implicit val speciesDetailDecoder = Decoder.forProduct1("flavor_text_entries")(PokemonSpecies)
}
