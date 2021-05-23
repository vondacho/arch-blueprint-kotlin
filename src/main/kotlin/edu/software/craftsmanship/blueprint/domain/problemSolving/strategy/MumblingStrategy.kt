package edu.software.craftsmanship.blueprint.domain.problemSolving.strategy

/**
 * See https://bitbucket.org/noiasquad/mumbling_kata_elixir
 */
class MumblingStrategy {

    fun mumble(text: String): String {

        fun mumbleLetter(letter: Char, minorOccurrences: Int): String =
            letter.uppercase() + letter.lowercase().repeat(minorOccurrences)

        // with full recursion
        fun mumble(text: String, duplicates: Int): String =
            when {
                text.isEmpty() -> ""
                text.length == 1 -> mumbleLetter(text.first(), duplicates)
                else -> {
                    val head = text.first()
                    val tail = text.substring(1 until text.length)
                    "${mumbleLetter(head, duplicates)}-${mumble(tail, duplicates + 1)}"
                }
            }

        // with tail recursion
        fun mumble(text: String, duplicates: Int, acc: String): String =
            if (text.isEmpty()) acc
            else {
                val head = text.first()
                val tail = text.substring(1 until text.length)
                if (acc.isEmpty())
                    mumble(tail, duplicates + 1, mumbleLetter(head, duplicates))
                else
                    mumble(tail, duplicates + 1, "$acc-${mumbleLetter(head, duplicates)}")
            }

        return mumble(text, 0, "")
    }
}
