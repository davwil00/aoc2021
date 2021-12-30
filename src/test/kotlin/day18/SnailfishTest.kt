package day18

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import readTestInputLines

@Disabled
class SnailfishTest {

	@Test
    fun `add snailfish numbers`() {
        //[1,2] + [[3,4],5] becomes [[1,2],[[3,4],5]].
        val sn1 = SnailfishNumber.fromString("[1,2]")
        val sn2 = SnailfishNumber.fromString("[[3,4],5]")
        val expected = SnailfishNumber.fromString("[[1,2],[[3,4],5]]")
        assertThat(sn1 + sn2).isEqualTo(expected)
    }

//    @ParameterizedTest(name = "Parses {0}")
//    @MethodSource("getSnailfishNumbersToParse")
//    fun `parse snailfish numbers`(string: String, expected: SnailfishNumber) {
//        assertThat(SnailfishNumber.fromString(string)).isEqualTo(expected)
//    }

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
        "[[[[6,7],[0,7]],[[7,[6,7]],[0,21]]],[[2,[11,10]],[[0,8],[8,0]]]]|[[[[6,7],[0,7]],[[13,0],[7,21]]],[[2,[11,10]],[[0,8],[8,0]]]]"
        ], delimiter = '|'
    )
    fun `explode snailfish numbers`(input: String, expected: String) {
        val snailfishNumber = SnailfishNumber.fromString(input)
        val exploded = snailfishNumber.explode()
        assertThat(exploded).isTrue
        assertThat(snailfishNumber).isEqualTo(SnailfishNumber.fromString(expected))
    }

    @ParameterizedTest(name = "can split {0}")
    @CsvSource(
        "10,5,5",
        "11,5,6",
        "12,6,6"
    )
    fun `can split`(digit: Int, expectedLeft: Int, expectedRight: Int) {
        val splitNumber = SnailfishNumber.split(digit, SnailfishNumber(null))
        assertThat(splitNumber.leftValue).isEqualTo(expectedLeft)
        assertThat(splitNumber.rightValue).isEqualTo(expectedRight)
    }

    @Test
    fun split() {
        val snailfishNumber = SnailfishNumber.fromString("[[[[7,7],[7,8]],[[9,5],[8,0]]],[[[9,10],20],[8,[9,0]]]]")
        snailfishNumber.split()
        assertThat(snailfishNumber).isEqualTo(SnailfishNumber.fromString("[[[[7,7],[7,8]],[[9,5],[8,0]]],[[[9,[5,5]],20],[8,[9,0]]]]"))
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

    @Test
    fun `add and reduce2`() {
        val first = SnailfishNumber.fromString("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]")
        val second = SnailfishNumber.fromString("[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]")
        val added = first + second
        reduce(added)
        val expected = SnailfishNumber.fromString("[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]")
        assertThat(added).isEqualTo(expected)
    }

    @Test
    fun `add and reduce3`() {
        val first = SnailfishNumber.fromString("[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]")
        val second = SnailfishNumber.fromString("[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]")
        val added = first + second
        reduce(added)
        val expected = SnailfishNumber.fromString("[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]")
        assertThat(added).isEqualTo(expected)
    }

    @Test
    fun `add and reduce4`() {
        val first = SnailfishNumber.fromString("[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]")
        val second = SnailfishNumber.fromString("[7,[5,[[3,8],[1,4]]]]")
        val added = first + second
        reduce(added)
        val expected = SnailfishNumber.fromString("[[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]")
        assertThat(added).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "testInput1.txt|[[[[3,0],[5,3]],[4,4]],[5,5]]",
        "testInput2.txt|[[[[5,0],[7,4]],[5,5]],[6,6]]",
        "testInput.txt|[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"
    , delimiter = '|')
    fun `add and reduce list`(source: String, expected: String) {
        val result = addAndReduceList(readTestInputLines(18, source))
        val expectedSnailfishNumber = SnailfishNumber.fromString(expected)
        assertThat(result).isEqualTo(expectedSnailfishNumber)
    }

    @ParameterizedTest
    @CsvSource(
        "[[1,2],[[3,4],5]]|143",
        "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]|3488"
    , delimiter = '|')
    fun getMagnitude(input: String, expected: Long) {
        assertThat(SnailfishNumber.fromString(input).getMagnitude()).isEqualTo(expected)
    }

    companion object {
//        @JvmStatic
//        fun getSnailfishNumbersToParse() = listOf(
//            Arguments.of("[1,2]", SnailfishNumber(0, 1, 2)),
//            Arguments.of("[[1,2],3]", SnailfishNumber(0, SnailfishNumber(1, 1, 2), 3)),
//            Arguments.of("[9,[8,7]]", SnailfishNumber(0, 9, SnailfishNumber(1, 8, 7))),
//            Arguments.of("[[1,9],[8,5]]", SnailfishNumber(0, SnailfishNumber(1, 1, 9), SnailfishNumber(1,8, 5))),
//            Arguments.of("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]",
//                SnailfishNumber(0,
//                    SnailfishNumber(1,
//                        SnailfishNumber(2,
//                            SnailfishNumber(3, 1, 2),
//                            SnailfishNumber(3, 3, 4)
//                        ),
//                        SnailfishNumber(2,
//                            SnailfishNumber(3, 5, 6),
//                            SnailfishNumber(3, 7, 8)
//                        )
//                    ),
//                    9
//                )
//            ),
//            Arguments.of("[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]",
//                SnailfishNumber(0,
//                    SnailfishNumber(1,
//                        SnailfishNumber(2,
//                            9,
//                            SnailfishNumber(3, 3, 8)
//                        ),
//                        SnailfishNumber(2,
//                            SnailfishNumber(3, 0, 9),
//                            6
//                        )
//                    ),
//                    SnailfishNumber(1,
//                        SnailfishNumber(2,
//                            SnailfishNumber(3, 3, 7),
//                            SnailfishNumber(3, 4, 9)
//                        ),
//                        3
//                    )
//                )
//            ),
//            Arguments.of("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]",
//                SnailfishNumber(0,
//                    SnailfishNumber(1,
//                        SnailfishNumber(2,
//                            SnailfishNumber(3,1, 3),
//                            SnailfishNumber(3,5, 3)
//                        ),
//                        SnailfishNumber(2,
//                            SnailfishNumber(3, 1, 3),
//                            SnailfishNumber(3, 8, 7)
//                        )
//                    ),
//                    SnailfishNumber(1,
//                        SnailfishNumber(2,
//                            SnailfishNumber(3, 4, 9),
//                            SnailfishNumber(3, 6, 9)
//                        ),
//                        SnailfishNumber(2,
//                            SnailfishNumber(3, 8, 2),
//                            SnailfishNumber(3, 7, 3)
//                        )
//                    )
//                )
//            ),
//            Arguments.of("[[[[0,7],4],[15,[0,13]]],[1,1]]",
//                SnailfishNumber(0,
//                    SnailfishNumber(1,
//                        SnailfishNumber(2,
//                            SnailfishNumber(3, 0, 7),
//                            4
//                        ),
//                        SnailfishNumber(2,
//                            15,
//                            SnailfishNumber(3, 0, 13)
//                        )
//                    ),
//                    SnailfishNumber(1, 1, 1)
//                ))
//        )

        @JvmStatic
        fun explode(): List<Arguments> = listOf(

        )
    }
}