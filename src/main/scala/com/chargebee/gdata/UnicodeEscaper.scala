package com.chargebee.gdata

// TODO Add JavaDoc
trait UnicodeEscaper extends Escaper {

	private val DEST_PAD: Int = 32

	protected def escape(cp: Int): Option[Array[Char]]

	protected def nextEscapeIndex(csq: CharSequence, start: Int, end: Int): Int = {
		var index: Int = start
		import scala.util.control.Breaks._
		breakable {
			while (index < end) {
				val codePoint = codePointAt(csq, index, end)
				if (codePoint < 0 || escape(codePoint) != null) break
				index = index + (if(Character.isSupplementaryCodePoint(codePoint)) 2 else 1)
			}
		}
		index
	}

	def escape(string: String): String = {
		def escapeIter(end: Int): String = {
			val index: Int = nextEscapeIndex(string, 0, end)
			if (index == end) string else escapeSlow(string, index)
		}
		escapeIter(string.length)
	}

	protected final def escapeSlow(string: String, index: Int): String = {
		val end: Int = string.length
		var dest: Array[Char] = DEST_TL.get
		var destIndex: Int = 0
		var unescapedChunkStart: Int = 0
		var newIndex: Int = index
		// Better to not use recursive function, we don't know the length of String
		// that will be used, and we don't want to have StackOverFlow Exception
		while (newIndex < end) {
			val cp: Int = codePointAt(string, newIndex, end)
			if (cp < 0) throw new IllegalArgumentException("Trailing high surrogate at end of input")
			val escaped: Option[Array[Char]] = escape(cp)
			if (escaped.isDefined) {
				val charsSkipped: Int = newIndex - unescapedChunkStart
				val escapedLength: Int = escaped.get.length
				// This is the size needed to add the replacement, not the full
				// size needed by the string.  We only regrow when we absolutely must.
				val sizeNeeded = destIndex + charsSkipped + escapedLength
				if (dest.length < sizeNeeded) {
					dest = growBuffer(dest, destIndex, sizeNeeded + (end - newIndex) + DEST_PAD)
				}
				// If we have skipped any characters, we need to copy them now.
				if (charsSkipped > 0) {
					string.getChars(unescapedChunkStart, newIndex, dest, destIndex)
					destIndex += charsSkipped
				}
				if (escapedLength > 0) {
					System.arraycopy(escaped, 0, dest, destIndex, escapedLength)
					destIndex += escapedLength
				}
			}
			unescapedChunkStart = newIndex + (if (Character.isSupplementaryCodePoint(cp)) 2 else 1)
			newIndex = nextEscapeIndex(string, unescapedChunkStart, end)
		}

		// Process trailing unescaped characters - no need to account for escaped
		// length or padding the allocation.
		val charsSkipped = end - unescapedChunkStart
		if (charsSkipped > 0) {
			val endIndex = destIndex + charsSkipped
			if (dest.length < endIndex) dest = growBuffer(dest, destIndex, endIndex)
			string.getChars(unescapedChunkStart, end, dest, destIndex)
			destIndex = endIndex
		}
		new String(dest, 0, destIndex)
	}

