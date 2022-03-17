package com.deviseworks.tpa.command

import com.deviseworks.tpa.App
import com.deviseworks.tpa.common.Messages
import com.deviseworks.tpa.entity.PlayerStatus
import com.deviseworks.tpa.requests
import com.deviseworks.tpa.schedule.RequestSchedule
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RequestCommand(private val plugin: App): CommandExecutor {

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

                    // すでに他人にリクエストを送っているか
                    for(r in requests){
                        if(r.uuid==executor.uniqueId){
                            sender.sendMessage(Messages.REQUEST_HAS_ALREADY_SENT)
                            return true
                        }
                    }

                    // リクエスト先が自分か
                    if(destPlayer.uniqueId == executor.uniqueId){
                        sender.sendMessage(Messages.CANNOT_REQUEST_SELF)
                        return true
                    }

                    // 実行
                    if(isForceMode){
                        executor.teleport(destPlayer.location)
                        executor.sendMessage(Messages.SUCCESSFULLY_TELEPORT)
                    }else{
                        destPlayer.sendMessage(Messages.requestNotification(executor.name))
                        sender.sendMessage(Messages.sentNotification(destPlayer.name))

                        val status = PlayerStatus(executor.uniqueId, true, destPlayer.uniqueId, false)

                        requests.add(status)
                        RequestSchedule(status).runTaskTimer(plugin, 0L, 20L)
                    }
                }


            }else{
                sender.sendMessage(Messages.REQUIRE_TARGET_PLAYER)
            }

            return true
        }else{
            return false
        }
    }
}