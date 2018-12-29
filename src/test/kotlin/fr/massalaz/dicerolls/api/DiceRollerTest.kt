package fr.massalaz.dicerolls.api

import fr.massalaz.dicerolls.model.Dice
import fr.massalaz.dicerolls.model.DiceResult
import fr.massalaz.dicerolls.model.RollResult
import org.junit.Assert.*
import org.junit.Test

class DiceRollerTest {

    companion object {
        val minValueGen: RandomGenerator = { min, _ -> min }
    }

    @Test
    fun `simple expression`() {
        assertEquals(
            "Constant only doesn't match",
            RollResult(1),
            DiceRoller.rollDices("1")
        )
    }

    @Test
    fun `reference`() {
        assertEquals(
            "Reference should match",
            RollResult(1),
            DiceRoller.rollDices("un", mapOf("un" to 1))
        )
    }

    @Test(expected = MissingReferenceException::class)
    fun `missing expression`() {
        DiceRoller.rollDices("1d100 - missing")
    }

    @Test
    fun `dice rolls`() {
        assertEquals(
            "Dice rolls should match min for simple version",
            RollResult(1, listOf(DiceResult(Dice(6), 1))),
            DiceRoller.rollDices("d6", randomGenerator = minValueGen)
        )

        assertEquals(
            "Dice rolls should match min for complex version",
            RollResult(1, listOf(DiceResult(Dice(6), 1))),
            DiceRoller.rollDices("1d6", randomGenerator = minValueGen)
        )
    }

    @Test
    fun `Simple arithmetics`() {
        assertEquals(
            RollResult(20, listOf()),
            DiceRoller.rollDices("20 + (10 - 2 * 2) - 6")
        )
        assertEquals(
            RollResult(20, listOf()),
            DiceRoller.rollDices("10+10")
        )

        assertEquals(
            RollResult(8, listOf()),
            DiceRoller.rollDices("10-2")
        )

        assertEquals(
            RollResult(20, listOf()),
            DiceRoller.rollDices("10-2*2+14")
        )

        assertEquals(
            RollResult(20, listOf()),
            DiceRoller.rollDices("20+(10-2*2)-6")
        )

        assertEquals(
            RollResult(20, listOf()),
            DiceRoller.rollDices("(10-2*2)-6+20")
        )

        assertEquals(
            RollResult(20, listOf()),
            DiceRoller.rollDices("2 * (2 + 3) * (3 - 1)")
        )
    }

    @Test
    fun `Complex expression`() {
        assertEquals(
            "Must match",
            RollResult(0, listOf(DiceResult(Dice(20), 1))),
            DiceRoller.rollDices("d20 - 1", randomGenerator = minValueGen)
        )
        assertEquals(
            "Complex expression should match",
            RollResult(100, listOf(DiceResult(Dice(100), 1))),
            DiceRoller.rollDices("(1d100-1+1)*100", randomGenerator = minValueGen)
        )
    }
}