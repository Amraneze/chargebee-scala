package com.chargebee

import org.json.JSONObject

trait ApiResponse {

	def httpCode: Int
	def jsonResponse: JSONObject

}
