package org.fluminous.jq.filter.pattern

import cats.data.{NonEmptyList, Validated}
import org.fluminous.jq.{Description, Expression, Tokenizer}
import org.fluminous.jq.filter.pattern.dsl.MatchFailure

case class PatternCases(name: String, cases: List[PatternCase])

case class MatcherInput(tokenizer: Tokenizer, stack: NonEmptyList[Expression])
case class MatcherOutput(tokenizer: Tokenizer, result: Validated[MatchFailure, List[Expression]])

case class PatternCase(length: Int, patternCase: MatcherInput => MatcherOutput)

object PatternCases {
  def apply[T <: Expression: Description](patternCases: PatternCase*): PatternCases =
    PatternCases(implicitly[Description[T]].description, patternCases.toList)
}
