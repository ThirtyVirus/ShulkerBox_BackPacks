package thirtyvirus.sbbp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteStreams;

import thirtyvirus.multiversion.XMaterial;
import thirtyvirus.sbbp.commands.reload;
import thirtyvirus.sbbp.events.UseItem;
import thirtyvirus.sbbp.events.inventory;

public class ShulkerBoxBackPacks extends JavaPlugin  {

    // config and IO
    private PluginDescriptionFile descFile = getDescription();
    private PluginManager pm = getServer().getPluginManager();

    private FileConfiguration config;
    private Logger logger = getLogger();

    //Permissions
    private Permission use = new Permission("ShulkerBoxBackPacks.use");
    private Permission nest = new Permission("ShulkerBoxBackPacks.nesting");
    private Permission reload = new Permission("ShulkerBoxBackPacks.reload");

    public static final List<Material> supportedMaterials = Arrays.asList(XMaterial.SHULKER_BOX.parseMaterial(), Material.BLACK_SHULKER_BOX,
            Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.GRAY_SHULKER_BOX,
            Material.GREEN_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, XMaterial.LIGHT_GRAY_SHULKER_BOX.parseMaterial(), Material.LIME_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.PURPLE_SHULKER_BOX,
            Material.RED_SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX);

    //Settings
    public static boolean nesting = false;
    public static int nestingDepth = -1;

    public static boolean useNamedBoxes = true;
    public static boolean useFormattedNamedBoxes = true;

    public void onEnable(){

        //load config.yml (generate one if not there)
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()){
            loadResource(getPlugin(ShulkerBoxBackPacks.class), "config.yml");
        }

        loadConfiguration();

        getCommand("sbbpReload").setExecutor(new reload(this));;
        registerEvents();
        registerPermissions();

        //posts confirmation in chat
        logger.info(descFile.getName() + " V: " + descFile.getVersion() + " has been enabled");
    }

    public void onDisable(){
        //posts exit message in chat
        logger.info(descFile.getName() + " V: " + descFile.getVersion() + " has been disabled");
    }

    public void registerEvents(){
        pm.registerEvents(new UseItem(), this);
        pm.registerEvents(new inventory(), this);
    }

    public void registerPermissions(){
        pm.addPermission(use);
        pm.addPermission(nest);
        pm.addPermission(reload);
    }

    //load config settings
    public void loadConfiguration(){
        config = this.getConfig();

        use = new Permission(config.getString("permission"));
        nest = new Permission(config.getString("nesting-permission"));
        nesting = config.getBoolean("nesting");
        nestingDepth = config.getInt("nesting-depth");
        useNamedBoxes = config.getBoolean("use-renamed-boxes");
        useFormattedNamedBoxes = config.getBoolean("use-formatted-renamed-boxes");
    }

    //Loads file from JAR with comments
    private File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResource(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }


    public static int getNestingDepth(ItemStack shulkerBox, int currentDepth){

        //make arraylist of all shulker boxes in current box
        BlockStateMeta im = (BlockStateMeta)shulkerBox.getItemMeta();
        ShulkerBox shulker = (ShulkerBox) im.getBlockState();
        ItemStack[] items = shulker.getInventory().getContents();

        ArrayList<ItemStack> boxes = new ArrayList<ItemStack>();
        for (ItemStack item : items) {
            if (item == null) continue;
            if (supportedMaterials.contains(item.getType())) boxes.add(item);
        }

        //determine maximum depth of boxes contained in current box
        int maxDepth = currentDepth;
        for (ItemStack box : boxes){
            int nextDepth = getNestingDepth(box, currentDepth + 1);
            if (nextDepth > maxDepth) maxDepth = nextDepth;
        }

        return maxDepth;
    }

}
