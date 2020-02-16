package com.chargebee.internal

import scala.collection.immutable.HashMap

class RequestBase[T <: RequestBase[_]] {

	protected var uri: String = _
	protected var params: Params = new Params
	protected var headers: Map[String, String] = new HashMap[String, String]

	def _params: Params = params

	def _uri: String = uri

	def _headers: Map[String, String] = headers

	def header_(headerName: String, headerValue: String): T = {
		headers += (headerName -> headerValue)
		this.asInstanceOf[T]
	}
}