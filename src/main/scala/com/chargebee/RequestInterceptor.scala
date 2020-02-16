package com.chargebee

import com.chargebee.internal.RequestBase

trait RequestInterceptor {

	@throws[Exception]
	def handleRequest(requestWrap: RequestWrap[_ <: RequestBase[_ <: RequestBase[_ <: AnyRef]]]): ApiResponse

}
