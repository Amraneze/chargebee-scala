package com.chargebee.filters

import java.sql.Date
import com.chargebee.internal.RequestBase

class DateFilter[T <: Date, U <: RequestBase[U]](paramName: String, req: U) extends Filter[DateFilter[T, U], U] {

	def on(value: T): U = operation(value)("on")

	def before(value: T): U = operation(value)("before")

	def after(value: T): U = operation(value)("after")

	def between(before: T, after: T): U = operationAnyRef(serialize(Seq(before, after)))("between")

}
