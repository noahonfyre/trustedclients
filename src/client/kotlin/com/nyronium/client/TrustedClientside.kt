package com.nyronium.client

import com.nyronium.TrustedClients
import com.nyronium.data.ModListEntry
import com.nyronium.data.TrustedDataLogger.logMods
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking
import net.fabricmc.fabric.api.networking.v1.FriendlyByteBufs
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture

object TrustedClientside : ClientModInitializer {
	val CLIENT_LOGGER: Logger = LoggerFactory.getLogger(TrustedClients.ID)

	override fun onInitializeClient() {
        ClientLoginNetworking.registerGlobalReceiver(TrustedClients.LOGIN_CHANNEL) {
            client, listener, buf, callbackConsumer ->

            CLIENT_LOGGER.info("Resolving data...")

            val initialMods = resolveMods()
            logMods(initialMods, CLIENT_LOGGER)

            CLIENT_LOGGER.info("Exchanging with server...")

            val response = FriendlyByteBufs.create()

            response.writeInt(TrustedClients.PROTOCOL_VERSION)
            response.writeCollection(initialMods, ModListEntry::write)

            return@registerGlobalReceiver CompletableFuture.completedFuture(response)
        }
	}

    fun resolveMods(): List<ModListEntry> {
        val modList = mutableListOf<ModListEntry>()
        for (container in FabricLoader.getInstance().allMods) {
            if(container.containingMod.isPresent) continue
            modList.add(ModListEntry(container.metadata.id, container.metadata.name))
            CLIENT_LOGGER.info(container.toString())
        }
        return modList
    }
}