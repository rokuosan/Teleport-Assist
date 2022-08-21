package com.deviseworks.tpa.command

import com.deviseworks.tpa.Store
import com.deviseworks.tpa.common.Messages
import com.deviseworks.tpa.entity.PlayerStatus
import com.deviseworks.tpa.requests
import com.deviseworks.tpa.schedule.RequestSchedule
import com.deviseworks.tpa.util.CustomItem
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane
import com.github.stefvanschie.inventoryframework.pane.Pane
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.SkullMeta

class RequestCommand: CommandExecutor {
    private val plugin = Store.plugin

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(command.name.equals("tpr", true)){
            if(args.isNotEmpty()){
                // 引数の大きさを確認
                if(args.size > 2){
                    sender.sendMessage(Messages.TOO_MANY_ARGUMENTS)
                    return true
                }

                var isForceMode = false
                var destPlayer: Player? = null

                // 引数を解析
                for (arg in args){
                    if(arg.startsWith("-")){
                        when(arg.substringAfter("-")){
                            "f" -> {
                                if(sender.hasPermission("tpa.bypass.accept")){
                                    isForceMode = true
                                }else{
                                    sender.sendMessage(Messages.DOESNT_HAVE_PERMISSION)
                                    return false
                                }
                            }
                            else -> {
                                sender.sendMessage(Messages.UNKNOWN_ARGUMENTS)
                                return true
                            }
                        }
                    }else{
                        destPlayer = Bukkit.getServer().getPlayer(arg)?: let{
                            sender.sendMessage(Messages.NOT_FOUND_TARGET_PLAYER)
                            return true
                        }
                    }
                }

                // プレイヤーが指定されていない場合
                if(destPlayer == null){
                    sender.sendMessage(Messages.REQUIRE_TARGET_PLAYER)
                }else{
                    // 実行者のプレイヤーインスタンスを取得
                    val executor = Bukkit.getServer().getPlayer(sender.name)?:let{
                        sender.sendMessage(Messages.FATAL_ERROR)
                        return true
                    }

                    // リクエストを送信
                    return sendRequest(executor, destPlayer, isForceMode)
                }

                return true
            }

            // GUI モード
            if(sender is Player){

                // Main frame
                val gui = ChestGui(6, Messages.MENU_TELEPORT_REQUEST)
                gui.setOnGlobalClick { e -> e.isCancelled = true }

                // Navigation
                val closeButton = CustomItem.create(Material.BARRIER, "閉じる", 1)
                val navigation = StaticPane(0, 5, 9, 1)
                navigation.addItem(GuiItem(closeButton), 4, 0)
                navigation.fillWith(CustomItem.create(Material.BLACK_STAINED_GLASS_PANE, " ", 1))
                navigation.setOnClick{ e -> run{
                    if(e.currentItem == closeButton){
                        e.whoClicked.closeInventory()
                    }
                }}
                navigation.priority = Pane.Priority.LOW
                gui.addPane(navigation)

                // Pagination
                val pageFrame = PaginatedPane(0, 0, 9, 6)

                val players = Bukkit.getServer().onlinePlayers.toMutableList()
                players.remove(sender)
                val pages = players.size / 46 + 1

                repeat(pages){ pageIndex ->
                    // Player List
                    val page = OutlinePane(0, 0, 9, 5)
                    for(i in 0 until 45){
                        val index = pageIndex * 45 + i

                        if(players.size <= index) break

                        val head = CustomItem.create(Material.PLAYER_HEAD, players[index].name, 1, Messages.ITEM_TELEPORT_DESCRIPTION)
                        val skullMeta = head.itemMeta as SkullMeta
                        skullMeta.ownerProfile = players[index].playerProfile
                        head.itemMeta = skullMeta

                        page.addItem(GuiItem(head))
                    }

                    page.setOnClick { e->
                        e.currentItem?.itemMeta?.displayName?.let {name ->
                            Bukkit.getPlayer(name)?.let{ player ->
                                sendRequest(sender, player)
                                e.whoClicked.closeInventory()
                            }
                        }
                    }

                    // Footer
                    val footer = StaticPane(0 ,5, 9, 1)
                    val next = CustomItem.create(Material.MUSIC_DISC_STAL, "次のページへ", 1)
                    val previous = CustomItem.create(Material.MUSIC_DISC_11, "前のページへ", 1)

                    if(pages > 1){
                        if(pageIndex + 1 < pages){
                            footer.addItem(GuiItem(next), 7, 0)
                        }
                        if(pageIndex > 0){
                            footer.addItem(GuiItem(previous), 1, 0)
                        }
                    }

                    footer.setOnClick { e->
                        if(e.currentItem == next){
                            pageFrame.page = pageFrame.page + 1
                            gui.update()
                        }else if(e.currentItem == previous){
                            pageFrame.page = pageFrame.page - 1
                            gui.update()
                        }
                    }

                    // Add pane
                    pageFrame.addPane(pageIndex, page)
                    pageFrame.addPane(pageIndex, footer)
                }
                gui.addPane(pageFrame)

                gui.show(sender)

            }else{
                return false
            }


            return true
        }else{
            return false
        }
    }

    private fun sendRequest(executor: Player, target: Player, isForceMode: Boolean = false): Boolean{
        // すでに他人にリクエストを送っているか
        for(r in requests){
            if(r.uuid==executor.uniqueId && !r.accepted){
                executor.sendMessage(Messages.REQUEST_HAS_ALREADY_SENT)
                return true
            }
        }

        // リクエスト先が自分か
        if(target.uniqueId == executor.uniqueId){
            executor.sendMessage(Messages.CANNOT_REQUEST_SELF)
            return true
        }

        // 実行
        if(isForceMode){
            executor.teleport(target.location)
            executor.sendMessage(Messages.SUCCESSFULLY_TELEPORT)
        }else{
            target.sendMessage(Messages.requestNotification(executor.name))
            target.sendMessage(Messages.HINT_HOW_TO_ACCEPT)
            executor.sendMessage(Messages.sentNotification(target.name))

            val status = PlayerStatus(executor.uniqueId, true, target.uniqueId, false)

            requests.add(status)
            plugin?.let { RequestSchedule(status).runTaskTimer(it, 0L, 20L) }
        }

        return true
    }
}