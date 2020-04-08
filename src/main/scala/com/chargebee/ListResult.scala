package com.chargebee

import com.chargebee.internal.ResultBase
import org.json.{JSONArray, JSONObject}

case class Entry(jsObject: JSONObject) extends ResultBase(jsObject)

final class ListResult extends IndexedSeq[Entry] with ApiResponse {

	var httpStatusCode: Int = 0
	var respJson: JSONObject = _
	private var entries: Seq[Entry] = IndexedSeq[Entry]()

	def this(httpCode: Int, respJson: JSONObject) {
		this()
		this.httpStatusCode = httpCode
		this.respJson = respJson
		initEntries()
	}

	override def httpCode: Int = httpStatusCode

	override def jsonResponse: JSONObject = respJson

	@throws[RuntimeException]
	def initEntries(): Unit = {
		val jsonArray: JSONArray = respJson.getJSONArray("list")
		for (i <- 0 until jsonArray.length()) {
			entries = entries :+ Entry(jsonArray.getJSONObject(i))
		}
	}

	// It doesn't return null in case of an empty cursor
	def nextOffset: String = Option(respJson.optString("next_offset")).orNull

	override def toString(): String = respJson.toString

	override def apply(idx: Int): Entry = this(idx)

	override def length: Int = this.size
}
