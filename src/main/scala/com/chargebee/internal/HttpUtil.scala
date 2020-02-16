package com.chargebee.internal

import java.io.IOException
import java.net.{HttpURLConnection, URL, URLConnection, URLEncoder}

import com.chargebee.exceptions.{InvalidRequestException, OperationFailedException, PaymentException}
import com.chargebee.{APIException, Environment, ListResult, Result}
import org.apache.commons.codec.binary.Base64
import spray.json.{JsObject, JsString}

private[internal] object HttpUtil {

	sealed trait Method
	case object GET extends Method
	case object POST extends Method

	private[internal] class Resp {
		private[internal] var httpCode: Int = 0
		private[internal] var jsonContent: JsObject = _

		def this(httpCode: Int, jsonContent: JsObject) {
			this()
			this.httpCode = httpCode
			this.jsonContent = jsonContent
		}

		def toResult: Result = new Result(this.httpCode, this.jsonContent)

		def toListResult: ListResult = new ListResult(httpCode, jsonContent)

	}

	@throws[IOException]
	def get(url: String, params: Params, headers: Map[String, String], env: Environment): Result = {
		var newUrl: String = url
		if (params != null && !params.isEmpty) newUrl = url + '?' + toQueryStr(params, isListReq = true) // fixme: what about url size restrictions ??
		sendRequest(createConnection(newUrl, GET, headers, env)).toResult
	}

	@throws[IOException]
	def post(url: String, params: Params, headers: Map[String, String], env: Environment): Result = doFormSubmit(url, POST, toQueryStr(params), headers, env);

	def toQueryStr(params: Params): String = toQueryStr(params, isListReq = false)

	def toQueryStr(map: Params, isListReq: Boolean): String = {
		var str: String = _
		map.setOfEntries().foreach(entry => {
			if (entry._2.getClass.isAssignableFrom(classOf[List[String]])) {
				val list: List[String] = entry._2.asInstanceOf[List[String]]
				if (isListReq) {
					str += enc(s"${entry._1}=${enc(if (list.isEmpty) "" else list.toString)}")
				}
				list.zipWithIndex.foreach(element => str += enc(s"${entry._1}[${element._2}]=${enc(if (element._1 == null) "" else element._1)}"))
			} else {
				str += enc(s"${entry._1}=${enc(entry._2.asInstanceOf[String])}")
			}
		})
		str.mkString("&")
	}

	@throws[RuntimeException]
	private def enc(value: String): String = URLEncoder.encode(value, Environment.CHARSET)

	@throws[IOException]
	private def doFormSubmit(url: String, m: HttpUtil.Method, queryStr: String, headers: Map[String, String], env: Environment): Result = {
		val conn = createConnection(url, m, headers, env)
		writeContent(conn, queryStr)
		sendRequest(conn).toResult
	}

	@throws[IOException]
	private def writeContent(conn: HttpURLConnection, queryStr: String): Unit = {
		if (queryStr == null) return
		val os = conn.getOutputStream
		try os.write(queryStr.getBytes(Environment.CHARSET))
		finally os.close()
	}

	@throws[IOException]
	private def createConnection(url: String, method: HttpUtil.Method, headers: Map[String, String], config: Environment): HttpURLConnection = {
		val conn: HttpURLConnection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
		conn.setRequestMethod(method.toString)
		setTimeouts(conn, config)
		addHeaders(conn, config)
		addCustomHeaders(conn, headers)
		setContentType(conn, method)
		method match {
			case POST => conn.setDoOutput(true)
			case _ => None
		}
		conn.setUseCaches(false)
		conn
	}

	@throws[IOException]
	private def sendRequest(conn: HttpURLConnection): HttpUtil.Resp = {
		val httpRespCode: Int = conn.getResponseCode
		if (httpRespCode == HttpURLConnection.HTTP_NO_CONTENT) throw new RuntimeException("Got http_no_content response")
		val isError: Boolean = httpRespCode < 200 || httpRespCode > 299
		val content: String = getContentAsString(conn, isError)
		val jsResp: JsObject = getContentAsJSON(content)
		if (isError) try {
			jsResp.getFields("api_error_code")
			jsResp.getFields("type").toString match {
				case "payment" => throw new PaymentException(httpRespCode, jsResp)
				case "operation_failed" => throw new OperationFailedException(httpRespCode, jsResp)
				case "invalid_request" => throw new InvalidRequestException(httpRespCode, jsResp)
				case _ => throw new APIException(httpRespCode, jsResp)
			}
		} catch {
			case ex: APIException =>
				throw ex
			case ex: Exception =>
				throw new RuntimeException(s"Error when parsing the error response. Probably not ChargeBee' error response. The content is \n $content", ex)
		}
		new HttpUtil.Resp(httpRespCode, jsResp)
	}

	private def setTimeouts(conn: URLConnection, config: Environment): Unit = {
		conn.setConnectTimeout(config.connectTimeout)
		conn.setReadTimeout(config.readTimeout)
	}

	private def setContentType(conn: HttpURLConnection, method: HttpUtil.Method): Unit = method match {
		case POST => addHeader(conn, "Content-Type", "application/x-www-form-urlencoded;charset=" + Environment.CHARSET)
		case _ => None
	}

	private def addHeaders(conn: HttpURLConnection, config: Environment): Unit = {
		addHeader(conn, "Accept-Charset", Environment.CHARSET)
		addHeader(conn, "User-Agent", String.format("Chargebee-Java-Client v%s", Environment.LIBRARY_VERSION))
		addHeader(conn, "Authorization", getAuthValue(config))
		addHeader(conn, "Accept", "application/json")
		addHeader(conn, "OS-Version", String.format("%s  %s  %s", System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("os.version")))
		addHeader(conn, "Lang-Version", System.getProperty("java.version"))
	}

	private def addCustomHeaders(conn: HttpURLConnection, headers: Map[String, String]): Unit = headers.foreach(header => addHeader(conn, header._1, header._2))

	private def addHeader(conn: HttpURLConnection, headerName: String, value: String): Unit = conn.setRequestProperty(headerName, value)

	private def getAuthValue(config: Environment) = "Basic " + Base64.encodeBase64String(s"$config.apiKey:".getBytes).replaceAll("\r?", "").replaceAll("\n?", "")

	@throws[Exception]
	private def getContentAsJSON(content: String): JsObject = new JsString(content).asJsObject

	private def getContentAsString(conn: HttpURLConnection, error: Boolean): String = conn.getContent.toString

}
