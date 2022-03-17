package com.deviseworks.tpa.command

import com.deviseworks.tpa.requests
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * デバッグ用なのでこれは見なかったことにしてね
 */
class ShowList: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        return if(command.name.equals("sr", true)){
            sender.sendMessage(requests.toString())

            true
        }else{
            false
        }
    }
}