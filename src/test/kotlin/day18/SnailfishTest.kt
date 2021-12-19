package day18

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.exp

class SnailfishTest {

	@Test
    fun `add snailfish numbers`() {
        //[1,2] + [[3,4],5] becomes [[1,2],[[3,4],5]].
        assertThat(SnailfishNumber(0, 1, 2) + SnailfishNumber(0, SnailfishNumber(1,3, 4), 5))
            .isEqualTo(SnailfishNumber(1, SnailfishNumber(1, 1, 2), SnailfishNumber(1, SnailfishNumber(2, 3, 4), 5)))
    }

    @ParameterizedTest(name = "Parses {0}")
    @MethodSource("getSnailfishNumbersToParse")
    fun `parse snailfish numbers`(string: String, expected: SnailfishNumber) {
        assertThat(SnailfishNumber.fromString(string)).isEqualTo(expected)
    }

    @ParameterizedTest(name = "explodes {0}")
    @CsvSource(value = [
        "[[[[[9,8],1],2],3],4]|[[[[0,9],2],3],4]",
        "[7,[6,[5,[4,[3,2]]]]]|[7,[6,[5,[7,0]]]]",
        "[[[[[9,8],1],2],3],4]|[[[[0,9],2],3],4]",
        "[7,[6,[5,[4,[3,2]]]]]|[7,[6,[5,[7,0]]]]",
        "[[6,[5,[4,[3,2]]]],1]|[[6,[5,[7,0]]],3]",
        "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]|[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
        "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]|[[3,[2,[8,0]]],[9,[5,[7,0]]]]",
        "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]|[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
        ], delimiter = '|'
    )
    fun `explode snailfish numbers`(input: String, expected: String) {
        val snailfishNumber = SnailfishNumber.fromString(input)
        val exploded = snailfishNumber.explode()
        assertThat(exploded).isTrue
        assertThat(snailfishNumber).isEqualTo(SnailfishNumber.fromString(expected))
    }

    @ParameterizedTest(name = "can split {0}")
    @MethodSource("split")
    fun `can split`(digit: Int, expected: SnailfishNumber) {
        assertThat(SnailfishNumber.split(digit, 1)).isEqualTo(expected)
    }

    @Test
    fun `add and reduce`() {
        val first = SnailfishNumber.fromString("[[[[4,3],4],4],[7,[[8,4],9]]]")
        val second = SnailfishNumber.fromString("[1,1]")
        val added = first + second
        reduce(added)
        val expected = SnailfishNumber.fromString("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")
        assertThat(added).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun getSnailfishNumbersToParse() = listOf(
            Arguments.of("[1,2]", SnailfishNumber(0, 1, 2)),
            Arguments.of("[[1,2],3]", SnailfishNumber(0, SnailfishNumber(1, 1, 2), 3)),
            Arguments.of("[9,[8,7]]", SnailfishNumber(0, 9, SnailfishNumber(1, 8, 7))),
            Arguments.of("[[1,9],[8,5]]", SnailfishNumber(0, SnailfishNumber(1, 1, 9), SnailfishNumber(1,8, 5))),
            Arguments.of("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]",
                SnailfishNumber(0,
                    SnailfishNumber(1,
                        SnailfishNumber(2,
                            SnailfishNumber(3, 1, 2),
                            SnailfishNumber(3, 3, 4)
                        ),
                        SnailfishNumber(2,
                            SnailfishNumber(3, 5, 6),
                            SnailfishNumber(3, 7, 8)
                        )
                    ),
                    9
                )
            ),
            Arguments.of("[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]",
                SnailfishNumber(0,
                    SnailfishNumber(1,
                        SnailfishNumber(2,
                            9,
                            SnailfishNumber(3, 3, 8)
                        ),
                        SnailfishNumber(2,
                            SnailfishNumber(3, 0, 9),
                            6
                        )
                    ),
                    SnailfishNumber(1,
                        SnailfishNumber(2,
                            SnailfishNumber(3, 3, 7),
                            SnailfishNumber(3, 4, 9)
                        ),
                        3
                    )
                )
            ),
            Arguments.of("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]",
                SnailfishNumber(0,
                    SnailfishNumber(1,
                        SnailfishNumber(2,
                            SnailfishNumber(3,1, 3),
                            SnailfishNumber(3,5, 3)
                        ),
                        SnailfishNumber(2,
                            SnailfishNumber(3, 1, 3),
                            SnailfishNumber(3, 8, 7)
                        )
                    ),
                    SnailfishNumber(1,
                        SnailfishNumber(2,
                            SnailfishNumber(3, 4, 9),
                            SnailfishNumber(3, 6, 9)
                        ),
                        SnailfishNumber(2,
                            SnailfishNumber(3, 8, 2),
                            SnailfishNumber(3, 7, 3)
                        )
                    )
                )
            ),
            Arguments.of("[[[[0,7],4],[15,[0,13]]],[1,1]]",
                SnailfishNumber(0,
                    SnailfishNumber(1,
                        SnailfishNumber(2,
                            SnailfishNumber(3, 0, 7),
                            4
                        ),
                        SnailfishNumber(2,
                            15,
                            SnailfishNumber(3, 0, 13)
                        )
                    ),
                    SnailfishNumber(1, 1, 1)
                ))
        )

        @JvmStatic
        fun split(): List<Arguments> = listOf(
            Arguments.of(10, SnailfishNumber(1, 5, 5)),
            Arguments.of(11, SnailfishNumber(1, 5, 6)),
            Arguments.of(12, SnailfishNumber(1, 6, 6))
        )

        @JvmStatic
        fun explode(): List<Arguments> = listOf(

        )
    }
}