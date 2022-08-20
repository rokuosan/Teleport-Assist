package com.deviseworks.tpa

import com.deviseworks.tpa.command.AcceptCommand
import com.deviseworks.tpa.command.RequestCommand
import com.deviseworks.tpa.entity.PlayerStatus
import org.bukkit.plugin.java.JavaPlugin

var requests = mutableListOf<PlayerStatus>()

class App: JavaPlugin(){

    override fun onEnable() {
        Store.plugin = this

        logger.info("Registering commands...")
        getCommand("tpr")?.setExecutor(RequestCommand(this))
        getCommand("tpa")?.setExecutor(AcceptCommand())

        logger.info("Registering events...")

        logger.info("TPA is ready!")
    }
}