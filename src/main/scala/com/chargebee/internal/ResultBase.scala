package com.chargebee.internal

import org.json.JSONObject

case class ResultBase(jsonObject: JSONObject) {

	def jsonObj: JSONObject = this.jsonObject

}
