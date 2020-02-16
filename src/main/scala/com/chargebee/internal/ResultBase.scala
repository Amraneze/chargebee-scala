package com.chargebee.internal

import spray.json.JsObject

case class ResultBase(jsonObject: JsObject) {

	def jsonObj: JsObject = this.jsonObject

}
