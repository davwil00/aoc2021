package day23

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import readTestInputLines
import utils.readInputLines

class AmphipodTest {

    private val burrow = Burrow.fromString(readTestInputLines(23))

	@Test
    fun `get first moves`() {
        val availableMoves = produceEdgeList(burrow)
        println(availableMoves)
    }

    @Test
    fun `calculate cost`() {
        assertThat(Move(AmberAmphipod(1), Location.ROOM_1_TOP, Location.HALLWAY_3).calculateCost())
            .isEqualTo(2)
        assertThat(Move(BronzeAmphipod(2), Location.ROOM_1_TOP, Location.HALLWAY_3).calculateCost())
            .isEqualTo(20)
        assertThat(Move(AmberAmphipod(2), Location.ROOM_1_BOTTOM, Location.HALLWAY_0).calculateCost())
            .isEqualTo(4)
        assertThat(Move(AmberAmphipod(2), Location.ROOM_1_BOTTOM, Location.HALLWAY_10).calculateCost())
            .isEqualTo(10)
    }

    @Test
    fun calculateTotalCost() {
        assertThat(findPath(burrow)).isEqualTo(12521)
    }

    @Test
    fun `calculateTotalCost for part 2`() {
        val burrow = Burrow.fromString(readTestInputLines(23, "input-part2.txt"))
        assertThat(findPath(burrow)).isEqualTo(44169)
    }

    @Test
    fun `calculateTotalCost for full input`() {
        val burrow = Burrow.fromString(readInputLines(23))
        assertThat(findPath(burrow)).isEqualTo(13558)
    }

    @Test
    fun thing() {
        val endState = sequenceOf(
            Location.ROOM_1_TOP to AmberAmphipod(1),
            Location.ROOM_1_BOTTOM to AmberAmphipod(1),
            Location.ROOM_2_TOP to BronzeAmphipod(1),
            Location.ROOM_2_BOTTOM to BronzeAmphipod(1),
            Location.ROOM_3_TOP to CopperAmphipod(1),
            Location.ROOM_3_BOTTOM to CopperAmphipod(1),
            Location.ROOM_4_TOP to DesertAmphipod(1),
            Location.ROOM_4_BOTTOM to DesertAmphipod(1),
        ).toMap(BurrowMap())

        val endState2 = sequenceOf(
            Location.ROOM_1_TOP to AmberAmphipod(1),
            Location.ROOM_1_BOTTOM to AmberAmphipod(2),
            Location.ROOM_2_TOP to BronzeAmphipod(3),
            Location.ROOM_2_BOTTOM to BronzeAmphipod(4),
            Location.ROOM_3_TOP to CopperAmphipod(5),
            Location.ROOM_3_BOTTOM to CopperAmphipod(6),
            Location.ROOM_4_TOP to DesertAmphipod(7),
            Location.ROOM_4_BOTTOM to DesertAmphipod(8),
        ).toMap(BurrowMap())

        assertThat(endState == endState2).isTrue
    }
}
