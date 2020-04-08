package com.chargebee.gdata

// TODO Add JavaDoc
class PercentEscaper extends UnicodeEscaper {

	private lazy val URI_ESCAPED_SPACE: Array[Char] = Array('+')
	private lazy val UPPER_HEX_DIGITS: Array[Char] = "0123456789ABCDEF".toCharArray
	private var plusForSpace: Boolean = _
	private var safeOctets: Array[Boolean] = _

	def this(safeChars: String, plusForSpace: Boolean) = {
		this()
		// Avoid any misunderstandings about the behavior of this escaper
		if (""".*[0-9A-Za-z].*""".matches(safeChars)) throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified")
		// Avoid ambiguous parameters. Safe characters are never modified so if
		// space is a safe character then setting plusForSpace is meaningless.
		if (plusForSpace && safeChars.contains(" ")) new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character")
		if (safeChars.contains("%")) new IllegalArgumentException("The '%' character cannot be specified as 'safe'")
		this.plusForSpace = plusForSpace
		this.safeOctets = createSafeOctets(safeChars)
	}

	private[gdata] def createSafeOctets(safeChars: String): Array[Boolean] = {
		var maxChar: Int = 'z'
		val safeCharArray: Array[Char] = safeChars.toCharArray
		safeCharArray.foreach(char => maxChar = Math.max(char, maxChar))

		val octets: Array[Boolean] = new Array[Boolean](maxChar + 1)
		for(c <- '0' until '9') octets(c) = true
		for(c <- 'a' until 'z') octets(c) = true
		for(c <- 'A' until 'Z') octets(c) = true
		safeCharArray.foreach(char => octets(char) = true)
		octets
	}

	override protected def nextEscapeIndex(csq: CharSequence, start: Int, end: Int): Int = {
		var index: Int = start
		import scala.util.control.Breaks._
		breakable {
			while (index < end) {
				val c = csq.charAt(index)
				if (c >= safeOctets.length || !safeOctets(c)) break
				index += 1
			}
		}
		index
	}

	override def escape(string: String): String = {
		val length: Int = string.length
		var char: Char = Char.MinValue
		for (index <- 0 until length) {
			char = string.charAt(index)
			if (char >= safeOctets.length || !safeOctets(char)) escapeSlow(string, index)
		}
		string
	}

	override protected def escape(cp: Int): Option[Array[Char]] = {
		// We should never get negative values here but if we do it will throw an
		// IndexOutOfBoundsException, so at least it will get spotted.
		cp match {
			case _ if cp < safeOctets.length && safeOctets(cp) => None
			case _ if cp == ' ' && plusForSpace => Option(URI_ESCAPED_SPACE)
			case _ if cp <= 0x7F =>
				// Single byte UTF-8 characters
				// Start with "%--" and fill in the blanks
				val dest: Array[Char] = new Array[Char](3)
				dest(0) = '%'
				dest(2) = UPPER_HEX_DIGITS(cp & 0xF)
				dest(1) = UPPER_HEX_DIGITS(cp >>> 4)
				Option(dest)
			case _ if cp <= 0x7ff =>
				// Two byte UTF-8 characters [cp >= 0x80 && cp <= 0x7ff]
				// Start with "%--%--" and fill in the blanks
				val dest = new Array[Char](6)
				var newChar: Int = cp
				dest(0) = '%'
				dest(3) = '%'
				dest(5) = UPPER_HEX_DIGITS(newChar & 0xF)
				newChar >>>= 4
				dest(4) = UPPER_HEX_DIGITS(0x8 | (newChar & 0x3))
				newChar >>>= 2
				dest(2) = UPPER_HEX_DIGITS(newChar & 0xF)
				newChar >>>= 4
				dest(1) = UPPER_HEX_DIGITS(0xC | newChar)
				Option(dest)
			case _ if cp <= 0xffff =>
				// Three byte UTF-8 characters [cp >= 0x800 && cp <= 0xffff]
				// Start with "%E-%--%--" and fill in the blanks
				val dest = new Array[Char](9)
				var newChar: Int = cp
				dest(0) = '%'
				dest(1) = 'E'
				dest(3) = '%'
				dest(6) = '%'
				dest(8) = UPPER_HEX_DIGITS(newChar & 0xF)
				newChar >>>= 4
				dest(7) = UPPER_HEX_DIGITS(0x8 | (newChar & 0x3))
				newChar >>>= 2
				dest(5) = UPPER_HEX_DIGITS(newChar & 0xF)
				newChar >>>= 4
				dest(4) = UPPER_HEX_DIGITS(0x8 | (newChar & 0x3))
				newChar >>>= 2
				dest(2) = UPPER_HEX_DIGITS(cp)
				Option(dest)
			case _ if cp <= 0x10ffff =>
				// Four byte UTF-8 characters [cp >= 0xffff && cp <= 0x10ffff]
				// Start with "%F-%--%--%--" and fill in the blanks
				val dest = new Array[Char](12)
				var newChar: Int = cp
				dest(0) = '%'
				dest(1) = 'F'
				dest(3) = '%'
				dest(6) = '%'
				dest(9) = '%'
				dest(11) = UPPER_HEX_DIGITS(newChar & 0xF)
				newChar >>>= 4
				dest(10) = UPPER_HEX_DIGITS(0x8 | (newChar & 0x3))
				newChar >>>= 2
				dest(8) = UPPER_HEX_DIGITS(newChar & 0xF)
				newChar >>>= 4
				dest(7) = UPPER_HEX_DIGITS(0x8 | (newChar & 0x3))
				newChar >>>= 2
				dest(5) = UPPER_HEX_DIGITS(newChar & 0xF)
				newChar >>>= 4
				dest(4) = UPPER_HEX_DIGITS(0x8 | (newChar & 0x3))
				newChar >>>= 2
				dest(2) = UPPER_HEX_DIGITS(cp & 0x7)
				Option(dest)
			case _ => throw new IllegalArgumentException(s"Invalid unicode character value $cp")
		}
	}
}

object PercentEscaper {

	val SAFECHARS_URLENCODER: String = "-_.*"

	val SAFEPATHCHARS_URLENCODER: String = "/-_.!~*'()@:$&,;="

	val SAFEQUERYSTRINGCHARS_URLENCODER: String = "-_.!~*'()@:$,;/?:"

}