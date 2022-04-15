package com.nolanofra.service.decoder

import com.nolanofra.service.model.FunTranslationsResponse.{ Contents, Translation }
import io.circe.Decoder

object TranslationsDecoder {
  implicit val contentsDecoder: Decoder[Contents] = Decoder.forProduct1("translated")(Contents)

  implicit val shakespeareDecoder: Decoder[Translation] =
    Decoder.forProduct1("contents")(Translation)

}
