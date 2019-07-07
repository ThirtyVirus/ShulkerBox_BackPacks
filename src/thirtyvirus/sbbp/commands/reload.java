package thirtyvirus.sbbp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import thirtyvirus.sbbp.ShulkerBoxBackPacks;

public class reload implements CommandExecutor {

    public ShulkerBoxBackPacks main = null;
    public reload(ShulkerBoxBackPacks main){
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender.hasPermission("ShulkerBoxBackPacks.reload")){
            main.reloadConfig();
            main.loadConfiguration();

            sender.sendMessage("[ShulkerBox BackPacks] config reloaded!");
        }

        return true;
    }

}
