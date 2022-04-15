package com.nolanofra.service.model

object FunTranslationsResponse {
  case class Contents(translated: String)
  case class Translation(contents: Contents)
}
