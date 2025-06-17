package com.pinont.plugin.devToolUi

import com.pinont.lib.api.creator.items.ItemCreator
import com.pinont.lib.api.creator.items.ItemHeadCreator
import com.pinont.lib.api.ui.Button
import com.pinont.lib.api.ui.Layout
import com.pinont.lib.api.ui.Menu
import com.pinont.lib.api.utils.Common
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.text.SimpleDateFormat
import java.util.*

class playerManager {

    var blank = MainMenu().blank

    fun accept (player: Player, target: Player): Layout {
        return object : Layout {
            override fun getKey(): Char {
                return 'a'
            }

            override fun getButton(): Button {
                return object : Button {
                    override fun getItem(): ItemStack? {
                        return ItemCreator(Material.GREEN_STAINED_GLASS).setDisplayName("<green>ACCEPT").create()
                    }

                    override fun onClick(player: Player) {
                        target.kick(Common().colorize("You have been kicked from this server."))
                        openMenu(player)
                    }
                }
            }
        }
    }

    fun deny (player: Player, target: Player): Layout {
        return object : Layout {
            override fun getKey(): Char {
                return 'd'
            }

            override fun getButton(): Button {
                return object : Button {
                    override fun getItem(): ItemStack? {
                        return ItemCreator(Material.RED_STAINED_GLASS).setDisplayName("<red>DENY").create()
                    }

                    override fun onClick(player: Player) {
                        showSpecificPlayerManager(player, target)
                    }
                }
            }
        }
    }

    fun openMenu(player: Player) {
        var main: Menu = Menu("Player Manager (${Bukkit.getOnlinePlayers().size})",
            (Bukkit.getOnlinePlayers().size / 9).coerceAtMost(6)
        )
        for (i in Bukkit.getOnlinePlayers().indices) {
            val player = Bukkit.getOnlinePlayers().toTypedArray()[i] as Player
            main.addButton(object: Button {
                override fun getSlot(): Int {
                    return i
                }

                override fun getItem(): ItemStack? {
                    return ItemHeadCreator(ItemStack(Material.PLAYER_HEAD)).setOwner(player.name)
                        .setDisplayName(player.name).create()
                }

                override fun onClick(player: Player) {
                }
            })
        }
    }

    private fun showSpecificPlayerManager(origin: Player, target: Player) {
        val main : Menu = Menu("Player Manager", 5)
        main.setLayout("====p====", "=========", "==t=i=o==", "==b=k=n==", "====v====", "=========")
        main.setKey(
            blank,
            object : Layout {
                override fun getKey(): Char {
                    return 'p'
                }

                override fun getButton(): Button {
                    return object : Button {
                        override fun getItem(): ItemStack? {
                            val firstPlayedDate = SimpleDateFormat("dd/MM/yyyy HH:mm")
                                .format(Date(target.firstPlayed))
                            return ItemHeadCreator(ItemStack(Material.PLAYER_HEAD))
                                .setOwner(target.name)
                                .setDisplayName(target.name)
                                .addLore("<bold><gray>First Joined: <yellow>${firstPlayedDate}")
                                .create()
                        }

                        override fun onClick(player: Player) {
                        }
                    }
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 't' // teleport
                }

                override fun getButton(): Button {
                    return object : Button {
                        override fun getItem(): ItemStack? {
                            return ItemCreator(ItemStack(Material.BEACON)).setDisplayName("Teleport")
                                .addLore("<bold><yellow>Click to Teleport")
                                .create()
                        }

                        override fun onClick(player: Player) {
                            player.teleport(target.location)
                            player.closeInventory()
                        }
                    }
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 'i' // player Inventory
                }

                override fun getButton(): Button? {
                    return null
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 'b'
                }

                override fun getButton(): Button {
                    return object : Button {
                        override fun getItem(): ItemStack? {
                            return ItemCreator(Material.ANVIL).setDisplayName("<red>Ban").addLore(
                                "<red>Click to ban."
                            ).create()
                        }

                        override fun onClick(player: Player) {
                            showBanPlayerApproval(player, target)
                        }
                    }
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 'k'
                }

                override fun getButton(): Button {
                    return object : Button {
                        override fun getItem(): ItemStack? {
                            return ItemCreator(Material.REDSTONE).setDisplayName("<red>Kick")
                                .addLore(
                                    "<red>Click to kick."
                                ).create()
                        }

                        override fun onClick(player: Player) {
                            showKickPlayerApproval(player, target)
                        }
                    }
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 'o'
                } // op Player

                override fun getButton(): Button? {
                    return null
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 'n'
                } // invincibility

                override fun getButton(): Button {
                    return object : Button {
                        override fun getItem(): ItemStack? {
                            return ItemCreator(Material.TOTEM_OF_UNDYING).setDisplayName("God: " + target.isInvulnerable)
                                .create()
                        }

                        override fun onClick(player: Player) {
                            target.isInvulnerable = !target.isInvulnerable
                            if (Bukkit.getServer().getAllowFlight()) {
                                target.allowFlight = target.isInvulnerable
                            } else player.sendMessage(("<red>You need to enable flight to use flying feature."))
                            showSpecificPlayerManager(player, target)
                        }
                    }
                }
            },
            object : Layout {
                override fun getKey(): Char { // vanish
                    return 'v'
                }

                override fun getButton(): Button? {
                    return null
                }
            }
        ).show(origin)
    }

