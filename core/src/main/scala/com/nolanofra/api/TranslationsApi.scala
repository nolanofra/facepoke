package com.nolanofra.api

import cats.effect.IO
import com.nolanofra.api.TranslationType.TranslationType
import com.nolanofra.api.error.Errors._
import org.http4s.Method.POST
import org.http4s.client.Client
import org.http4s.headers.`Content-Type`
import org.http4s.{ Headers, MediaType, Request, Uri, UrlForm }
import com.nolanofra.api.decoder.TranslationsDecoder._
import com.nolanofra.api.model.FunTranslationsResponse.Translation

object TranslationType extends Enumeration {
  type TranslationType = Value
  val Shakespeare = Value("shakespeare")
  val Yoda = Value("yoda")
}

trait FunTranslationsApi {
  def translate(text: String, translations: TranslationType): IO[Translation]
}

class FunTranslationsApiImpl private (httpClient: Client[IO], baseUrl: String) extends FunTranslationsApi {

  override def translate(text: String, translationType: TranslationType): IO[Translation] = {
    val funTranslationEndpoint =
      Uri.unsafeFromString(baseUrl + s"translate/$translationType.json")

    val request = Request[IO](
      method = POST,
      uri = funTranslationEndpoint,
      headers = Headers {
        `Content-Type`(MediaType.application.`x-www-form-urlencoded`)
      }
    ).withEntity(
      UrlForm(
        "text" -> text
      )
    )

    httpClient.run(request).use(handleHttpErrors[Translation])
  }
}

object FunTranslationsApiImpl {
  def apply(httpClient: Client[IO], baseUrl: String) = new FunTranslationsApiImpl(httpClient, baseUrl)
}
