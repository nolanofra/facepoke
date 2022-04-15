package com.nolanofra.routes

import com.nolanofra.service.{ PokemonService, PokemonTranslationService }
import org.http4s.server.Router
import cats.implicits._

class FacePokeHttpRoutes private (
  private val pokemonService: PokemonService,
  translationService: PokemonTranslationService
) {

  private val healthCheckRoute = HealthCheckRoute.healthCheck
  private val pokemonRoute = PokemonInfoRoute(pokemonService, translationService)

  val facePokeRoutes = Router(
    "/" -> healthCheckRoute,
    "/" -> (pokemonRoute.basicInfoPokemon <+> pokemonRoute.translatePokemon)
  ).orNotFound

}

object FacePokeHttpRoutes {

  def apply(pokemonService: PokemonService, translationService: PokemonTranslationService) =
    new FacePokeHttpRoutes(pokemonService, translationService)
}