    private fun showBanPlayerApproval(player: Player, target: Player) {
        Menu("<red>Are you sure to ban ${target.name}?", 5)
            .setLayout("=========", "====p====", "=========", "==a===d==", "=========")
            .setKey(
                blank,
                object : Layout {
                    override fun getKey(): Char {
                        return 'p'
                    }

                    override fun getButton(): Button {
                        return object : Button {
                            override fun getItem(): ItemStack? {
                                return ItemHeadCreator(ItemStack(Material.PLAYER_HEAD)).setOwner(target.name)
                                    .setDisplayName("<red>Are you sure to ban ${target.name}?").create()
                            }

                            override fun onClick(player: Player) {}
                        }
                    }
                },
                accept(player, target),
                deny(player, target)
            ).show(player)
    }

    fun showKickPlayerApproval(player: Player, target: Player) {
        Menu("<red>Are you sure to kick ${target.name}?", 5)
            .setLayout("=========", "====p====", "=========", "==a===d==", "=========")
            .setKey(
                blank,
                object : Layout {
                    override fun getKey(): Char {
                        return 'p'
                    }

                    override fun getButton(): Button {
                        return object : Button {
                            override fun getItem(): ItemStack? {
                                return ItemHeadCreator(ItemStack(Material.PLAYER_HEAD)).setOwner(target.name)
                                    .setDisplayName("<red>Are you sure to kick ${target.name}?").create()
                            }

                            override fun onClick(player: Player) {}
                        }
                    }
                },
                accept(player, target),
                deny(player, target)
            ).show(player)

    }

