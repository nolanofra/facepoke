package com.nolanofra

import cats.effect._
import com.nolanofra.api.{ HttpClient, PokeApiImpl }
import com.nolanofra.configuration.Configuration
import com.nolanofra.routes.FacePokeHttpRoutes
import com.nolanofra.service.PokemonService
import org.http4s.blaze.server.BlazeServerBuilder

object FacePokeApplication extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    HttpClient.create.use { httpClient: HttpClient =>
      for {
        pokeApiConfig <- Configuration.loadPokemonEndpointBaseUrl
        pokeApi = PokeApiImpl(httpClient.client, pokeApiConfig.baseUrl)
        pokemonService = PokemonService(pokeApi)
        routes = FacePokeHttpRoutes(pokemonService)
        _ <- BlazeServerBuilder[IO]
          .bindHttp(Configuration.serverPort, Configuration.serverHost)
          .withHttpApp(routes.facePokeRoutes)
          .serve
          .compile
          .drain
      } yield ExitCode.Success
    }
}
