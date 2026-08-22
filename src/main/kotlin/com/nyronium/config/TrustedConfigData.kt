package com.nyronium.config

class TrustedConfigData {
    var modList = mutableListOf<String>()
    var mode = ConfigMode.WHITELIST
    var bypass = mutableListOf<String>()

    enum class ConfigMode {
        BLACKLIST,
        WHITELIST
    }
}