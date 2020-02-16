package com.chargebee.exceptions

import com.chargebee.APIException
import spray.json.JsObject

case class InvalidRequestException(httpResponseCode: Int, jsObject: JsObject) extends APIException(httpResponseCode, jsObject)
