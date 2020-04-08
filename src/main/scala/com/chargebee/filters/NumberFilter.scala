package com.chargebee.filters

import com.chargebee.internal.RequestBase
import org.json.JSONArray

class NumberFilter[T <: AnyRef, U <: RequestBase[U]](paramName: String, req: U) extends Filter[NumberFilter[T, U], U] {

	def gt(value: T): U = operation(value)("gt")

	def lt(value: T): U = operation(value)("lt")

	def gte(value: T): U = operation(value)("gte")

	def lte(value: T): U = operation(value)("lte")

	def isNot(value: T): U = operation(value)("is_not")

	def between(before: T, after: T): U = operationAnyRef(new JSONArray(Seq(before, after)))("between")

}