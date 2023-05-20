package edu.obya.blueprint.problemsolving.domain

/**
 * See # https://bitbucket.org/socraagile/split-string
 */
class SplitStringStrategy {

    fun split(text: String, amplitude: Int = 1): Array<String> {

        fun fill(text: String, amplitude: Int) = text + "_".repeat(amplitude - text.length)

        fun split(text: String, acc: Array<String>): Array<String> =
            when {
                text.isEmpty() -> acc
                text.length < amplitude -> split("", acc + fill(text, amplitude))
                else -> split(text.substring(amplitude until text.length), acc + text.substring(0 until amplitude))
            }

        return split(text, emptyArray())
    }
}
