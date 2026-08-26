package com.nyronium.util

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor

object ComponentUtils {
    fun defaultGradient(text: String): MutableComponent {
        return gradientComponent(text, 0xFF8800, 0xEA00FF)
    }

    fun gradientComponent(text: String, startHex: Int, endHex: Int): MutableComponent {
        var result = Component.empty()
        val length = text.length

        val startR = (startHex shr 16) and 0xFF
        val startG = (startHex shr 8) and 0xFF
        val startB = startHex and 0xFF

        val endR = (endHex shr 16) and 0xFF
        val endG = (endHex shr 8) and 0xFF
        val endB = endHex and 0xFF

        for (i in 0..<length) {
            val ratio = if (length == 1) 0f else i.toFloat() / (length - 1)

            val red = (startR + ratio * (endR - startR)).toInt()
            val green = (startG + ratio * (endG - startG)).toInt()
            val blue = (startB + ratio * (endB - startB)).toInt()

            val rgb = (red shl 16) or (green shl 8) or blue
            val color = TextColor.fromRgb(rgb)

            val letter: MutableComponent = Component.literal(text[i].toString())
                .setStyle(Style.EMPTY.withColor(color))
            result = result.append(letter)
        }

        return result
    }
}