package facepoke

import com.nolanofra.api.model.PokemonEndpointResponse.Pokemon
import io.circe.literal.JsonStringContext

object Expectations {

  def pokemonSpeciesResponse(pokemon: Pokemon) = json""" {
    "name": ${pokemon.name},
    "is_legendary": ${pokemon.isLegendary},
    "habitat": {
      "name": ${pokemon.habitat.name}
    },
    "flavor_text_entries":  
    ${pokemon.flavorTextEntries.map(flavor => json""" {
      "flavor_text": ${flavor.text},
        "language": {
        "name": ${flavor.language.lang}
        } 
      }
      """)}
     
  }""".noSpaces
}
