package com.deviseworks.tpa.schedule

import com.deviseworks.tpa.common.Messages
import com.deviseworks.tpa.entity.PlayerStatus
import com.deviseworks.tpa.requests
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class RequestSchedule( private val status: PlayerStatus ): BukkitRunnable() {
    private var elapsedTime = 0

    override fun run() {
        if(elapsedTime >= 20){
            requests.remove(status)

            Bukkit.getServer().getPlayer(status.uuid)?.sendMessage(Messages.REQUEST_TIMEOUT)
            Bukkit.getServer().getPlayer(status.targetUUID)?.sendMessage(Messages.REQUEST_TIMEOUT)

            // 終わり
            cancel()
            return
        }else{
            if(status.accepted){
                cancel()
            }

            elapsedTime++
        }
    }
}