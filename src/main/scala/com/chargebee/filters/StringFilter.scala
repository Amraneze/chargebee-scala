package com.chargebee.filters

import com.chargebee.internal.RequestBase
import org.json.JSONArray

class StringFilter[U <: RequestBase[U]](paramName: String, req: U, var supportsMultiOperators: Boolean = false) extends Filter[StringFilter[U], U] {

	def supportsMultiOperators(supportsMultiOperators: Boolean): StringFilter[U] = {
		this.supportsMultiOperators = supportsMultiOperators
		this
	}

	def isNot(value: AnyRef): U = operation(value)("is_not")
	def startsWith(value: AnyRef): U = operation(value)("startsWith")

	def in(value: AnyRef*): U = {
		if (!this.supportsMultiOperators) throw new UnsupportedOperationException("operator '[in]' is not supported for this filter-parameter")
		operationAnyRef(new JSONArray(value))("in")
	}

	def notIn(value: AnyRef*): U = {
		if (!this.supportsMultiOperators) throw new UnsupportedOperationException("operator '[not_in]' is not supported for this filter-parameter")
		operationAnyRef(new JSONArray(value))("not_in")
	}

}