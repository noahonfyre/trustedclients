package com.nyronium.network

import com.nyronium.TrustedClients
import com.nyronium.TrustedClients.PROTOCOL_VERSION
import com.nyronium.data.ModListEntry
import com.nyronium.data.TrustedDataLogger
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking
import net.fabricmc.loader.api.FabricLoader

object TrustedHandler {
    fun initialize() {
        ServerLoginConnectionEvents.QUERY_START.register { impl, server, sender, synchronizer ->
            sender.sendPacket(TrustedClients.LOGIN_CHANNEL, PacketByteBufs.empty())
        }

        ServerLoginNetworking.registerGlobalReceiver(TrustedClients.LOGIN_CHANNEL) {
            server, handler, understood, buf, synchronizer, responseSender ->

            if(!understood) {
                handler.disconnect(TrustedConstants.notInstalledMessage)
                return@registerGlobalReceiver
            }

            val peerProtocolVersion = buf.readInt()

            if(peerProtocolVersion != PROTOCOL_VERSION) {
                handler.disconnect(TrustedConstants.wrongVersionMessage)
                return@registerGlobalReceiver
            }

            val modList = buf.readList(ModListEntry.Companion::read).filter { modListEntry ->
                !FabricLoader.getInstance().isModLoaded(modListEntry.id) && !TrustedValidator.isDefaultMod(modListEntry.id)
            }

            TrustedDataLogger.logMods(modList, TrustedClients.LOGGER)

            val flaggedMods = TrustedValidator.flagMods(modList, handler.connectionInfo.substringBefore(" "))

            if(flaggedMods.isNotEmpty()) {
                handler.disconnect(TrustedConstants.buildRejectedMessage(flaggedMods))
                return@registerGlobalReceiver
            }
        }
    }
}