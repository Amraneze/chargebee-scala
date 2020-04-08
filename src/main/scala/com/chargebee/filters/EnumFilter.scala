package com.chargebee.filters

import com.chargebee.internal.RequestBase

class EnumFilter[T, U <: RequestBase[U]](paramName: String, req: U) extends Filter[T, U] {

	def isNot(value: AnyRef): U = operation(value)("is_not")

	def in(value: AnyRef*): U = operationAnyRef(serialize(value))("in")

	def notIn(value: AnyRef*): U = operationAnyRef(serialize(value))("not_in")

}