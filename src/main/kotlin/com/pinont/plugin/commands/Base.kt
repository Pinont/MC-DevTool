package com.pinont.plugin.commands

import com.pinont.lib.api.creator.items.ItemCreator
import com.pinont.lib.api.utils.Common
import com.pinont.lib.plugin.SimpleCommand
import com.pinont.plugin.devToolUi.MainMenu
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Base : SimpleCommand {

    var common: Common = Common()

    override fun getName(): String {
        return "devtool:dt:devtoy"
    }

    override fun description(): String {
        return "SingularityLib Developer Tools"
    }

    override fun execute(
        commandSourceStack: CommandSourceStack,
        strings: Array<out String>
    ) {
        if (commandSourceStack.sender is Player) {
            val player: Player = commandSourceStack.sender as Player
            when (strings.size) {
                0 -> {
                    MainMenu().openMenu(player)
                }

                1 -> {
                    val devToolItem =
                        ItemCreator(Material.DIAMOND).setDisplayName("<red>Developer Tool")
                            .setUnstackable(true).addInteraction(
                                null
                            ).create()
                    if (strings[0].equals("get", ignoreCase = true) || strings[0].equals(
                            "getItem",
                            ignoreCase = true
                        )
                    ) {
                        player.inventory.addItem(devToolItem)
                    }
                }

                3 -> {
                    if (strings[0].equals("world", ignoreCase = true)) {
                        if (strings[1].equals("teleport", ignoreCase = true)) {
                            val world: World? = Bukkit.getWorld(strings[2])
                            if (world != null) {
                                player.teleport(world.spawnLocation)
                                player.sendMessage(common.colorize("Teleporting to ${world.name}..."))
                            } else {
                                player.sendMessage(common.colorize("World not found!"))
                            }
                        }
                    }
                }
            }
            return
        }
        commandSourceStack.getSender().sendMessage("This command can only be executed by a player!")
    }

    override fun suggest(commandSourceStack: CommandSourceStack, args: Array<out String>): Collection<String> {
        when (args.size) {
            0, 1 -> {
                return listOf(
                    "get",
                    "getItem",
                    "world"
                )
            }

            2 -> {
                if (args[0].equals("world", ignoreCase = true)) {
                    return listOf("teleport")
                }
            }

            3 -> {
                if (args[0].equals("world", ignoreCase = true) && args[1].equals("teleport", ignoreCase = true)) {
                    return listOf(
                        *Bukkit.getWorlds().map { it.name }.toTypedArray()
                    )
                }
            }
        }
        return listOf()
    }

    override fun canUse(sender: CommandSender): Boolean {
        return if (sender is Player) {
            sender.hasPermission("pinont.devtool")
        } else {
            false
        }
    }
}