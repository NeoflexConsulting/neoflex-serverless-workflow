package org.fluminous.jq.tokens

import org.fluminous.jq.{ input, Description, ParserException }
import org.fluminous.jq.input.{ Character, EOF }

case class Identifier(value: String) extends Token {
  def tryAppend(symbol: input.Symbol, position: Int): Either[ParserException, Option[Token]] = {
    symbol match {
      case EOF =>
        Right(None)
      case Character(c) if Token.whitespaceSymbols.contains(c) || SpecialSymbol.symbols.contains(c) || c == Root.char =>
        Right(None)
      case Character(c) =>
        Right(Some(Identifier(value :+ c)))
    }
  }
  override def toString: String    = value
  override val description: String = toString
}

object Identifier {
  implicit def typeDescription: Description[Identifier] = new Description[Identifier] {
    override val description: String = "identifier"
  }
}