	def escape(appendable: Appendable): Appendable = new Appendable {

		private[gdata] var pendingHighSurrogate: Int = -1
		private[gdata] val decodedChars: Array[Char] = new Array[Char](2)

		override def append(csq: CharSequence): Appendable = append(csq, 0, csq.length)

		override def append(csq: CharSequence, start: Int, end: Int): Appendable = {
			var index: Int = start
			if (index < end) {
				// This is a little subtle: index must never reference the middle of a
				// surrogate pair but unescapedChunkStart can. The first time we enter
				// the loop below it is possible that index != unescapedChunkStart.
				var unescapedChunkStart = index
				if (pendingHighSurrogate != -1) {
					// Our last append operation ended halfway through a surrogate pair
					// so we have to do some extra work first.
					val c = csq.charAt({
						index += 1; index - 1
					})
					if (!Character.isLowSurrogate(c)) throw new IllegalArgumentException("Expected low surrogate character but got " + c)
					val escaped = escape(Character.toCodePoint(pendingHighSurrogate.toChar, c))
					if (escaped.isDefined) {
						// Emit the escaped character and adjust unescapedChunkStart to
						// skip the low surrogate we have consumed.
						outputChars(escaped)
						unescapedChunkStart += 1
					} else {
						// Emit pending high surrogate (unescaped) but do not modify
						// unescapedChunkStart as we must still emit the low surrogate.
						appendable.append(pendingHighSurrogate.toChar)
					}
					pendingHighSurrogate = -1
				}
				import scala.util.control.Breaks._
				breakable {
					while (true) {
						// Find and append the next subsequence of unescaped characters.
						index = nextEscapeIndex(csq, index, end)
						if (index > unescapedChunkStart) appendable.append(csq, unescapedChunkStart, index)
						if (index == end) break
						// If we are not finished, calculate the next code point.
						val cp = codePointAt(csq, index, end)
						if (cp < 0) {
							// Our sequence ended half way through a surrogate pair so just
							// record the state and exit.
							pendingHighSurrogate = -cp
							break
						}
						// Escape the code point and output the characters.
						val escaped: Option[Array[Char]] = escape(cp)
						if (escaped.isDefined) outputChars(escaped)
						else {
							// This shouldn't really happen if nextEscapeIndex is correct but
							// we should cope with false positives.
							outputChars(decodedChars, Character.toChars(cp, decodedChars, 0))
						}
						// Update our index past the escaped character and continue.
						index += (if (Character.isSupplementaryCodePoint(cp)) 2 else 1)
						unescapedChunkStart = index
					}
				}
			}
			this
		}

		override def append(char: Char): Appendable = {
			if (pendingHighSurrogate != -1) {
				// Our last append operation ended halfway through a surrogate pair
				// so we have to do some extra work first.
				if (!Character.isLowSurrogate(char)) throw new IllegalArgumentException(s"Expected low surrogate character but got '$char' with value ${char.toInt}")
				val escaped = escape(Character.toCodePoint(pendingHighSurrogate.toChar, char))
				if (escaped.isDefined) outputChars(escaped)
				else appendable.append(pendingHighSurrogate.toChar).append(char)
				pendingHighSurrogate = -1
			} else if (Character.isHighSurrogate(char)) {
				// This is the start of a (split) surrogate pair.
				pendingHighSurrogate = char
			} else {
				if (Character.isLowSurrogate(char)) throw new IllegalArgumentException(s"Unexpected low surrogate character '$char' with value ${char.toInt}")
				// This is a normal (non surrogate) char.
				val escaped: Option[Array[Char]] = escape(char)
				if (escaped.isDefined) outputChars(escaped)
				else appendable.append(char)
			}
			this
		}

		private[Appendable] def outputChars(chars: Array[Char], length: Int): Unit = {
			for (i <- chars.indices) appendable.append(chars(i))
		}

		private[Appendable] def outputChars(chars: Option[Array[Char]]): Unit = {
			for (i <- chars.get.indices) appendable.append(chars.get(i))
		}
	}

	protected final def codePointAt(seq: CharSequence, index: Int, end: Int): Int = {
		var newIndex: Int = index
		if (newIndex < end) {
			val c1: Char = seq.charAt({newIndex += 1; newIndex - 1})
			if (c1 < Character.MIN_HIGH_SURROGATE || c1 > Character.MAX_LOW_SURROGATE)
				// Fast path (first test is probably all we need to do)
				c1
			else if (c1 <= Character.MAX_HIGH_SURROGATE) {
				// If the high surrogate was the last character, return its inverse
				if (newIndex == end) -c1
				// Otherwise look for the low surrogate following it
				val c2 = seq.charAt(index)
				if (Character.isLowSurrogate(c2)) Character.toCodePoint(c1, c2)
				throw new IllegalArgumentException(s"Expected low surrogate but got char '$c2' with value ${c2.toInt} at index $index")
			}
			throw new IllegalArgumentException(s"Unexpected low surrogate character '$c1' with value ${c1.toInt} at index ${index - 1}")
		}
		throw new IndexOutOfBoundsException("Index exceeds specified range")
	}

	private[gdata] def growBuffer(dest: Array[Char], index: Int, size: Int): Array[Char] = {
		val copy: Array[Char] = new Array[Char](size)
		if (index > 0) Array.copy(dest, 0, copy, 0, index)
		copy
	}

	private lazy val DEST_TL = new ThreadLocal[Array[Char]]() {
		override protected def initialValue = new Array[Char](1024)
	}
}