package me.sangca.itemdb;

import me.sangca.itemdb.command.AdminCommand;
import me.sangca.itemdb.service.ItemService;
import me.sangca.itemdb.service.ItemStackTransformationService;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.http.HttpClient;

public class ItemDBPlugin extends JavaPlugin {
    public void onEnable() {
        ItemStackTransformationService transformationService = new ItemStackTransformationService();
        ItemService httpClientService = new ItemService(HttpClient.newHttpClient());
        this.getCommand("itemdb").setExecutor(new AdminCommand(transformationService, httpClientService));
    }
}
