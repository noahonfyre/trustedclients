package com.nyronium.data

import net.minecraft.network.PacketByteBuf

data class ModListEntry(
    val id: String,
    val name: String,
) {
    companion object {
        fun write(buf: PacketByteBuf, data: ModListEntry) {
            buf.writeString(data.id)
            buf.writeString(data.name)
        }

        fun read(buf: PacketByteBuf): ModListEntry {
            val id: String = buf.readString()
            val name: String = buf.readString()
            return ModListEntry(id, name)
        }
    }
}