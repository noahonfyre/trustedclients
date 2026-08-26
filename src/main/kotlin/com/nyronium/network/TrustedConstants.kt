package com.nyronium.network

import com.nyronium.TrustedClients
import com.nyronium.data.ModListEntry
import com.nyronium.util.ComponentUtils
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style

object TrustedConstants {
    val header: Component = Component.literal("[").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY))
        .append(ComponentUtils.defaultGradient("Trusted Clients"))
        .append(Component.literal("]").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)))

    val wrongVersionMessage = composeWrongVersionMessage()
    val notInstalledMessage = composeNotInstalledMessage()

    fun composeRejectedMessage(mods: List<ModListEntry>): Component {
        val body = Component.literal("\n\nThe server rejected your current mod configuration. Affected modifications:\n")
            .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))

        mods.forEach { mod -> body.append("\n${mod.name} (ID: ${mod.id})") }

        val footer = Component.literal("\n\nRemove these modifications from your client and restart your game.")
            .setStyle(Style.EMPTY.withColor(ChatFormatting.RED))

        return Component.literal("").append(header).append(body).append(footer)
    }

    fun composeWrongVersionMessage(): Component {
        val body = Component.literal("\n\nThe server rejected your installed version of Trusted Clients.\nPlease change your version to one that supports protocol version ${TrustedClients.PROTOCOL_VERSION}.\n\n")
            .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))

        val footer = Component.literal("https://modrinth.com/mod/trustedclients/versions")
            .setStyle(Style.EMPTY.withColor(0x1BD96A))

        return Component.literal("").append(header).append(body).append(footer)
    }

    private fun composeNotInstalledMessage(): Component {
        val body = Component.literal("\n\nThis server requires the Trusted Clients mod to be installed on your client.\nPlease download it using the link below and put it into your mods folder.\n\n")
            .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))

        val footer = Component.literal("https://modrinth.com/mod/trustedclients")
            .setStyle(Style.EMPTY.withColor(0x1BD96A))

        return Component.literal("").append(header).append(body).append(footer)
    }
}