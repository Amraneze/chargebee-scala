package com.chargebee

import org.json.JSONObject

sealed abstract class Exception(msg: String) extends RuntimeException(msg)
class APIExceptionJSONObject(jsonObj: JSONObject) extends Exception(jsonObj.getString("message").mkString)
class APIExceptionString(msg: String) extends Exception(msg)

class APIException(msg: String) extends RuntimeException(msg) {

	var jsonObj: JSONObject = _
	var httpStatusCode = 0
	var `type`: String = _
	var param: String = _
	var apiErrorCode: String = _

	/**
	 * Use {@link #httpStatusCode} instead.
	 *
	 * @deprecated
	 */
	@deprecated var httpCode = 0

	/**
	 * Use {@link #apiErrorCode} instead.
	 *
	 * @deprecated
	 */
	@deprecated var code: String = _
	/**
	 * Use {@link #getMessage()} instead.
	 *
	 * @deprecated
	 */
	@deprecated var message: String = _

	def this(httpStatusCode: Int, jsonObj: JSONObject) = {
		this(jsonObj.getString("message"))
		this.jsonObj = jsonObj
		this.httpStatusCode = httpStatusCode
		this.apiErrorCode = jsonObj.getString("api_error_code")
		this.`type` = jsonObj.optString("type")
		this.param = jsonObj.optString("param")

		this.httpCode = httpStatusCode
		this.code = jsonObj.getString("error_code")
		this.message = jsonObj.getString("error_msg")
	}

	override def toString: String = this.jsonObj.toString

	def isParamErr: Boolean = this.param != null
}

object APIException {
	def apply(msg: Int, jsonObj: JSONObject) = new APIException(msg, jsonObj)
}