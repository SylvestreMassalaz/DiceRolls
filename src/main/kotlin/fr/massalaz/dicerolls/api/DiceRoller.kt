package fr.massalaz.dicerolls.api

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import fr.massalaz.dicerolls.model.RollResult
import kotlin.random.Random

object DiceRoller {
    fun rollDices(
        expr: String,
        definitions: Map<String, Int> = mapOf(),
        randomGenerator: RandomGenerator = { min, max -> Random.nextInt(min, max) }
    ): RollResult =
        DiceRollerGrammar(randomGenerator, definitions).parseToEnd(expr)
}