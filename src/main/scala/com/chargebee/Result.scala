package com.chargebee

import com.chargebee.internal.ResultBase
import spray.json.JsObject

final class Result(httpStatusCode: Int, jsonObject: JsObject) extends ResultBase(jsonObject) with ApiResponse {

	override def httpCode: Int = this.httpStatusCode

	override def jsonResponse: JsObject = jsonObj

}
