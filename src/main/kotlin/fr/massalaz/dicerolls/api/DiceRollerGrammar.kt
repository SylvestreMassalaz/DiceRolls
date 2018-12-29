package fr.massalaz.dicerolls.api

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.parser.Parser
import fr.massalaz.dicerolls.model.Dice
import fr.massalaz.dicerolls.model.DiceResult
import fr.massalaz.dicerolls.model.RollResult

class DiceRollerGrammar(
    private val randomGenerator: RandomGenerator,
    private val definitions: Map<String, Int>
) : Grammar<RollResult>() {

    private val num by token("\\d+")
    private val lPar by token("\\(")
    private val rPar by token("\\)")
    private val mul by token("\\*")
    private val div by token("/")
    private val minus by token("-")
    private val plus by token("\\+")
    private val ws by token("\\s+", ignore = true)
    private val diceD by token("d")
    private val reference by token("[a-zA-Z]\\w*")

    private val number by num use { text.toInt() }

    private val negativeNumber by (-minus * num) use { -text.toInt() }

    private val singleDice by -diceD * number map { listOf(Dice(it)) }

    private val multipleDice by number * -diceD * number map { data -> (1..data.t1).map { Dice(data.t2) } }

    private val dice: Parser<List<Dice>> by singleDice or multipleDice

    private val diceParser by dice use { roll(randomGenerator) }

    private val constParser by number or negativeNumber use { RollResult(this) }

    private val referenceParser by reference use { RollResult(get(text)) }

    private val term: Parser<RollResult> by
    diceParser or constParser or referenceParser or (-lPar * parser(this::rootParser) * skip(rPar))

    private val divMulChain by leftAssociative(term, div or mul use { type }) { a, op, b ->
        if (op == div) {
            a / b
        } else {
            a * b
        }

    }

    private val addMinChain by leftAssociative(divMulChain, plus or minus use { type }) { a, op, b ->
        if (op == plus) {
            a + b
        } else {
            a - b
        }
    }

    override val rootParser: Parser<RollResult> by addMinChain

    operator fun get(ref: String): Int = definitions[ref] ?: throw MissingReferenceException(ref)
}

open class DiceRollException(message: String) : Exception(message)

class MissingReferenceException(ref: String) : DiceRollException("Missing reference $ref")

fun List<Dice>.roll(random: RandomGenerator): RollResult =
    map { DiceResult(it, random(1, it.sideNumber)) }
        .let { results -> RollResult(results.map { it.result }.sum(), results) }