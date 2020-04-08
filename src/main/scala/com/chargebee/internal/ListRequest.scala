package com.chargebee.internal

import java.io.IOException
import java.util.Date

import com.chargebee.{ApiResponse, Environment, ListResult, RequestWrap}
import com.chargebee.filters.{BooleanFilter, DateFilter, NumberFilter, StringFilter, TimestampFilter}

class ListRequest[U <: ListRequest[_]](uri: String) extends RequestBase[U] {

	def limit(limit: Int): U = addParam("limit", limit)

	def offset(limit: Int): U = addParam("limit", limit)

	def stringFilterParam(paramName: String): StringFilter[U] = new StringFilter[U](paramName, this.asInstanceOf[U], true)
    		.supportsPresenceOperator(true)

	def booleanFilterParam(paramName: String): BooleanFilter[U] = new BooleanFilter[U](paramName, this.asInstanceOf[U])
			.supportsPresenceOperator(true)

	def longFilterParam(paramName: String): NumberFilter[Long, U] = new NumberFilter[Long, U](paramName, this.asInstanceOf[U])
			.supportsPresenceOperator(true)

	def timestampFilterParam(paramName: String): TimestampFilter[U] = new TimestampFilter[U](paramName, this.asInstanceOf[U])
			.supportsPresenceOperator(true)
			.asInstanceOf[TimestampFilter[U]]

	def dateFilterParam(paramName: String): DateFilter[Date, U] = new DateFilter[Date, U](paramName, this.asInstanceOf[U])
			.supportsPresenceOperator(true)

	@throws[Exception]
	def request(): ListResult = request(Environment.defaultConfig)

	@throws[Exception]
	def request(env: Environment): ListResult = {
		val requestWrapper: RequestWrap[ListRequest[U]] = new RequestWrap(env, this) {
			override def call: ApiResponse = _request(env, request)
		}
		if (env.reqInterceptor != null) env.reqInterceptor
			.handleRequest(requestWrapper)
			.asInstanceOf[ListResult]
		else requestWrapper.call()
			.asInstanceOf[ListResult]
	}

	@throws[IOException]
	private def _request(env: Environment, req: ListRequest[U]): ListResult = {
		if (env == null)
			throw new RuntimeException("Environment cannot be null")
		val url: String = s"${env.apiBaseUrl}${req.uri}"
		HttpUtil.getList(url, req.params, req.headers, env)
	}

	def addParam(param: String, value: AnyRef): U = {
		params.addOpt(param, value)
		this.asInstanceOf[U]
	}
}
