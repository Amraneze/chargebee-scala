package com.chargebee

import spray.json._

trait ApiResponse {

	def httpCode: Int
	def jsonResponse: JsObject

}
