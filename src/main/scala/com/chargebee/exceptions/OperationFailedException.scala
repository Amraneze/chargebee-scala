package com.chargebee.exceptions

import com.chargebee.APIException
import org.json.JSONObject

case class OperationFailedException(httpResponseCode: Int, jsObject: JSONObject) extends APIException(httpResponseCode, jsObject)
