package com.nolanofra.routes

import com.nolanofra.service.PokemonService
import org.http4s.server.Router

class FacePokeHttpRoutes private (private val pokemonService: PokemonService) {

  private val healthCheckRoute = HealthCheckRoute.healthCheck
  private val pokemonRoute = PokemonInfoRoute(pokemonService).infoPokemon

  val facePokeRoutes = Router(
    "/" -> healthCheckRoute,
    "/" -> pokemonRoute
  ).orNotFound

}

object FacePokeHttpRoutes {
  def apply(pokemonService: PokemonService) = new FacePokeHttpRoutes(pokemonService)
}
