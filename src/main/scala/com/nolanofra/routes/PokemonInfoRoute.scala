package com.nolanofra.routes

import cats.effect.IO
import com.nolanofra.api.error.Errors.{ PokeApiBadRequest, PokemonNotFound, TooManyRequest }
import com.nolanofra.service.{ PokemonService, PokemonTranslationService }
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class PokemonInfoRoute private (
  private val pokemonService: PokemonService,
  translationService: PokemonTranslationService
) {

  def basicInfoPokemon: HttpRoutes[IO] = {
    val dsl = Http4sDsl[IO]
    import dsl._

    HttpRoutes
      .of[IO] { case GET -> Root / "pokemon" / pokemonName =>
        pokemonService
          .pokemonInformation(pokemonName)
          .flatMap {
            case pokemon => Ok(pokemon.asJson)
            case _ => NotFound("Pokemon not found")
          }
          .handleErrorWith {
            case _: PokemonNotFound => NotFound("Pokemon not found")
            case _: TooManyRequest => TooManyRequests("Too many request")
            case _: PokeApiBadRequest => BadRequest("Bad request")
            case t => InternalServerError(t.getMessage)
          }
      }
  }

  def translatePokemon: HttpRoutes[IO] = {
    val dsl = Http4sDsl[IO]
    import dsl._

    HttpRoutes
      .of[IO] { case GET -> Root / "pokemon" / "translated" / pokemonName =>
        translationService
          .pokemonInformationTranslated(pokemonName)
          .flatMap {
            case pokemon => Ok(pokemon.asJson)
            case _ => NotFound("Pokemon not found")
          }
          .handleErrorWith {
            case _: PokemonNotFound => NotFound("Pokemon not found")
            case _: TooManyRequest => TooManyRequests("Too many request")
            case _: PokeApiBadRequest => BadRequest("Bad request")
            case t => InternalServerError(t.getMessage)
          }
      }
  }
}

object PokemonInfoRoute {

  def apply(pokemonService: PokemonService, translationService: PokemonTranslationService) =
    new PokemonInfoRoute(pokemonService, translationService)
}
