package day16

import day16.PacketDecoder.LiteralPacket
import day16.PacketDecoder.OperatorPacket
import day16.PacketDecoder.Packet.Companion.readPacket
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import utils.readInput

class PacketDecoderTest {

    private val packetDecoder = PacketDecoder()

	@Test
    fun `parse small literal packet`() {
        val testInput = "D2FE28"
        val binInput = packetDecoder.convertInputToBinary(testInput)
        val packet = readPacket(binInput)
        assertThat(packet).isInstanceOf(LiteralPacket::class.java)
        assertThat((packet as LiteralPacket).value).isEqualTo(2021)
    }

	@Test
    fun `parse small operator packet`() {
        val testInput = "38006F45291200"
        val binInput = packetDecoder.convertInputToBinary(testInput)
        val packet = readPacket(binInput)
        assertThat(packet is OperatorPacket).isTrue
        if (packet is OperatorPacket) {
            assertThat(packet.packets).hasSize(2)
            assertThat((packet.packets[0] as LiteralPacket).value).isEqualTo(10)
            assertThat((packet.packets[1] as LiteralPacket).value).isEqualTo(20)
        }
    }

	@Test
    fun `parse another operator packet`() {
        val testInput = "EE00D40C823060"
        val binInput = packetDecoder.convertInputToBinary(testInput)
        val packet = readPacket(binInput)
        assertThat(packet is OperatorPacket).isTrue
        if (packet is OperatorPacket) {
            assertThat(packet.packets).hasSize(3)
            assertThat((packet.packets[0] as LiteralPacket).value).isEqualTo(1)
            assertThat((packet.packets[1] as LiteralPacket).value).isEqualTo(2)
            assertThat((packet.packets[2] as LiteralPacket).value).isEqualTo(3)
        }
    }

	@ParameterizedTest
    @CsvSource(
        "8A004A801A8002F478,16",
        "620080001611562C8802118E34,12",
        "C0015000016115A2E0802F182340,23",
        "A0016C880162017C3686B18A3D4780,31"
    )
    fun `getPacketSum`(testInput: String, expectedSum: Int) {
        val binInput = packetDecoder.convertInputToBinary(testInput)
        val packet = readPacket(binInput)
        assertThat(packet.getVersionSum()).isEqualTo(expectedSum)
    }

    @Test
    fun `getPacketSum for full input`() {
        val binInput = packetDecoder.convertInputToBinary(readInput(15))
        val packet = readPacket(binInput)
        assertThat(packet.getVersionSum()).isEqualTo(886)
    }
}
