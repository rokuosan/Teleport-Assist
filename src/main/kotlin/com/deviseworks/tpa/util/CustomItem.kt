package com.deviseworks.tpa.util

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object CustomItem {

    /**
     * GUIのスロットに設置するアイテムを作成する関数
     */
    fun create(material: Material, name: String?, amount: Int, vararg lore: String? ): ItemStack{
        val item = ItemStack(material, amount)
        val meta = item.itemMeta

        meta?.setDisplayName(name)
        meta?.lore = lore.toList()

        item.itemMeta = meta

        return item
    }

}