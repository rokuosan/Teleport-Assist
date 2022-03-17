package com.deviseworks.tpa.schedule

import com.deviseworks.tpa.entity.PlayerStatus
import com.deviseworks.tpa.requests
import org.bukkit.scheduler.BukkitRunnable

class RequestSchedule( private val status: PlayerStatus ): BukkitRunnable() {
    private var elapsedTime = 0

    override fun run() {
        if(elapsedTime >= 10){
            requests.remove(status)

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