package com.nolanofra.configuration

import cats.effect.IO
import com.typesafe.config.ConfigFactory

case class ApiEndpointConfiguration(pokeApiBaseUrl: String, translateAPiBaseUrl: String)

object Configuration {
  val serverHost = "0.0.0.0"
  val serverPort = 5000

  def loadPokemonEndpointBaseUrl: IO[ApiEndpointConfiguration] =
    IO.apply(ConfigFactory.load)
      .map(config =>
        ApiEndpointConfiguration(config.getString("pokeAPI.baseUrl"), config.getString("translationAPI.baseUrl"))
      )
}
