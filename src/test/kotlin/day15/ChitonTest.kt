package day15

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import utils.Coordinate
import utils.readInputLines

class ChitonTest {

    private val chiton = Chiton()
    private val testInput = """1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581""".lines()

	@Test
    fun `should find lowest risk path`() {
        val riskLevelMap = chiton.produceEdgeList(testInput)
        assertThat(chiton.findLowestRiskLevelPath(riskLevelMap, Coordinate(9, 9))).isEqualTo(40)
    }

	@Test
    fun `should find lowest risk path for full input`() {
        val riskLevelMap = chiton.produceEdgeList(readInputLines(15))
        assertThat(chiton.findLowestRiskLevelPath(riskLevelMap, Coordinate(99, 99))).isEqualTo(589)
    }

    @Test
    fun `should find lowest risk path for massive map`() {
        val riskLevelMap = chiton.produceEdgeList(chiton.produceMassiveMapInput(testInput))
        assertThat(chiton.findLowestRiskLevelPath(riskLevelMap, Coordinate(49, 49))).isEqualTo(315)
    }

    @Test
    fun `should find lowest risk path for massive map for full input`() {
        val riskLevelMap = chiton.produceEdgeList(chiton.produceMassiveMapInput(readInputLines(15)))
        assertThat(chiton.findLowestRiskLevelPath(riskLevelMap, Coordinate(49, 49))).isEqualTo(2885)
    }
}
