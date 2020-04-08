package com.chargebee

import com.chargebee.internal.ResultBase
import org.json.JSONObject

final class Result(httpStatusCode: Int, jsonObject: JSONObject) extends ResultBase(jsonObject) with ApiResponse {

	override def httpCode: Int = this.httpStatusCode

	override def jsonResponse: JSONObject = jsonObj

}
