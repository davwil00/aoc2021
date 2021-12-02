package day02

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import utils.readInputLines

class DiveTest {

    private val dive = Dive()

	@Test
    fun `should calculate new position`() {
        val input = """forward 5
down 5
forward 8
up 3
down 8
forward 2"""
        val commands = dive.parseCommands(input.lines())
        val actual = dive.plotCourse(commands)
        assertThat(actual).isEqualTo(150)
    }

	@Test
    fun `should calculate new position for input`() {
        val input = readInputLines(2)
        val commands = dive.parseCommands(input)
        val actual = dive.plotCourse(commands)
        assertThat(actual).isEqualTo(1604850)
    }

	@Test
    fun `should calculate new position with aim`() {
        val input = """forward 5
down 5
forward 8
up 3
down 8
forward 2"""
        val commands = dive.parseCommands(input.lines())
        val actual = dive.plotCourseWithAim(commands)
        assertThat(actual).isEqualTo(900)
    }

	@Test
    fun `should calculate new position with aim for input`() {
        val input = readInputLines(2)
        val commands = dive.parseCommands(input)
        val actual = dive.plotCourseWithAim(commands)
        assertThat(actual).isEqualTo(1685186100)
    }
}
