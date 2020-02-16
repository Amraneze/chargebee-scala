package com.chargebee.exceptions

import com.chargebee.APIException
import spray.json.JsObject

case class OperationFailedException(httpResponseCode: Int, jsObject: JsObject) extends APIException(httpResponseCode, jsObject)
