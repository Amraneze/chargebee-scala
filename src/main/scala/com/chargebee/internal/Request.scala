package com.chargebee.internal

import java.io.IOException

import com.chargebee.internal.HttpUtil.{GET, POST}
import com.chargebee.{ApiResponse, Environment, RequestWrap, Result}

class Request[U <: Request[U]](httpMethod: HttpUtil.Method, uri: String) extends RequestBase[U] {

	def param(paramName: String, value: AnyRef): U = {
		params.add(paramName, Option(value))
		this.asInstanceOf[U]
	}

	@throws[Exception]
	final def request(): Result = request(Environment.defaultConfig)

	@throws[Exception]
	final def request(env: Environment): Result = {
		val requestWrapper: RequestWrap[Request[U]] = new RequestWrap[Request[U]](env, this) {

			@throws[IOException]
			def _request(environment: Option[Environment], request: Request[_]): ApiResponse = {
				if (environment.isEmpty) throw new RuntimeException("Environment cannot be null")
				val env = environment.get
				val url: String = s"${env.apiBaseUrl}${request.uri}"
				request.httpMethod match {
					case GET => HttpUtil.get(url, request.params, request.headers, env)
					case POST => HttpUtil.post(url, request.params, request.headers, env)
					case _ => throw new RuntimeException(s"Not handled type [${request.httpMethod}]")
				}
			}

			override def call: ApiResponse = _request(Option(env), request)
		}
		if (env.reqInterceptor != null) env.reqInterceptor
			.handleRequest(requestWrapper)
			.asInstanceOf[Result]
		else requestWrapper.call()
    		.asInstanceOf[Result]
	}

	def httpMeth(): HttpUtil.Method = httpMethod
}
