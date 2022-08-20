package com.deviseworks.tpa.command

import com.deviseworks.tpa.inventory.ExampleGui
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GuiCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(!command.name.equals("g", true)) return false


        if(sender is Player){
            ExampleGui().openInventory(sender)
        }

        return true
    }
}