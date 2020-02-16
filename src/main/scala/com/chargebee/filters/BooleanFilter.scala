package com.chargebee.filters

import com.chargebee.internal.RequestBase

class BooleanFilter[U <: RequestBase[U]](var paramName: String, var req: U) {
	private var supportsPresenceOperator = false

	def is(value: Boolean): U = {
		req._params.addOpt(paramName + "[is]", value)
		req
	}

	def supportsPresenceOperator(supportsPresenceOperator: Boolean): BooleanFilter[U] = {
		this.supportsPresenceOperator = supportsPresenceOperator
		this
	}

	def isPresent(value: Boolean): U = {
		if (!supportsPresenceOperator) throw new UnsupportedOperationException("operator '[is_present]' is not supported for this filter-parameter")
		req._params.addOpt(s"$paramName[is_present]", value)
		req
	}
}