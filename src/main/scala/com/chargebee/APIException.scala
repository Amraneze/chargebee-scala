package com.chargebee

import spray.json.JsObject

sealed abstract class Exception(msg: String) extends RuntimeException(msg)
class APIExceptionJsObject(jsonObj: JsObject) extends Exception(jsonObj.getFields("message").mkString)
class APIExceptionString(msg: String) extends Exception(msg)

class APIException(msg: String) extends RuntimeException(msg) {

	var jsonObj: JsObject = _
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

	def this(httpStatusCode: Int, jsonObj: JsObject) = {
		this(jsonObj.getFields("message").mkString)
		this.jsonObj = jsonObj
		this.httpStatusCode = httpStatusCode
		this.apiErrorCode = jsonObj.getFields("api_error_code").mkString
		this.`type` = jsonObj.getFields("type").mkString
		this.param = jsonObj.getFields("param").mkString

		this.httpCode = httpStatusCode
		this.code = jsonObj.getFields("error_code").mkString
		this.message = jsonObj.getFields("error_msg").mkString
	}

	override def toString: String = this.jsonObj.toString

	def isParamErr: Boolean = this.param != null
}

object APIException {
	def apply(msg: Int, jsonObj: JsObject) = new APIException(msg, jsonObj)
}