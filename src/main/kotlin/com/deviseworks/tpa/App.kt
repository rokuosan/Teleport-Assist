package com.deviseworks.tpa

import com.deviseworks.tpa.command.AcceptCommand
import com.deviseworks.tpa.command.GuiCommand
import com.deviseworks.tpa.command.RequestCommand
import com.deviseworks.tpa.entity.PlayerStatus
import com.deviseworks.tpa.inventory.ExampleGui
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

var requests = mutableListOf<PlayerStatus>()

class App: JavaPlugin(){

    override fun onEnable() {

        logger.info("Registering commands...")
        getCommand("tpr")?.setExecutor(RequestCommand(this))
        getCommand("tpa")?.setExecutor(AcceptCommand())
//        getCommand("sr")?.setExecutor(ShowList())
        getCommand("g")?.setExecutor(GuiCommand())

        logger.info("Registering events...")
        Bukkit.getServer().pluginManager.registerEvents(ExampleGui(), this)

        logger.info("TPA is ready!")
    }
}