    /*
    private fun showServerPlayerManager(origin: Player?) {
        int max = 45;
        val playerManager = Menu("Player Manager", 9) // temp
        for (i in Bukkit.getOnlinePlayers().indices) {
            val player = Bukkit.getOnlinePlayers().toTypedArray()[i] as Player
            val finalI = i
            playerManager.addButton(object : Button {
                override fun getSlot(): Int {
                    return finalI
                }

                override fun getItem(): ItemStack? {
                    return ItemHeadCreator(ItemStack(Material.PLAYER_HEAD)).setOwner(player.getName())
                        .setDisplayName(player.getName()).create()
                }

                override fun onClick(player: Player) {
                    showSpecificPlayerManager(origin, player)
                }
            })
        }
        playerManager.show(origin)
    }
    */
    /*
    private fun showSpecificPlayerManager(origin: Player?, target: Player) {
        val playerManager = Menu("Player Manager", 9 * 5)
        playerManager.setLayout("====p====", "=========", "==t=i=o==", "==b=k=n==", "====v====", "=========")
        playerManager.setKey(
            blank(),
            object : Layout {
                override fun getKey(): Char {
                    return 'p'
                }

                override fun getButton(): Button {
                    return object : Button {
                        override fun getItem(): ItemStack? {
                            val firstPlayedDate = SimpleDateFormat("dd/MM/yyyy HH:mm")
                                .format(Date(target.getFirstPlayed()))
                            return ItemHeadCreator(ItemStack(Material.PLAYER_HEAD))
                                .setOwner(target.getName())
                                .setDisplayName(target.getName())
                                .addLore(ChatColor.BOLD.toString() + "" + ChatColor.GRAY + "First Joined: " + ChatColor.YELLOW + firstPlayedDate)
                                .create()
                        }

                        override fun onClick(player: Player?) {
                        }
                    }
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 't' // teleport
                }

                override fun getButton(): Button {
                    return object : Button {
                        override fun getItem(): ItemStack? {
                            return ItemCreator(ItemStack(Material.BEACON)).setDisplayName("Teleport")
                                .addLore(ChatColor.BOLD.toString() + "" + ChatColor.YELLOW + "Click to Teleport")
                                .create()
                        }

                        override fun onClick(player: Player) {
                            player.teleport(target.getLocation())
                            player.closeInventory()
                        }
                    }
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 'i' // player Inventory
                }

                override fun getButton(): Button? {
                    return null
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 'b'
                }

                override fun getButton(): Button {
                    return object : Button {
                        override fun getItem(): ItemStack? {
                            return ItemCreator(Material.ANVIL).setDisplayName(ChatColor.RED.toString() + "Ban").addLore(
                                ChatColor.RED.toString() + "Click to ban."
                            ).create()
                        }

                        override fun onClick(player: Player?) {
                            showBanPlayerApproval(player, target)
                        }
                    }
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 'k'
                }

                override fun getButton(): Button {
                    return object : Button {
                        override fun getItem(): ItemStack? {
                            return ItemCreator(Material.REDSTONE).setDisplayName(ChatColor.RED.toString() + "Kick")
                                .addLore(
                                    ChatColor.RED.toString() + "Click to kick."
                                ).create()
                        }

                        override fun onClick(player: Player?) {
                            showKickPlayerApproval(player, target)
                        }
                    }
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 'o'
                } // op Player

                override fun getButton(): Button? {
                    return null
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 'n'
                } // invincibility

                override fun getButton(): Button {
                    return object : Button {
                        override fun getItem(): ItemStack? {
                            return ItemCreator(Material.TOTEM_OF_UNDYING).setDisplayName("God: " + target.isInvulnerable())
                                .create()
                        }

                        override fun onClick(player: Player) {
                            target.setInvulnerable(!target.isInvulnerable())
                            if (Bukkit.getServer().getAllowFlight()) {
                                target.setAllowFlight(target.isInvulnerable())
                            } else player.sendMessage(ChatColor.RED.toString() + "You need to enable flight to use flying feature.")
                            showSpecificPlayerManager(player, target)
                        }
                    }
                }
            },
            object : Layout {
                override fun getKey(): Char { // vanish
                    return 'v'
                }

                override fun getButton(): Button? {
                    return null
                }
            }
        ).show(origin)
    }

    private fun showBanPlayerApproval(origin: Player?, target: Player) {
        Menu(ChatColor.RED.toString() + "Are you sure to ban " + target.getName() + "?", 9 * 5)
            .setLayout("=========", "====p====", "=========", "==a===d==", "=========")
            .setKey(
                blank(),
                object : Layout {
                    override fun getKey(): Char {
                        return 'p'
                    }

                    override fun getButton(): Button {
                        return object : Button {
                            override fun getItem(): ItemStack? {
                                return ItemHeadCreator(ItemStack(Material.PLAYER_HEAD)).setOwner(target.getName())
                                    .setDisplayName(
                                        ChatColor.RED.toString() + "Are you sure to ban " + target.getName() + "?"
                                    ).create()
                            }

                            override fun onClick(player: Player?) {
                            }
                        }
                    }
                },
                object : Layout {
                    override fun getKey(): Char {
                        return 'a'
                    }

                    override fun getButton(): Button {
                        return object : Button {
                            override fun getItem(): ItemStack? {
                                return ItemCreator(Material.GREEN_STAINED_GLASS).setDisplayName(ChatColor.GREEN.toString() + "ACCEPT")
                                    .create()
                            }

                            override fun onClick(player: Player) {
                                target.ban<BanEntry<in PlayerProfile?>?>(
                                    "You have been banned from this server.",
                                    null as Date?,
                                    player.getName(),
                                    true
                                )
                                showServerPlayerManager(origin)
                            }
                        }
                    }
                },
                object : Layout {
                    override fun getKey(): Char {
                        return 'd'
                    }

                    override fun getButton(): Button {
                        return object : Button {
                            override fun getItem(): ItemStack? {
                                return ItemCreator(Material.RED_STAINED_GLASS).setDisplayName(ChatColor.RED.toString() + "DENY")
                                    .create()
                            }

                            override fun onClick(player: Player?) {
                                showSpecificPlayerManager(player, target)
                            }
                        }
                    }
                }
            ).show(origin)
    }

    private fun showKickPlayerApproval(origin: Player?, target: Player) {
        Menu(ChatColor.RED.toString() + "Are you sure to kick " + target.getName() + "?", 9 * 5)
            .setLayout("=========", "====p====", "=========", "==a===d==", "=========")
            .setKey(
                blank(),
                object : Layout {
                    override fun getKey(): Char {
                        return 'p'
                    }

                    override fun getButton(): Button {
                        return object : Button {
                            override fun getItem(): ItemStack? {
                                return ItemHeadCreator(ItemStack(Material.PLAYER_HEAD)).setOwner(target.getName())
                                    .setDisplayName(
                                        ChatColor.RED.toString() + "Are you sure to kick " + target.getName() + "?"
                                    ).create()
                            }

                            override fun onClick(player: Player?) {
                            }
                        }
                    }
                },
                object : Layout {
                    override fun getKey(): Char {
                        return 'a'
                    }

                    override fun getButton(): Button {
                        return object : Button {
                            override fun getItem(): ItemStack? {
                                return ItemCreator(Material.GREEN_STAINED_GLASS).setDisplayName(ChatColor.GREEN.toString() + "ACCEPT")
                                    .create()
                            }

                            override fun onClick(player: Player?) {
                                target.kick()
                                showServerPlayerManager(origin)
                            }
                        }
                    }
                },
                object : Layout {
                    override fun getKey(): Char {
                        return 'd'
                    }

                    override fun getButton(): Button {
                        return object : Button {
                            override fun getItem(): ItemStack? {
                                return ItemCreator(Material.RED_STAINED_GLASS).setDisplayName(ChatColor.RED.toString() + "DENY")
                                    .create()
                            }

                            override fun onClick(player: Player?) {
                                showSpecificPlayerManager(player, target)
                            }
                        }
                    }
                }
            ).show(origin)
    }
    */

}