package thirtyvirus.sbbp;

import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MenuLoader {

    public static Inventory createShulkerBoxInventory(Player player, ItemStack shulkerBoxItemStack){

        if(shulkerBoxItemStack.getItemMeta() instanceof BlockStateMeta) {
            BlockStateMeta im = (BlockStateMeta)shulkerBoxItemStack.getItemMeta();
            if(im.getBlockState() instanceof ShulkerBox) {
                ShulkerBox shulker = (ShulkerBox) im.getBlockState();
                Inventory inv = Bukkit.createInventory(null, 27, "Holding: " + shulkerBoxItemStack.getItemMeta().getDisplayName());
                inv.setContents(shulker.getInventory().getContents());
                player.openInventory(inv);

                // set the lore of the item to "Opened!" to prevent duplication exploits down the line
                ItemMeta meta = shulkerBoxItemStack.getItemMeta();
                meta.setLore(Arrays.asList("Opened!"));
                shulkerBoxItemStack.setItemMeta(meta);

                return inv;
            }
        }

        //should never happen, because function is only called for shulker boxes
        return null;
    }
}
