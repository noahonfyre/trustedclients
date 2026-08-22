package com.nyronium.config

class TrustedConfigData {
    var modList = mutableListOf<String>()
    var mode = ConfigMode.BLACKLIST
    var bypass = mutableListOf<String>()

    enum class ConfigMode {
        BLACKLIST,
        WHITELIST
    }
}