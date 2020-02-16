package com.chargebee

import com.chargebee.internal.ResultBase
import spray.json.{JsObject, JsValue}

case class Entry(jsObject: JsObject) extends ResultBase(jsObject)

final class ListResult extends IndexedSeq[Entry] with ApiResponse {

	var httpStatusCode: Int = 0
	var respJson: JsObject = _
	private var entries: Seq[Entry] = IndexedSeq[Entry]()

	def this(httpCode: Int, respJson: JsObject) {
		this()
		this.httpStatusCode = httpCode
		this.respJson = respJson
		initEntries()
	}

	override def httpCode: Int = httpCode

	override def jsonResponse: JsObject = respJson

	@throws[RuntimeException]
	def initEntries(): Unit = respJson.getFields("list").foreach((obj: JsValue) => entries = entries :+ Entry(obj.asJsObject))

	// TODO implement this
	def nextOffset: String = ???

	override def toString(): String = respJson.toString

	override def apply(idx: Int): Entry = this(idx)

	override def length: Int = this.size
}
