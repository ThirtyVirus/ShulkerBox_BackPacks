package thirtyvirus.sbbp.events;

import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import thirtyvirus.sbbp.multiversion.Version;
import thirtyvirus.sbbp.ShulkerBoxBackPacks;

public class Inventory implements Listener {

    @EventHandler(priority=EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event){

        // ensure the Inventory is a Shulker Box Backpack Inventory
        if (Version.getVersion().isBiggerThan(Version.v1_13)) {
            if (!event.getView().getTitle().contains("Shulker Box - In Hand")) return;
        }
        else {
           if (!event.getInventory().getName().contains("Shulker Box - In Hand")) return;
        }

        if (event.getCurrentItem() == null) return;
        if (!ShulkerBoxBackPacks.nesting && ShulkerBoxBackPacks.supportedMaterials.contains(event.getCurrentItem().getType())){
            event.setCancelled(true);
            return;
        }

        ItemStack shulkerBox = event.getWhoClicked().getInventory().getItemInMainHand();

        // prevent duplication exploits on laggy servers by closing Inventory if no shulker box in hand on Inventory click
        if (shulkerBox == null) { event.setCancelled(true); event.getWhoClicked().closeInventory(); }

        // prevent putting box inside itself (tests this by testing equal-ness for shulker boxes in hotbar
        if (event.getCurrentItem().equals(shulkerBox) && event.getRawSlot() >= 54) { event.setCancelled(true); return; }

        // prevent nesting too far
        if (ShulkerBoxBackPacks.supportedMaterials.contains(event.getCurrentItem().getType())){
            if (event.getRawSlot() > 34){
                if (ShulkerBoxBackPacks.nesting && ShulkerBoxBackPacks.nestingDepth > -1 && ShulkerBoxBackPacks.getNestingDepth(event.getCurrentItem(), 1) >= ShulkerBoxBackPacks.nestingDepth) { event.setCancelled(true); return; }
            }
        }

        // prevent swapping Inventory slot with shulker box (fixes dupe glitch)
        if (event.getAction().name().contains("HOTBAR")) { event.setCancelled(true); return; }

        BlockStateMeta im = (BlockStateMeta)shulkerBox.getItemMeta();
        ShulkerBox shulker = (ShulkerBox) im.getBlockState();

        // set all contents minus most recent item
        shulker.getInventory().setContents(event.getInventory().getContents());

        // set most recent item
        // if (event.getAction() == InventoryAction.DROP_ALL_SLOT)
        //shulker.getInventory().setItem(event.getSlot(), event.getCurrentItem());

        im.setBlockState(shulker);
        shulkerBox.setItemMeta(im);

    }


    //play shulker box close sound on Inventory close
    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event){

        // ensure the Inventory is a Shulker Box Backpack Inventory
        if (Version.getVersion().isBiggerThan(Version.v1_13)) {
            if (!event.getView().getTitle().contains("Shulker Box - In Hand")) return;
        }
        else {
            if (!event.getInventory().getName().contains("Shulker Box - In Hand")) return;
        }

        Player player = (Player) event.getPlayer();

        ItemStack shulkerBox = player.getInventory().getItemInMainHand();

        BlockStateMeta im = (BlockStateMeta)shulkerBox.getItemMeta();
        ShulkerBox shulker = (ShulkerBox) im.getBlockState();

        //set all contents minus most recent item
        shulker.getInventory().setContents(event.getInventory().getContents());
        im.setBlockState(shulker);
        shulkerBox.setItemMeta(im);

        player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_CLOSE, 1, 1);

    }
}



