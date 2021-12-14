package day13

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TransparentOrigamiTest {

    private val transparentOrigami = TransparentOrigami()
    private val testInput = """6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5"""

    @Test
    fun `should fold along y axis`() {
        val input = transparentOrigami.readInstructions(testInput)
        val actual = transparentOrigami.fold(input.grid, input.instructions[0])
        assertThat(actual).hasSize(17)
    }

    @Test
    fun `should fold along x axis`() {
        val input = transparentOrigami.readInstructions(testInput)
        val first = transparentOrigami.fold(input.grid, input.instructions[0])
        val actual = transparentOrigami.fold(first, input.instructions[1])
        assertThat(actual).hasSize(16)
    }
}
