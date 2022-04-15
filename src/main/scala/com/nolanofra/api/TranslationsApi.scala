package com.nolanofra.api

import cats.effect.IO
import com.nolanofra.api.Translations.Translations
import io.circe.Decoder
import org.http4s.Method.POST
import org.http4s.client.Client
import org.http4s.headers.`Content-Type`
import org.http4s.{ Headers, MediaType, Request, Uri, UrlForm }
import com.nolanofra.api.error.Errors._
import com.nolanofra.service.decoder.TranslationsDecoder.shakespeareDecoder
import com.nolanofra.service.model.FunTranslationsResponse.Translation

object Translations extends Enumeration {
  type Translations = Value
  val Shakespeare = Value("shakespeare")
  val Yoda = Value("yoda")
}

trait FunTranslationsApi {
  def translate[O: Decoder](text: String, translations: Translations): IO[O]
}

class FunTranslationsApiImpl private (httpClient: Client[IO], baseUrl: String) extends FunTranslationsApi {

  override def translate[O: Decoder](text: String, translations: Translations): IO[O] = {
    val funTranslationEndpoint =
      Uri.unsafeFromString(baseUrl + s"$translations.json")

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

    httpClient.run(request).use(handleHttpErrors[O])
  }
}

object FunTranslationsApiImpl {
  def apply(httpClient: Client[IO], baseUrl: String) = new FunTranslationsApiImpl(httpClient, baseUrl)
}
