package com.deviseworks.tpa.command

import com.deviseworks.tpa.common.Messages
import com.deviseworks.tpa.entity.PlayerStatus
import com.deviseworks.tpa.requests
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

class AcceptCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(!command.name.equals("tpa", true)) return false
        if(sender !is Player) return false

        // GUI モード
        if(args.isEmpty()){
            openGui(sender)
            return true
        }

        // 引数の大きさを確認
        if(args.size > 1){
            sender.sendMessage(Messages.TOO_MANY_ARGUMENTS)
            return true
        }

        // 対象のプレイヤーを確定
        val target = Bukkit.getServer().getPlayer(args[0])?: let{
            sender.sendMessage(Messages.NOT_FOUND_TARGET_PLAYER)
            return true
        }

        // 実行者のプレイヤーインスタンスを取得
        val executor = Bukkit.getServer().getPlayer(sender.name)?:let{
            sender.sendMessage(Messages.FATAL_ERROR)
            return true
        }

        // 受理
        acceptRequest(executor, target)

        return true
    }

    private fun openGui(player: Player){
        // Check requests
        var isExistsRequest = false
        for(r in requests){
            if(!r.accepted && !r.expired && r.isWaiting
                && r.targetUUID == player.uniqueId ){
                isExistsRequest = true
            }
        }
//        if(!isExistsRequest){
//            player.sendMessage("Not found requests.")
//            return
//        }

        // Main Frame
        val gui = ChestGui(6, "ACCEPT MENU")
        gui.setOnGlobalClick { e -> e.isCancelled = true }

        // Navbar
        val closeButton = CustomItem.create(Material.BARRIER, "閉じる", 1)
        val navigation = StaticPane(0, 5, 9, 1)
        navigation.addItem(GuiItem(closeButton), 4, 0)
        navigation.fillWith(CustomItem.create(Material.BLACK_STAINED_GLASS_PANE, " ", 1))
        navigation.setOnClick{ e ->
            if(e.currentItem == closeButton){
                e.whoClicked.closeInventory()
            }
        }
        navigation.priority = Pane.Priority.LOW

        // Pagination
        val pageFrame = PaginatedPane(0, 0, 9, 6)
        val players = requests.filter { r -> r.targetUUID == player.uniqueId && r.isWaiting}
        val pages = players.size / 46 + 1

        repeat(pages){ pageIndex ->
            // Page
            val page = OutlinePane(0, 0, 6, 5)
            for(i in 0 until 45){
                val index = pageIndex * 45 + i

                if(index < players.size){
                    val p = players[index]

                   // Head
                    val head = CustomItem.create(Material.PLAYER_HEAD,p.uuid.toString(), 1, "Click to accept")
                    val skull = head.itemMeta as SkullMeta
                    skull.ownerProfile = Bukkit.getPlayer(p.uuid)?.playerProfile
                    head.itemMeta = skull

                    // Add item
                    page.addItem(GuiItem(head))
                }
            }

            // Event
            page.setOnClick { e ->
                e.currentItem?.itemMeta?.displayName?.let { name ->
                    val p = Bukkit.getPlayer(name)

                    if(p == null){
                        player.sendMessage(Messages.NOT_FOUND_TARGET_PLAYER)
                    }else{
                        acceptRequest(player, p)
                        e.whoClicked.closeInventory()
                    }
                }
            }

            // Footer
            val footer = StaticPane(0, 5, 9, 1)
            val next = CustomItem.create(Material.MUSIC_DISC_STAL, "次", 1)
            val previous = CustomItem.create(Material.MUSIC_DISC_11, "前", 1)

            if(pageIndex + 1 < pages){
                footer.addItem(GuiItem(next), 7, 0)
            }
            if(0 < pageIndex){
                footer.addItem(GuiItem(previous), 1, 0)
            }

            // Footer Event
            footer.setOnClick { e ->
                if(e.currentItem == next){
                    pageFrame.page = pageFrame.page + 1
                    gui.update()
                }else if(e.currentItem == previous){
                    pageFrame.page = pageFrame.page - 1
                    gui.update()
                }
            }

            // Add Pane
            gui.addPane(navigation)
            pageFrame.addPane(pageIndex, page)
            pageFrame.addPane(pageIndex, footer)
        }

        // Add page frame
        gui.addPane(pageFrame)

        // Show
        gui.show(player)
    }

    private fun acceptRequest(executor: Player, target: Player){
        // ターゲットが自分に対してリクエストを送信しているか
        var flag = false
        lateinit var status: PlayerStatus
        var accepted: Boolean    // 承認済み
        var existTarget: Boolean // 相手からのリクエストがあるか
        var existSelf: Boolean   // 自分がターゲットになっているか
        var expired: Boolean // 期限切れか
        for(r in requests){
            status = r
            accepted = r.accepted
            existSelf = r.targetUUID == executor.uniqueId
            existTarget = r.uuid == target.uniqueId
            expired = r.expired

            if(!accepted && existSelf && existTarget && !expired){
                flag = true
                break
            }
        }

        if(flag){
            target.teleport(executor.location)
            executor.sendMessage(Messages.PERMIT_TELEPORT)
            target.sendMessage(Messages.SUCCESSFULLY_TELEPORT)
            status.accepted = true
        }else{
            executor.sendMessage(Messages.NOT_FOUND_REQUEST)
            executor.sendMessage(Messages.HINT_RESEND_REQUEST)
        }
    }
}