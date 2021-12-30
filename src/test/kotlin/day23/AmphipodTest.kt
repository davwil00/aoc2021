package day23

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import readTestInputLines
import utils.readInputLines

class AmphipodTest {

    @Test
    fun `calculate cost`() {
        assertThat(Move(AmberAmphipod(), Room(AmberAmphipod::class, 1), Hallway(3)).calculateCost())
            .isEqualTo(2)
        assertThat(Move(BronzeAmphipod(), Room(AmberAmphipod::class, 1), Hallway(3)).calculateCost())
            .isEqualTo(20)
        assertThat(Move(AmberAmphipod(), Room(AmberAmphipod::class, 2), Hallway(0)).calculateCost())
            .isEqualTo(4)
        assertThat(Move(AmberAmphipod(), Room(AmberAmphipod::class, 2), Hallway(10)).calculateCost())
            .isEqualTo(10)
    }

    @Test
    fun calculateTotalCost() {
        val burrowMap = BurrowMap.fromString(readTestInputLines(23))
        assertThat(findPath(burrowMap)).isEqualTo(12521)
    }

    @Test
    fun `calculateTotalCost for part 2`() {
        val burrow = BurrowMap.fromString(readTestInputLines(23, "input-part2.txt"), 4)
        assertThat(findPath(burrow)).isEqualTo(44169)
    }

    @Test
    fun `calculateTotalCost for full input`() {
        val burrow = BurrowMap.fromString(readInputLines(23))
        assertThat(findPath(burrow)).isEqualTo(13558)
    }

    @Test
    fun `calculateTotalCost for full input - part 2`() {
        val burrow = BurrowMap.fromString(readInputLines(23, "input-part2.txt"), 4)
        assertThat(findPath(burrow)).isGreaterThan(56982)
    }

    @Test
    fun isPathToHallwayLocationNotBlocked() {
        val input = """#############
#...A.......#
###C#B#.#D###
  #C#D#A#B#
  #########
""".lines()
        val burrow = BurrowMap.fromString(input)
        assertThat(burrow.isPathToHallwayLocationNotBlocked(Room(AmberAmphipod::class, 1), Hallway(5))).isFalse
    }

    @Test
    fun isPathToRoomLocationNotBlocked() {
        val input = """#############
#.......D.D.#
###.#B#C#.###
  #A#B#C#A#
  #########
""".lines()
        val burrow = BurrowMap.fromString(input)
        assertThat(burrow.getBottommostValidRoomOrNull(DesertAmphipod::class)).isNull()
//        assertThat(burrow.getBottommostValidRoomOrNull(Hallway(9), Room(DesertAmphipod::class, 1))).isFalse
    }

    @Test
    fun debug() {
        val input = """#############
#...A.......#
###C#B#.#D###
  #C#D#A#B#
  #########
""".lines()
        val burrow = BurrowMap.fromString(input)
        burrow.getAvailableMoves().forEach { println(it) }
    }
}
