package thirtyvirus.sbbp;

import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class MenuLoader {

    public static Inventory createShulkerBoxInventory(Player player, ItemStack shulkerBoxItemStack){

        if(shulkerBoxItemStack.getItemMeta() instanceof BlockStateMeta){
            BlockStateMeta im = (BlockStateMeta)shulkerBoxItemStack.getItemMeta();
            if(im.getBlockState() instanceof ShulkerBox){
                ShulkerBox shulker = (ShulkerBox) im.getBlockState();
                Inventory inv = Bukkit.createInventory(null, 27, "Shulker Box - In Hand");
                inv.setContents(shulker.getInventory().getContents());
                player.openInventory(inv);
                return inv;
            }
        }

        //should never happen, because function is only called for shulker boxes
        return null;
    }
}
