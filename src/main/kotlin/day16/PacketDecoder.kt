package day16

import utils.productOf
import utils.readInput
import utils.splitToString
import java.util.*

class PacketDecoder {

    fun convertInputToBinary(input: String) = LinkedList(input
        .map { hexToBin(it) }
        .joinToString("")
        .splitToString()
    )

    private fun hexToBin(hex: Char) = hex.digitToInt(16).toString(2).padStart(4, '0')

    abstract class Packet(val version: Int) {
        abstract fun getVersionSum(): Int

        abstract val value: Long

        companion object {
            fun readPacket(packetString: PacketBody): Packet {
                val version = packetString.takeBinToInt(3)
                val typeID = packetString.takeBinToInt(3)

                return when (typeID) {
                    0 -> SumPacket(version, packetString)
                    1 -> ProductPacket(version, packetString)
                    2 -> MinimumPacket(version, packetString)
                    3 -> MaximumPacket(version, packetString)
                    4 -> LiteralPacket(version, packetString)
                    5 -> GreaterThanPacket(version, packetString)
                    6 -> LessThanPacket(version, packetString)
                    7 -> EqualToPacket(version, packetString)
                    else -> throw IllegalArgumentException("Unknown type ID $typeID")
                }
            }
        }
    }

    class LiteralPacket(version: Int, packetBody: PacketBody): Packet(version) {
        override val value: Long

        init {
            with(StringBuilder()) {
                var prefix: String
                do {
                    prefix = packetBody.pop()
                    append(packetBody.takeToString(4))
                } while(prefix == "1")
                value = toString().toLong(2)
            }
        }

        override fun getVersionSum(): Int {
            return version
        }
    }

    abstract class OperatorPacket(version: Int, packetBody: PacketBody): Packet(version) {
        val packets: List<Packet>

        init {
            val lengthTypeId = packetBody.takeToString(1)

            when (lengthTypeId) {
                "0" -> {
                    val subPacketLength = packetBody.takeBinToInt(15)
                    val subpackets = mutableListOf<Packet>()
                    val subpacketsBody = packetBody.take(subPacketLength)
                    do {
                        val subpacket = readPacket(subpacketsBody)
                        subpackets.add(subpacket)
                    } while (subpacketsBody.any())
                    packets = subpackets
                }
                "1" -> {
                    val numberOfSubPackets = packetBody.takeBinToInt(11)
                    packets = (0 until numberOfSubPackets).map {
                        val subPacket = readPacket(packetBody)
                        subPacket
                    }
                }
                else -> throw IllegalArgumentException("Unknown length type ID")
            }
        }

        override fun getVersionSum(): Int {
            return packets.sumOf { it.getVersionSum() } + version
        }
    }

    class SumPacket(version: Int, packetBody: PacketBody): OperatorPacket(version, packetBody) {
        override val value: Long
            get() = packets.sumOf { it.value }
    }

    class ProductPacket(version: Int, packetBody: PacketBody): OperatorPacket(version, packetBody) {
        override val value: Long
            get() = packets.productOf { it.value }
    }

    class MinimumPacket(version: Int, packetBody: PacketBody): OperatorPacket(version, packetBody) {
        override val value: Long
            get() = packets.minOf { it.value }
    }

    class MaximumPacket(version: Int, packetBody: PacketBody): OperatorPacket(version, packetBody) {
        override val value: Long
            get() = packets.maxOf { it.value }
    }

    class GreaterThanPacket(version: Int, packetBody: PacketBody): OperatorPacket(version, packetBody) {
        override val value: Long
            get() = if (packets[0].value > packets[1].value) 1 else 0
    }

    class LessThanPacket(version: Int, packetBody: PacketBody): OperatorPacket(version, packetBody) {
        override val value: Long
            get() = if (packets[0].value < packets[1].value) 1 else 0
    }

    class EqualToPacket(version: Int, packetBody: PacketBody): OperatorPacket(version, packetBody) {
        override val value: Long
            get() = if (packets[0].value == packets[1].value) 1 else 0
    }
}

typealias PacketBody = LinkedList<String>
fun PacketBody.take(n: Int) = PacketBody((0 until n).map { this.pop() })
fun PacketBody.takeToString(n: Int) = this.take(n).joinToString("")
fun PacketBody.takeBinToInt(n: Int) = this.take(n).joinToString("").toInt(2)

fun main() {
    val input = readInput(16)
    val packetDecoder = PacketDecoder()
    val binInput = packetDecoder.convertInputToBinary(input)
    val packet = PacketDecoder.Packet.readPacket(binInput)
    val versionSum = packet.getVersionSum()
    println(versionSum)
    val value = packet.value
    println(value)
}
