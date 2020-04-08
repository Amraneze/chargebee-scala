package com.chargebee.internal

import org.json.JSONObject

object ClazzUtil {

	def getObjType(clazz: Class[_]): String = toUnderScores(clazz.getSimpleName)

	def toUnderScores(camelCaseName: String): String = {
		camelCaseName.zipWithIndex.map {
			case (char: Char, index: Int) => if (index != 0 && char.isUpper) s"_${char.toLower}" else s"${char.toLower}"
		}.mkString
	}

	@Deprecated("In favor of the new the new function getClazz","16-02-2020")
	def getClaz(objType: String): Class[_] = Class.forName(s"com.chargebee.models.${}")

	def getClazz(objType: String): Class[_] = Class.forName(s"com.chargebee.models.${}")

	def toCamelCase(name: String): String = toCamelCase(name.split("_"))

	def toCamelCase(names: Seq[String]): String = names.map(_.toLowerCase.capitalize).mkString

	@throws(classOf[RuntimeException])
	def createInstance[T](clazz: Class[T], jsObject: JSONObject): T = clazz.getDeclaredConstructor(classOf[JSONObject]).newInstance(jsObject)

	@throws(classOf[RuntimeException])
	def createNumberInstance[T](clazz: Class[T], value: String): T = clazz.getConstructor(classOf[String]).newInstance(value)
}
