package com.chargebee.internal

import scala.collection.immutable.HashMap

trait RequestBase[T <: RequestBase[_]] {

	private[internal] var _uri: String = _
	private[internal] var _params: Params = new Params
	private[internal] var _headers: Map[String, String] = new HashMap[String, String]

	def params: Params = _params
	def params_= (params: Params): Unit = _params = params

	def uri: String = _uri
	def uri_= (uri: String): Unit = _uri = uri

	def headers: Map[String, String] = _headers

	def header_(headerName: String, headerValue: String): T = {
		headers += (headerName -> headerValue)
		this.asInstanceOf[T]
	}
}