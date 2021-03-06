package com.chargebee.exceptions

import com.chargebee.APIException
import org.json.JSONObject

case class InvalidRequestException(httpResponseCode: Int, jsObject: JSONObject) extends APIException(httpResponseCode, jsObject)
