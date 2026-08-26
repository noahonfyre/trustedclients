package com.nyronium

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.nyronium.config.TrustedConfigHandler
import com.nyronium.network.TrustedConstants
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.server.permissions.Permissions


object TrustedCommands {
    fun initialize() {
        CommandRegistrationCallback.EVENT.register(::registerCommand)
    }

    fun registerCommand(
        dispatcher: CommandDispatcher<CommandSourceStack>,
        buildConComponent: CommandBuildContext,
        selection: Commands.CommandSelection
    ) {
        dispatcher.register(
            LiteralArgumentBuilder.literal<CommandSourceStack>(TrustedClients.ID)
                .requires { it.permissions().hasPermission(Permissions.COMMANDS_ADMIN) }
                .then(
                    LiteralArgumentBuilder.literal<CommandSourceStack>("reload")
                        .executes { ctx ->
                            TrustedConfigHandler.reload()
                            ctx.source.sendSuccess({ Component.literal("").append(TrustedConstants.header).append(" ").append("Config reloaded!") }, true)
                            return@executes 1
                        }
                )
                .then(
                    LiteralArgumentBuilder.literal<CommandSourceStack>("list")
                        .executes { ctx ->
                            ctx.source.sendSuccess({ Component.literal("").append(TrustedConstants.header).append(" ").append("Current mod list:") }, true)
                            ctx.source.sendSuccess({ Component.literal(TrustedConfigHandler.data.modList.joinToString(", ")) }, true)
                            return@executes 1
                        }
                )
                .then(
                    LiteralArgumentBuilder.literal<CommandSourceStack>("add")
                        .then(
                            RequiredArgumentBuilder.argument<CommandSourceStack, String>("id", StringArgumentType.string())
                                .executes { ctx ->
                                    val id = StringArgumentType.getString(ctx, "id")
                                    if(id.contains(' ') || id.lowercase() != id || TrustedConfigHandler.data.modList.contains(id)) return@executes 0
                                    TrustedConfigHandler.update { data -> data.modList.add(id) }
                                    ctx.source.sendSuccess({ Component.literal("").append(TrustedConstants.header).append(" ").append("Added \"$id\" to the config list!") }, true)
                                    return@executes 1
                                }
                        )
                )
                .then(
                    LiteralArgumentBuilder.literal<CommandSourceStack>("remove")
                        .then(
                            RequiredArgumentBuilder.argument<CommandSourceStack, String>("id", StringArgumentType.string())
                                .executes { ctx ->
                                    val id = StringArgumentType.getString(ctx, "id")
                                    if(id.contains(' ') || id.lowercase() != id || !TrustedConfigHandler.data.modList.contains(id)) return@executes 0
                                    TrustedConfigHandler.update { data -> data.modList.remove(id) }
                                    ctx.source.sendSuccess({ Component.literal("").append(TrustedConstants.header).append(" ").append("Removed \"$id\" from the config list!") }, true)
                                    return@executes 1
                                }
                        )
                )
        )

    }
}