package thirtyvirus.sbbp.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.sbbp.ShulkerBoxBackPacks;
import thirtyvirus.sbbp.MenuLoader;

public class UseItem implements Listener {

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event){

        // Shulker Box Right Click
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            Player player = event.getPlayer();
            if (!player.hasPermission("ShulkerBoxBackPacks.use")) return;

            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) return;

            // test for Shulker Boxes that are renamed
            if (!ShulkerBoxBackPacks.useNamedBoxes || !ShulkerBoxBackPacks.useFormattedNamedBoxes){
                ItemStack i = new ItemStack(item.getType());
                String defaultName = i.getItemMeta().getDisplayName();

                if (!ShulkerBoxBackPacks.useNamedBoxes && !item.getItemMeta().getDisplayName().equals(defaultName)) return;
                if (!ShulkerBoxBackPacks.useFormattedNamedBoxes && !item.getItemMeta().getDisplayName().equals(ChatColor.stripColor(item.getItemMeta().getDisplayName()))) return;

            }

            if (ShulkerBoxBackPacks.supportedMaterials.contains(item.getType()) && player.isSneaking()) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_OPEN, 1, 1);
                player.openInventory(MenuLoader.createShulkerBoxInventory(player, player.getInventory().getItemInMainHand()));
            }

        }

    }
}
