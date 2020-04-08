package com.chargebee.internal

import java.sql.Timestamp
import java.util.Map.Entry

import org.json.{JSONArray, JSONObject}

import scala.collection.immutable.HashMap

class Params {

	private var params: Map[String, AnyRef] = new HashMap[String, AnyRef]

	/**
	 * Add a parameter name and its value to the list of params
	 * @param paramName name of the parameter
	 * @param value the value of the param to add, it can be null
	 */
	@deprecated("Use the new function with Optional param","16-02-2020")
	def add(paramName: String, value: AnyRef): Unit = {
		if (Option(value).isDefined) throw new RuntimeException(s"The param { $paramName } cannot be null")
		params += (paramName -> Params.toValStr(value))
	}

	/**
	 * Add a parameter name and its value to the list of params
	 * @param paramName name of the parameter
	 * @param value An optional value of the param to add
	 */
	@throws(classOf[RuntimeException])
	def add(paramName: String, value: Option[AnyRef] = None): Unit = {
		if (value.isDefined) throw new RuntimeException(s"The param { $paramName } cannot be null")
		params += (paramName -> Params.toValStr(value.get))
	}

	def addOpt(paramName: String, value: AnyRef = ""): Unit = params += (paramName -> Params.toValStr(value))

	def size: Int = params.size

	def isEmpty: Boolean = params.isEmpty

	def isNotEmpty: Boolean = params.nonEmpty

	def keys: Set[String] = params.keySet

	def entries: Set[Entry[String, AnyRef]] = {
		lazy val entries: Set[java.util.Map.Entry[String, AnyRef]] = params.map {
			case (key, value) => new java.util.AbstractMap.SimpleImmutableEntry[String, AnyRef](key, value)
		}.toSet
		entries
	}

	def setOfEntries(): Set[(String, AnyRef)] = params.toSet

	override def toString: String = params.mkString
}

object Params {
	def toValStr(value: Any*): AnyRef = {
		val clazz: Class[_] = value.getClass
		if (clazz.isAssignableFrom(classOf[String]) || clazz.isAssignableFrom(classOf[Int])
			|| clazz.isAssignableFrom(classOf[Long]) || clazz.isAssignableFrom(classOf[Float])
			|| clazz.isAssignableFrom(classOf[Double]) || clazz.isAssignableFrom(classOf[Boolean])) {
			value.toString
		} else if (clazz.isEnum) {
			value.toString.toLowerCase
		} else if (clazz.isAssignableFrom(classOf[Timestamp])) {
			asUnixTimestamp(value.asInstanceOf[Timestamp]).toString
		} else if (clazz.isAssignableFrom(classOf[Iterable[_]])) {
			value.asInstanceOf[Iterable[_]].map(obj => toValStr(obj.toString))
		} else if (clazz.isAssignableFrom(classOf[List[_]])) {
			value.asInstanceOf[List[_]].map(obj => toValStr(obj.toString))
		} else if (clazz.isAssignableFrom(classOf[JSONObject])) {
			value.asInstanceOf[JSONObject].toString
		} else if (clazz.isAssignableFrom(classOf[JSONArray])) {
			value.asInstanceOf[JSONArray].toString
		} else throw new RuntimeException(s"Type [ ${clazz.getName} ] not handled")
	}

	def asUnixTimestamp(ts: Timestamp): Long = ts.getTime / 1000
}