package com.nolanofra.api

import cats.effect.IO
import com.nolanofra.api.error.Errors._
import io.circe.Decoder
import org.http4s._
import org.http4s.client.Client

trait PokeApi {
  def getPokemonSpecies[O: Decoder](name: String): IO[O]
}

private class PokeApiImpl(httpClient: Client[IO], baseUrl: String) extends PokeApi {

  override def getPokemonSpecies[O: Decoder](name: String): IO[O] = {
    val pokemonEndpoint = Uri.unsafeFromString(baseUrl + s"pokemon-species/$name")
    httpClient.get(pokemonEndpoint)((response: Response[IO]) => handleHttpErrors[O](response))
  }
}

object PokeApiImpl {
  def apply(httpClient: Client[IO], baseUrl: String): PokeApi = new PokeApiImpl(httpClient, baseUrl)
}
