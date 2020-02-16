package com.chargebee.exceptions

import com.chargebee.APIException
import spray.json.JsObject

case class PaymentException(httpResponseCode: Int, jsObject: JsObject) extends APIException(httpResponseCode, jsObject)

