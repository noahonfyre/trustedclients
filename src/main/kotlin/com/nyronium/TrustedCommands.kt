package com.nyronium

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.nyronium.config.TrustedConfigHandler
import com.nyronium.network.TrustedConstants
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager.RegistrationEnvironment
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text


object TrustedCommands {
    fun initialize() {
        CommandRegistrationCallback.EVENT.register(::registerCommand)
    }

    fun registerCommand(
        dispatcher: CommandDispatcher<ServerCommandSource>,
        registryAccess: CommandRegistryAccess,
        environment: RegistrationEnvironment
    ) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<ServerCommandSource>(TrustedClients.ID)
                .requires { it.hasPermissionLevel(4) }
                .then(
                    LiteralArgumentBuilder.literal<ServerCommandSource>("reload")
                        .executes { ctx ->
                            TrustedConfigHandler.reload()
                            ctx.source.sendFeedback({ Text.literal("").append(TrustedConstants.header).append(" ").append("Config reloaded!") }, true)
                            return@executes 1
                        }
                )
                .then(
                    LiteralArgumentBuilder.literal<ServerCommandSource>("list")
                        .executes { ctx ->
                            ctx.source.sendFeedback({ Text.literal("").append(TrustedConstants.header).append(" ").append("Current mod list:") }, true)
                            ctx.source.sendFeedback({ Text.literal(TrustedConfigHandler.data.modList.joinToString(", ")) }, true)
                            return@executes 1
                        }
                )
                .then(
                    LiteralArgumentBuilder.literal<ServerCommandSource>("add")
                        .then(
                            RequiredArgumentBuilder.argument<ServerCommandSource, String>("id", StringArgumentType.string())
                                .executes { ctx ->
                                    val id = StringArgumentType.getString(ctx, "id")
                                    if(id.contains(' ') || id.lowercase() != id || TrustedConfigHandler.data.modList.contains(id)) return@executes 0
                                    TrustedConfigHandler.update { data -> data.modList.add(id) }
                                    ctx.source.sendFeedback({ Text.literal("").append(TrustedConstants.header).append(" ").append("Added \"$id\" to the config list!") }, true)
                                    return@executes 1
                                }
                        )
                )
                .then(
                    LiteralArgumentBuilder.literal<ServerCommandSource>("remove")
                        .then(
                            RequiredArgumentBuilder.argument<ServerCommandSource, String>("id", StringArgumentType.string())
                                .executes { ctx ->
                                    val id = StringArgumentType.getString(ctx, "id")
                                    if(id.contains(' ') || id.lowercase() != id || !TrustedConfigHandler.data.modList.contains(id)) return@executes 0
                                    TrustedConfigHandler.update { data -> data.modList.remove(id) }
                                    ctx.source.sendFeedback({ Text.literal("").append(TrustedConstants.header).append(" ").append("Removed \"$id\" from the config list!") }, true)
                                    return@executes 1
                                }
                        )
                )
        )

    }
}