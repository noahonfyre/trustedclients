package com.nyronium

import com.nyronium.config.TrustedConfigHandler
import com.nyronium.network.TrustedHandler
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object TrustedClients : ModInitializer {
	const val ID: String = "trustedclients"
    const val PROTOCOL_VERSION: Int = 1

	val LOGGER: Logger = LoggerFactory.getLogger(ID)
    val LOGIN_CHANNEL: Identifier = Identifier.fromNamespaceAndPath(ID, "trusted_login")

	override fun onInitialize() {
        TrustedHandler.initialize()
        TrustedConfigHandler.initialize()
        TrustedCommands.initialize()
	}
}
