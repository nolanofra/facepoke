package com.nolanofra.api.decoder

import com.nolanofra.api.model.FunTranslationsResponse.{ Contents, Translation }
import io.circe.Decoder

object TranslationsDecoder {
  implicit val contentsDecoder: Decoder[Contents] = Decoder.forProduct1("translated")(Contents)

  implicit val shakespeareDecoder: Decoder[Translation] =
    Decoder.forProduct1("contents")(Translation)

}
