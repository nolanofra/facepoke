package com.nolanofra.api

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeErrorId
import com.nolanofra.api.error.Errors._
import io.circe.Decoder
import org.http4s._
import org.http4s.circe._
import org.http4s.client.Client

trait PokeApi {
  def getPokemonSpecies[O: Decoder](name: String): IO[O]
}

private class PokeApiImpl(httpClient: Client[IO], baseUrl: String) extends PokeApi {

  override def getPokemonSpecies[O: Decoder](name: String): IO[O] = {
    val pokemonEndpoint = Uri.unsafeFromString(baseUrl + s"pokemon-species/$name")
    httpClient.get(pokemonEndpoint)((response: Response[IO]) => handleHttpErrors[O](response))
  }

  private def handleHttpErrors[A: Decoder](response: Response[IO]) =
    response.status match {
      case Status.Ok => response.asJsonDecode[A]
      case Status.NotFound => PokemonNotFound(response.status.reason).raiseError[IO, A]
      case Status.TooManyRequests => TooManyRequest(response.status.reason).raiseError[IO, A]
      case Status.InternalServerError => InternalServerError(response.status.reason).raiseError[IO, A]
      case Status.BadRequest => PokeApiBadRequest(response.status.reason).raiseError[IO, A]
      case _ => ServiceNotAvailable(response.status.reason).raiseError[IO, A]
    }
}

object PokeApiImpl {
  def apply(httpClient: Client[IO], baseUrl: String): PokeApi = new PokeApiImpl(httpClient, baseUrl)
}
