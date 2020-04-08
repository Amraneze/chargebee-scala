package com.chargebee.filters

import com.chargebee.internal.Params.toValStr
import com.chargebee.internal.RequestBase
import org.json.JSONArray

trait Filter[T <: Filter[T, U], U <: RequestBase[U]] {

	protected var paramName: String = _
	protected var req: U = _
	protected var supportsPresenceOperator: Boolean = false

	def paramName_=(paramName: String): Unit
	def req_=(req: U): Unit

	def supportsPresenceOperator(supportsPresenceOperator: Boolean): T = {
		this.supportsPresenceOperator = supportsPresenceOperator
		this.asInstanceOf[T]
	}

	def is(value: AnyRef): U = {
		req.params.addOpt(s"$paramName[is]", value)
		req
	}

	def isPresent(isPresent: Boolean): U = {
		if (!supportsPresenceOperator) throw new UnsupportedOperationException("operator '[is_present]' is not supported for this filter-parameter")
		req.params.addOpt(s"$paramName[is_present]", isPresent)
		req
	}

	def operation(value: AnyRef)(implicit operation: String): U = {
		req.params.addOpt(s"$paramName[$operation]", value)
		req
	}

	private[internal] def operationAnyRef(value: AnyRef)(implicit operation: String): U = {
		req.params.addOpt(s"$paramName[$operation]", value)
		req
	}

	def serialize(values: Seq[AnyRef]): JSONArray = {
		val jsArray: JSONArray = new JSONArray()
		values.foreach((value: AnyRef) => jsArray.put(toValStr(value)))
		jsArray
	}
}
