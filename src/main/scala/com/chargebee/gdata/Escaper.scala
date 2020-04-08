package com.chargebee.gdata

// TODO Add JavaDoc
trait Escaper {

	def escape(string: String): String

	def escape(out: Appendable): Appendable

}
