package com.deviseworks.tpa.inventory

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack


private const val INVENTORY_NAME = "Sample Menu"

class ExampleGui : Listener {

    private val inv: Inventory=Bukkit.createInventory(null, 9, INVENTORY_NAME)

    init {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example

        // Put the items into the inventory
        initializeItems()
    }

    // You can call this whenever you want to put the items in
    private fun initializeItems() {
        inv.addItem(
            createGuiItem(
                Material.DIAMOND_SWORD,
                "Example Sword",
                "§aFirst line of the lore",
                "§bSecond line of the lore"
            )
        )
        inv.addItem(
            createGuiItem(
                Material.IRON_HELMET,
                "§bExample Helmet",
                "§aFirst line of the lore",
                "§bSecond line of the lore"
            )
        )
    }

    // Nice little method to create a gui item with a custom name, and description
    private fun createGuiItem(material: Material?, name: String?, vararg lore: String?): ItemStack {
        val item=ItemStack(material!!, 1)
        val meta=item.itemMeta

        // Set the name of the item
        meta!!.setDisplayName(name)

        // Set the lore of the item
        meta.lore=lore.toList()
        item.itemMeta=meta
        return item
    }

    // You can open the inventory with this
    fun openInventory(ent: HumanEntity) {
        ent.openInventory(inv)
    }

    // Check for clicks on items
    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryClick(e: InventoryClickEvent) {
        val title = e.view.title
        if(title != INVENTORY_NAME) return

        e.isCancelled=true
        val clickedItem=e.currentItem

        // verify current item is not null
        if(clickedItem == null || clickedItem.type.isAir) return
        val p=e.whoClicked as Player

        // Using slots click is best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.rawSlot)
    }

    // Cancel dragging in our inventory
    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryClick(e: InventoryDragEvent) {
        val title = e.view.title
        if(title != INVENTORY_NAME) return

        e.isCancelled = true
    }
}