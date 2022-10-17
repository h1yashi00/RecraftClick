package click.recraft.share

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import javax.annotation.Nullable

fun item(
    material: Material,
    amount: Int = 1,
    displayName: String = "",
    @Nullable lore: List<String> = arrayListOf(),
    customModelData: Int = 0,
    localizedName: String = ""
): ItemStack {
    val item = ItemStack(material, amount)
    val meta = item.itemMeta!!
    if (customModelData != 0) {
        meta.setCustomModelData(customModelData)
    }
    if (displayName != "") {
        meta.setDisplayName(displayName)
    }
    if (localizedName != "") {
        meta.setLocalizedName(localizedName)
    }
    if (lore.isNotEmpty()) {
        meta.lore = lore
    }
    item.itemMeta = meta
    return item
}
class ShowingDSL(private val itemStack: ItemStack) {
    lateinit var player: Player
    private val lore = arrayListOf<String>()
    var isChoose = false
    var selectedColoredGreenDye: Boolean? = null
    var newItem: ItemStack? = null
    fun setLore(lores: List<String>) {
        lore.addAll(lores)
    }
    fun setUnlock(boolean: Boolean) {
        val addLore = if (boolean) "${ChatColor.GREEN}アンロック" else "${ChatColor.RED}ロック"
        lore.add(addLore)
    }
    fun chose() {
        isChoose = true
    }
    fun setItem(item: ItemStack) {
        newItem = item
    }
    fun selectedColoredGreenDye(boolean: Boolean) {
        selectedColoredGreenDye = boolean
    }
    fun getITem(): ItemStack {
        if (newItem != null) return newItem!!
        val item = itemStack.clone()
        if (selectedColoredGreenDye != null) {
            return if (selectedColoredGreenDye!!) {
                item(Material.LIME_DYE, displayName = item.itemMeta!!.displayName)
            } else {
                item(Material.GRAY_DYE, displayName = item.itemMeta!!.displayName)
            }
        }
        val meta = item.itemMeta
        meta!!.lore = lore
        if (isChoose) {
            meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
        item.itemMeta = meta
        return item
    }
}
class OnClickDSL {
    lateinit var player: Player
    fun openInv(menuDSL: MenuDSL) {
        player.openInventory(menuDSL.createInv(player))
    }
    fun closeInv() {
        player.closeInventory()
    }
}

class SlotDSL(val menuDSL: MenuDSL, val index: Int, val item: ItemStack) {
}

fun SlotDSL.onClick(function: OnClickDSL.()-> Unit) {
    menuDSL.onClick[index] = function
}

fun SlotDSL.onRender(function: ShowingDSL.()->Unit) {
    menuDSL.onRender[index] = function
}

class MenuDSL(
    private val title: String,
    private val isCancel: Boolean,
    private val plugin: JavaPlugin
) {
    val onRender   = hashMapOf<Int, ShowingDSL.()->Unit>()
    val onClick = hashMapOf<Int, OnClickDSL.()->Unit>()
    val slotDSLs   = hashMapOf<Int, SlotDSL>()

    var maxSize = 0
    fun load() {
        val listener = object: Listener {
            @EventHandler
            fun click(event: InventoryClickEvent) {
                if (event.view.title != title) return
                event.isCancelled = isCancel
                event.currentItem ?: return
                val player = event.whoClicked as Player
                val currentSlot = event.slot
                val action = onClick[currentSlot] ?: return
                val onclick = OnClickDSL()
                onclick.player = player
                action.invoke(onclick)
            }
        }
        val pm = plugin.server.pluginManager
        pm.registerEvents(listener, plugin)
    }
    fun createInv(player: Player): Inventory {
        // indexは0から始まるが､
        // createInventoryは9の倍数にする必要があるので-1をしたものをベースに作成している｡
        val size = when {
            maxSize <= 9 * 1 - 1 -> 9
            maxSize <= 9 * 2 - 1 -> 9 * 2
            maxSize <= 9 * 3 - 1 -> 9 * 3
            maxSize <= 9 * 4 - 1 -> 9 * 4
            maxSize <= 9 * 5 - 1 -> 9 * 5
            maxSize <= 9 * 6 - 1 -> 9 * 6
            else -> {9}
        }
        return Bukkit.createInventory(null, size, title)
            .apply {
                slotDSLs.forEach { (index, slotDsl) ->
                    if (!onRender.contains(index)) {
                        setItem(index, slotDsl.item)
                        return@forEach
                    }
                    val renderFunc = onRender[slotDsl.index]!!
                    val showingDSL = ShowingDSL(slotDsl.item)
                    showingDSL.player = player
                    renderFunc.invoke(showingDSL)
                    setItem(index, showingDSL.getITem())
                }
            }
    }
}

// クリックした時のイベントを作成
fun MenuDSL.slot(
    line: Int,
    slot: Int,
    item: ItemStack,
    action: SlotDSL.() -> Unit
) {
    val index = (line * 9) + slot
    val slotDSL = SlotDSL(this, index, item)
    slotDSLs[index] = slotDSL
    if (maxSize <= index) {
        maxSize = index
    }
    action.invoke(slotDSL)
}

fun MenuDSL.slot(
    index: Int,
    item: ItemStack,
    action: SlotDSL.() -> Unit
) {
    val slotDSL = SlotDSL(this, index, item)
    slotDSLs[index] = slotDSL
    if (maxSize <= index) {
        maxSize = index
    }
    action.invoke(slotDSL)
}

fun KotlinPlugin.menu(
    title: String,
    cancelOnClick: Boolean,
    action: MenuDSL.() -> Unit
): MenuDSL {
    val menuDsl = MenuDSL(title, cancelOnClick, this)
    menuDsls.add(menuDsl)
    action.invoke(menuDsl)
    return menuDsl
}

// Menuを作成して､slotを大量に設定して､設定しているEventを実行する
open class KotlinPlugin: JavaPlugin() {
    val menuDsls = arrayListOf<MenuDSL>()
    val interceptItemsDSLs = arrayListOf<InteractItemDSL>()
    override fun onEnable() {
        menuDsls.forEach {
            it.load()
        }
        interceptItemsDSLs.forEach {
            it.load()
        }
    }
}
