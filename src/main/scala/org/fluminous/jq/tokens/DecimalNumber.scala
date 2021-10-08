package org.fluminous.jq.tokens

import io.circe.Json
import org.fluminous.jq.filter.Filter
import org.fluminous.jq.{Description, EvaluationException, ParserException, input}
import org.fluminous.jq.input.{Character, EOF}
import org.fluminous.jq.tokens.symbolic.AtomicToken

case class DecimalNumber(override val position: Int, value: String) extends Token with Filter {
  def tryAppend(symbol: input.Symbol, symbolPosition: Int): Either[ParserException, AppendResult] = {
    symbol match {
      case Character(c) if Token.whitespaceSymbols.contains(c) || AtomicToken.symbols.contains(c) =>
        Right(TokenConstructed)
      case Character(c) if c.isDigit =>
        Right(
          TokenUpdated(DecimalNumber(position, value :+ c))
        )
      case EOF =>
        Right(TokenConstructed)
      case _ =>
        Left(ParserException(symbolPosition, "Identifier could not start with number. Try to surround it by quotes"))
    }
  }
  override def toString: String    = value
  override val description: String = toString

  override def transform(input: Json): Either[EvaluationException, Json] = Right(Json.fromBigDecimal(BigDecimal(value)))
}

object DecimalNumber {
  implicit def typeDescription: Description[DecimalNumber] = new Description[DecimalNumber] {
    override val description: String = "decimal number"
  }
}
