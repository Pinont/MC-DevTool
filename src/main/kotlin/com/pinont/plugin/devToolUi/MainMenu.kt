package com.pinont.plugin.devToolUi

import com.pinont.lib.api.creator.items.ItemCreator
import com.pinont.lib.api.creator.items.ItemHeadCreator
import com.pinont.lib.api.ui.Button
import com.pinont.lib.api.ui.Layout
import com.pinont.lib.api.ui.Menu
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class MainMenu {

    var blank: Layout = object : Layout {
        override fun getKey(): Char {
            return '='
        }

        override fun getButton(): Button {
            return object : Button {
                override fun getItem(): ItemStack {
                    return ItemStack(Material.AIR)
                }

                override fun onClick(player: Player?) {
                }
            }
        }
    }

    fun openMenu(player: Player) {
        val devMenu =
            Menu("<red> Development Tools", 9 * 5)
        devMenu.setLayout("=========", "====i====", "=========", "==w=p=t==", "=========")
        devMenu.setKey(
            blank,
            object : Layout {
                override fun getKey(): Char {
                    return 'i'
                }

                override fun getButton(): Button {
                    return object : Button {
                        override fun getItem(): ItemStack? {
                            return ItemCreator(ItemStack(Material.GRASS_BLOCK)).setDisplayName("<green>Server Info")
                                .addLore(
                                    "<gray>Server: <yellow> ${Bukkit.getServer().name}",
                                    "<gray>Server: <yellow> ${Bukkit.getServer().version}",
                                    "<gray>Server: <yellow> ${Bukkit.getServer().pluginManager.plugins.size}"
                                ).create()
                        }

                        override fun onClick(player: Player?) {
                        }
                    }
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 'p'
                }

                override fun getButton(): Button {
                    return object : Button {
                        override fun getItem(): ItemStack? {
                            return ItemHeadCreator(ItemStack(Material.PLAYER_HEAD)).setOwner(player.name)
                                .setDisplayName("Player List").create()
                        }

                        override fun onClick(player: Player) {
                            playerManager().openMenu(player)
                        }
                    }
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 'w' // worldcreator
                }

                override fun getButton(): Button {
                    return object : Button {
                        override fun getItem(): ItemStack? {
                            return ItemCreator(ItemStack(Material.COARSE_DIRT)).setDisplayName("Worlds").create()
                        }

                        override fun onClick(player: Player?) {

                        }
                    }
                }
            },
            object : Layout {
                override fun getKey(): Char {
                    return 't' // tools
                }

                override fun getButton(): Button {
                    return object : Button {
                        override fun getItem(): ItemStack? {
                            return ItemCreator(Material.STICK).setDisplayName("Tools").addLore("More Tools").create()
                        }

                        override fun onClick(player: Player?) {

                        }
                    }
                }
            }
        )
        devMenu.show(player)
    }

}