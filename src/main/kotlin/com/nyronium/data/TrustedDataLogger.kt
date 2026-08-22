package com.nyronium.data

import org.slf4j.Logger
import kotlin.collections.joinToString

object TrustedDataLogger {
    fun logMods(mods: List<ModListEntry>, logger: Logger) {
        logger.info("Client has ${mods.size} mods installed.")
        logger.info(mods.joinToString { entry -> "${entry.name} (ID: ${entry.id})" })
    }
}