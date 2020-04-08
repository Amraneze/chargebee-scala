package com.chargebee.internal

import java.io.{BufferedReader, IOException, InputStream, InputStreamReader}
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.logging.{Level, Logger}

import com.chargebee.Environment
import com.chargebee.gdata.PercentEscaper
import org.json.{JSONArray, JSONException, JSONObject}

import scala.collection.mutable.ArrayBuffer

class Resource[U] {

	var jsonObj: JSONObject = _

	def this(jsonObj: JSONObject) = {
		this()
		this.jsonObj = jsonObj
	}

	def this(jsonStr: String) = {
		this()
		try {
			this.jsonObj = new JSONObject(jsonStr)
		} catch {
			case e: JSONException => throw new RuntimeException(e)
		}
	}

	@throws[IOException]
	def this(is: InputStream) = {
		this(new BufferedReader(new InputStreamReader(is)))
	}

	@throws[IOException]
	def this(rd: BufferedReader) = {
		this(Resource.getAsString(rd))
	}

	def reqString(key: String): String = assertReqProp(key, optString(key))

	def optString(key: String): Option[String] = optional[String](key)

	def reqBoolean(key: String): Boolean = assertReqProp(key, optBoolean(key))

	def optBoolean(key: String): Option[Boolean] = optional[Boolean](key)

	def reqInteger(key: String): Integer = assertReqProp(key, genericOpt[Integer](key))

	def reqBigDecimal(key: String): BigDecimal = assertReqProp(key, genericOpt[BigDecimal](key))

	def reqDouble(key: String): Double = assertReqProp(key, genericOpt[Double](key))

	def genericOpt[T >: Null <: AnyRef](key: String): Option[T] = {
		val value: Option[T] = optional[T](key)
		try if (value != null) value
		else null catch {
			case _: Exception => throw conversionException(key)
		}
	}

	def reqLong(key: String): Long = assertReqProp(key, optLong(key))

	def optLong(key: String): Option[Long] = {
		val optValue: Option[Any] = Option(jsonObj.opt(key))
		if (optValue.isEmpty) return null
		// special handling for Long. Accepting both Long and Integer values !!
		val value: Any = optValue.get
		val clazz: Class[_] = value.getClass

		if (clazz.isAssignableFrom(classOf[Int])) {
			Option(value.asInstanceOf[Integer].longValue())
		} else if (clazz.isAssignableFrom(classOf[Long])) {
			Option(value.asInstanceOf[Long])
		} else throw new RuntimeException(s"Wrong type. Expecing Long type but got ${value.getClass.getSimpleName}")
	}

	def reqJSONObject(key: String): JSONObject = assertReqProp(key, optJSONObject(key))

	def optJSONObject(key: String): Option[JSONObject] = optional[JSONObject](key)

	def reqJSONArray(key: String): JSONArray = assertReqProp(key, optJSONArray(key))

	def optJSONArray(key: String): Option[JSONArray] = optional[JSONArray](key)

	def reqTimestamp(key: String): Timestamp = assertReqProp(key, optTimestamp(key))

	def optTimestamp(key: String): Option[Timestamp] = {
		val unixTime: Option[Int] = optional[Int](key)
		unixTime match {
			case Some(time) => Option(new Timestamp(time * 1000l))
			case None => None
		}
	}

	def reqEnum[T <: Enum[_]](key: String, enumClass: Class[T]): T = assertReqProp(key, optEnum(key, enumClass))

	def optEnum[T >: Null <: Enum[_]](key: String, enumClass: Class[T]): Option[T] = {
		val value: Option[String] = optString(key)
		if (value.isEmpty) Option.empty[T]
		try Option(Enum.valueOf(enumClass, value.get.toUpperCase))
		catch {
			case _: Exception =>
				Resource.logger.log(Level.SEVERE, "The property {0} has unexpected value {1}", Array[AnyRef](key, value))
				Option(Enum.valueOf(enumClass, Resource.unknown))
		}
	}

	def reqList[T <: Resource[_]](key: String, clazz: Class[T]): Seq[T] = {
		val list: Seq[T] = optList(key, clazz)
		if (list.isEmpty) throw new RuntimeException(s"The sub-resource [$key] is not present")
		list
	}

	def optList[T](key: String, clazz: Class[T]): Seq[T] = {
		val arr: Option[JSONArray] = Option(jsonObj.optJSONArray(key))
		var values: Seq[T] = Seq.empty[T]
		if (arr.isEmpty) return values
		val jsonArray = arr.get
		for (i <- 0 to jsonArray.length) {
			if (clazz.isAssignableFrom(classOf[String])) {
				values = values :+ jsonArray.optString(i).asInstanceOf[T]
			} else if (clazz.isAssignableFrom(classOf[Number])) {
				val value = jsonArray.optString(i)
				if (value == null) values = values :+ null
				else values = values :+ ClazzUtil.createNumberInstance(clazz, value)
			} else {
				values = values :+ ClazzUtil.createInstance(clazz, jsonArray.optJSONObject(i))
			}
		}
		values
	}

	def reqSubResource[T <: Resource[T]](key: String, clazz: Class[T]): T = assertReqProp(key, optSubResource(key, clazz))

	def optSubResource[T <: Resource[T]](key: String, clazz: Class[T]): Option[T] =	{
		val resJson = Option(jsonObj.optJSONObject(key))
		if (resJson.isEmpty) Option.empty[T]
		Option(ClazzUtil.createInstance(clazz, resJson.get))
	}

	private def optional[T](key: String): Option[T] = {
		val value = Option(jsonObj.opt(key))
		if (value.isEmpty) Option.empty[T]
		Option(value.get.asInstanceOf[T])
	}

	def toJson: String = jsonObj.toString

	override def toString: String = {
		try {
			jsonObj.toString(2)
		} catch {
			case e: JSONException => throw new RuntimeException(e)
		}
	}

	private def assertReqProp[T](key: String, value: Option[T]): T = value
		.getOrElse(throw new RuntimeException(s"The property [$key] is not present"))

	private def conversionException(key: String): RuntimeException = new RuntimeException(s"The property [$key] not in the required format")

}

object Resource {

	private val logger = Logger.getLogger(classOf[Resource[_]].getName)
	private val unknown = "_UNKNOWN"

	@throws[IOException]
	def getAsString(rd: BufferedReader): String = {
		val lines = new ArrayBuffer[String]()
		var line: String = null
		while ({line = rd.readLine; line != null}) {
			lines.addOne(line)
		}
		rd.close()
		lines.mkString
	}

	protected def nullCheck(id: String): String = Option(id).filter(!_.isEmpty).getOrElse(throw new RuntimeException("id cannot be null/empty"))

	protected def uri(paths: String*): String = {
		try {
			// Using URLEncoder is wrong as it encodes for form. Replace it with Google's CharEscapers.java
			paths.map(path => new PercentEscaper(PercentEscaper.SAFEPATHCHARS_URLENCODER, false).escape(path))
				.mkString("/", "/", "")
		} catch {
			case e: Exception => throw new RuntimeException(e)
		}
	}

	protected def apiVersionCheck(jsonObj: JSONObject): Unit = {
		if (!jsonObj.has("api_version")) return
		val apiVersion: Option[String] = Option(jsonObj.optString("api_version"))
		if (apiVersion.isDefined && !apiVersion.get.equalsIgnoreCase(Environment.API_VERSION)) throw new RuntimeException(s"API version [${apiVersion.get.toUpperCase}] in response does not match with client library API version [${Environment.API_VERSION.toUpperCase}]")
	}
}