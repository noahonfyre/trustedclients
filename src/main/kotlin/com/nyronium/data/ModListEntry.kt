package com.nyronium.data

import net.minecraft.network.FriendlyByteBuf

data class ModListEntry(
    val id: String,
    val name: String,
) {
    companion object {
        fun write(buf: FriendlyByteBuf, data: ModListEntry) {
            buf.writeUtf(data.id)
            buf.writeUtf(data.name)
        }

        fun read(buf: FriendlyByteBuf): ModListEntry {
            val id: String = buf.readUtf()
            val name: String = buf.readUtf()
            return ModListEntry(id, name)
        }
    }
}