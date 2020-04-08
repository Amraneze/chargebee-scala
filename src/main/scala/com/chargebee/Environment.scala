package com.chargebee

class Environment {
	/**
	 * You can generate API keys from the ChargeBee web interface.
	 */
	var apiKey: String = _

	/**
	 * Your chargebee subdomain. Could be your sandbox or production.
	 */
	var siteName: String = _

	/**
	 * Timeout value, in milliseconds, to be used when trying to conect to the
	 * chargebee api server. If the timeout expires before the connection can be
	 * established, a java.net.SocketTimeoutException is raised. A timeout of
	 * zero is interpreted as an infinite timeout.
	 */
	var connectTimeout: Int = Integer.getInteger("com.chargebee.api.http.timeout.connect", 15000)

	/**
	 * Timeout value, in milliseconds, to be used when reading response from the
	 * chargebee api server. If the timeout expires before there is data available
	 * for read, a java.net.SocketTimeoutException is raised. A timeout of zero
	 * is interpreted as an infinite timeout.
	 */
	var readTimeout: Int = Integer.getInteger("com.chargebee.api.http.timeout.read", 60000)

	private[internal] var apiBaseUrl: String = ""

	private[internal] var reqInterceptor: RequestInterceptor = _

	def this(siteName: String, apiKey: String) {
		this(siteName, apiKey, null)
	}

	def this(siteName: String, apiKey: String, reqInterceptor: RequestInterceptor) {
		this()
		this.apiKey = apiKey
		this.siteName = siteName
		this.reqInterceptor = reqInterceptor
		val domainSuffix = System.getProperty("com.chargebee.api.domain.suffix", "chargebee.com")
		val proto = System.getProperty("com.chargebee.api.protocol", "https")
		this.apiBaseUrl = s"$proto://$siteName.$domainSuffix/api/${Environment.API_VERSION}"
	}

	def configure(siteName: String, apikey: String): Unit = Environment.defaultEnv = new Environment(siteName, apikey)

	def reqInterceptor(reqInterceptor: RequestInterceptor): Unit = {
		Environment.defaultConfig.reqInterceptor = reqInterceptor
	}

	def _reqInterceptor: RequestInterceptor = reqInterceptor

	def _apiBaseUrl: String = this.apiBaseUrl

}


private[internal] object Environment {

	val CHARSET = "UTF-8"

	val API_VERSION = "v2"

	val LIBRARY_VERSION = "2.7.6"

	var defaultEnv: Environment = _ // singleton

	def defaultConfig: Environment = {
		if (Option(Environment.defaultEnv).isDefined) throw new RuntimeException("The default environment has not been configured")
		Environment.defaultEnv
	}
}
