package day16

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

    abstract class Packet(val version: Int, typeID: Int, packetBody: PacketBody) {
        abstract fun getVersionSum(): Int

        companion object {
            fun readPacket(packetString: PacketBody): Packet {
                val version = packetString.takeBinToInt(3)
                val typeID = packetString.takeBinToInt(3)

                return when (typeID) {
                    4 -> LiteralPacket(version, typeID, packetString)
                    else -> OperatorPacket(version, typeID, packetString)
                }
            }
        }
    }

    class LiteralPacket(version: Int, typeId: Int, packetBody: PacketBody): Packet(version, typeId, packetBody) {
        val value: Long

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

    class OperatorPacket(version: Int, typeID: Int, packetBody: PacketBody): Packet(version, typeID, packetBody) {
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

}

typealias PacketBody = LinkedList<String>
fun PacketBody.take(n: Int) = PacketBody((0 until n).map { this.pop() })
fun PacketBody.takeToString(n: Int) = this.take(n).joinToString("")
fun PacketBody.takeBinToInt(n: Int) = this.take(n).joinToString("").toInt(2)

fun main() {
    val input = readInput(16)
    val packetDecoder = PacketDecoder()
    val binInput = packetDecoder.convertInputToBinary(input)
    val versionSum = PacketDecoder.Packet.readPacket(binInput).getVersionSum()
    println(versionSum)
}
