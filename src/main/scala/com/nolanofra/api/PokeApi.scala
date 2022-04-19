package com.nolanofra.api

import cats.effect.IO
import com.nolanofra.api.error.Errors._
import com.nolanofra.api.model.PokemonEndpointResponse.Pokemon
import org.http4s._
import org.http4s.client.Client
import com.nolanofra.api.decoder.PokemonJsonDecoder._

trait PokeApi {
  def getPokemonSpecies(name: String): IO[Pokemon]
}

class PokeApiImpl private (private val httpClient: Client[IO], baseUrl: String) extends PokeApi {

  override def getPokemonSpecies(name: String): IO[Pokemon] = {
    val pokemonEndpoint = Uri.unsafeFromString(baseUrl + s"pokemon-species/$name")
    httpClient.get(pokemonEndpoint)((response: Response[IO]) => handleHttpErrors[Pokemon](response))
  }
}

object PokeApiImpl {
  def apply(httpClient: Client[IO], baseUrl: String): PokeApi = new PokeApiImpl(httpClient, baseUrl)
}
