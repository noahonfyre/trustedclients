package com.nyronium.network

import com.nyronium.TrustedClients
import com.nyronium.data.ModListEntry
import com.nyronium.util.ComponentUtils
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object TrustedConstants {
    val wrongVersionMessage = buildWrongVersionMessage()
    val notInstalledMessage = buildNotInstalledMessage()

    fun buildRejectedMessage(mods: List<ModListEntry>): Text {
        val header = Text.literal("[").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))
            .append(ComponentUtils.defaultGradient("Trusted Clients"))
            .append(Text.literal("]").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)))

        val info = Text.literal("\n\nThe server rejected your current mod configuration. Affected modifications:\n").setStyle(
            Style.EMPTY.withColor(Formatting.GRAY))

        mods.forEach { mod -> info.append("\n${mod.name} (ID: ${mod.id})") }

        val footer = Text.literal("\n\nRemove these modifications from your client and restart your game.")
            .setStyle(Style.EMPTY.withColor(Formatting.RED))

        return header.append(info).append(footer)
    }

    fun buildWrongVersionMessage(): Text {
        val header = Text.literal("[").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))
            .append(ComponentUtils.defaultGradient("Trusted Clients"))
            .append(Text.literal("]").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)))

        val info = Text.literal("\n\nThe server rejected your installed version of Trusted Clients.\nPlease change your version to one that supports protocol version ${TrustedClients.PROTOCOL_VERSION}.\n\n").setStyle(
            Style.EMPTY.withColor(Formatting.GRAY))

        val link = Text.literal("https://modrinth.com/mod/trustedclients/versions")
            .setStyle(Style.EMPTY.withColor(0x1BD96A))

        return header.append(info).append(link)
    }

    private fun buildNotInstalledMessage(): Text {
        val header = Text.literal("[").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))
            .append(ComponentUtils.defaultGradient("Trusted Clients"))
            .append(Text.literal("]").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)))

        val info = Text.literal("\n\nThis server requires the Trusted Clients mod to be installed on your client.\nPlease download it using the link below and put it into your mods folder.\n\n").setStyle(
            Style.EMPTY.withColor(Formatting.GRAY))

        val link = Text.literal("https://modrinth.com/mod/trustedclients")
            .setStyle(Style.EMPTY.withColor(0x1BD96A))

        return header.append(info).append(link)
    }
}