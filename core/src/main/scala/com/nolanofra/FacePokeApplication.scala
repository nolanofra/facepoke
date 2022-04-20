package com.nolanofra

import cats.effect._
import com.nolanofra.api.{ FunTranslationsApiImpl, HttpClient, PokeApiImpl }
import com.nolanofra.configuration.Configuration
import com.nolanofra.routes.FacePokeHttpRoutes
import com.nolanofra.service.{ PokemonService, PokemonTranslationService }
import org.http4s.blaze.server.BlazeServerBuilder

object FacePokeApropplication extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    HttpClient.create.use { httpClient: HttpClient =>
      for {
        config <- Configuration.loadPokemonEndpointBaseUrl
        pokeApi = PokeApiImpl(httpClient.client, config.pokeApiBaseUrl)
        translationApi = FunTranslationsApiImpl(httpClient.client, config.translateAPiBaseUrl)
        pokemonService = PokemonService(pokeApi)
        translationService = PokemonTranslationService(pokeApi, translationApi)
        routes = FacePokeHttpRoutes(pokemonService, translationService)
        _ <- BlazeServerBuilder[IO]
          .bindHttp(Configuration.serverPort, Configuration.serverHost)
          .withHttpApp(routes.facePokeRoutes)
          .serve
          .compile
          .drain
      } yield ExitCode.Success
    }
}
