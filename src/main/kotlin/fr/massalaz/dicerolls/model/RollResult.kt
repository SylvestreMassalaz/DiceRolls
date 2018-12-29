package fr.massalaz.dicerolls.model

data class RollResult(val total: Int, val diceRolls: List<DiceResult> = listOf()) {
    operator fun plus(other: RollResult): RollResult =
            RollResult(total + other.total, diceRolls + other.diceRolls)

    operator fun minus(other: RollResult): RollResult =
        RollResult(total - other.total, diceRolls + other.diceRolls)

    operator fun div(other: RollResult): RollResult =
            RollResult(total / other.total, diceRolls + other.diceRolls)

    operator fun times(other: RollResult): RollResult =
            RollResult(total * other.total, diceRolls + other.diceRolls)

}