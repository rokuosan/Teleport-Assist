package com.deviseworks.tpa.command

import com.deviseworks.tpa.common.Messages
import com.deviseworks.tpa.entity.PlayerStatus
import com.deviseworks.tpa.requests
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AcceptCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(command.name.equals("tpa", true)){
            if(args.isNotEmpty()){
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

            }else{
                sender.sendMessage(Messages.REQUIRE_TARGET_PLAYER)
            }

            return true
        }else{
            return false
        }
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