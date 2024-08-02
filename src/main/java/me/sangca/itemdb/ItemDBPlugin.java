package me.sangca.itemdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.sangca.itemdb.command.AdminCommand;
import me.sangca.itemdb.service.ItemStackTransactionService;
import me.sangca.itemdb.service.ItemStackTransformationService;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.http.HttpClient;

public class ItemDBPlugin extends JavaPlugin {
    public void onEnable() {
        ItemStackTransformationService transformationService = new ItemStackTransformationService(new ObjectMapper());
        ItemStackTransactionService httpClientService = new ItemStackTransactionService(HttpClient.newHttpClient());
        this.getCommand("itemdb").setExecutor(new AdminCommand(transformationService, httpClientService));
    }
}
