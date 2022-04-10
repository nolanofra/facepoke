package com.nolanofra.model.decoder

import com.nolanofra.model.PokemonEndpointResponse.{ FlavorText, FlavorTextEntries, Language, Pokemon, Species }
import io.circe.Decoder
import org.http4s.Uri

object JsonDecoder {

  implicit val uriDecoder = Decoder.decodeString.map(Uri.unsafeFromString)
  implicit val speciesDecoder = Decoder.forProduct2("name", "url")(Species)
  implicit val pokemonDecoder = Decoder.forProduct1("species")(Pokemon)
  implicit val languageDecoder = Decoder.forProduct1("name")(Language)
  implicit val descriptionDecoder = Decoder.forProduct2("flavor_text", "language")(FlavorText)
  implicit val speciesDetailDecoder = Decoder.forProduct1("flavor_text_entries")(FlavorTextEntries)
}
