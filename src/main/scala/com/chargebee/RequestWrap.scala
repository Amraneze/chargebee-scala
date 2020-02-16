package com.chargebee

import java.util.concurrent.Callable

import com.chargebee.internal.RequestBase

sealed abstract class RequestWrap[T <: RequestBase[T]] extends Callable[ApiResponse] {

	var env: Environment = _
	var request: T = _

	def this(env: Environment, request: T) {
		this()
		this.env = env
		this.request = request
	}

	@throws[Exception]
	override def call: ApiResponse

}
