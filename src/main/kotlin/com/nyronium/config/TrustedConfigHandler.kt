package com.nyronium.config

import com.google.gson.GsonBuilder
import com.nyronium.TrustedClients
import kotlinx.io.IOException
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Files
import kotlin.io.path.exists

object TrustedConfigHandler {
    var data = TrustedConfigData()

    private val GSON = GsonBuilder()
        .setPrettyPrinting()
        .create()

    private val FILE = FabricLoader.getInstance().configDir
        .resolve("trustedclients")
        .resolve("main.json")

    private fun load(): TrustedConfigData {
        data = try {
            if(FILE.exists()) {
                val json = Files.readString(FILE)
                GSON.fromJson(json, TrustedConfigData::class.java)
            } else {
                TrustedConfigData().also { save() }
            }
        } catch (e: IOException) {
            TrustedClients.LOGGER.error("Failed to load config $FILE: ${e.message}")
            TrustedConfigData()
        } catch(e: Exception) {
            TrustedClients.LOGGER.error("An unknown error occurred while loading config $FILE: ${e.message}")
            TrustedConfigData()
        }
        return data
    }

    fun save() {
        try {
            Files.createDirectories(FILE.parent)
            Files.writeString(FILE, GSON.toJson(data))
        } catch (e: IOException) {
            TrustedClients.LOGGER.error("Failed to save config $FILE: ${e.message}")
        }
    }

    fun update(mutator: (TrustedConfigData) -> Unit) {
        mutator(data)
        save()
    }

    fun initialize() {
        ServerLifecycleEvents.SERVER_STARTED.register { load() }
        ServerLifecycleEvents.SERVER_STOPPING.register { save() }
    }
}