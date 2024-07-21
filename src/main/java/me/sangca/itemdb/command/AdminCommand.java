package me.sangca.itemdb.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.sangca.itemdb.entity.SortedItemStack;
import me.sangca.itemdb.service.ItemService;
import me.sangca.itemdb.service.ItemStackTransformationService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class AdminCommand implements CommandExecutor {
    public ItemStackTransformationService transformationService;
    public ItemService itemService;

    public AdminCommand(ItemStackTransformationService transformationService, ItemService httpClientService) {
        this.transformationService = transformationService;
        this.itemService = httpClientService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cYou should be a player to use this command.");
            return true;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("register")) {
                sender.sendMessage("§c/itemdb register <category> <key> : registers an item to a category with a unique key.");
                return true;
            }
            if (args[0].equalsIgnoreCase("delete")) {
                sender.sendMessage("§c/itemdb delete <category> <key> : deletes an item in a category with a unique key.");
                return true;
            }
            if (args[0].equalsIgnoreCase("spawn")) {
                sender.sendMessage("§c/itemdb spawn <category> <key> : spawns an item by its category and unique key.");
                return true;
            }
            if (args[0].equalsIgnoreCase("list")) {
                if (Integer.parseInt(args[1]) != (int)Integer.parseInt(args[1])){
                    sender.sendMessage("§cIncorrect usage. /itemdb list <page>");
                    return true;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                List<SortedItemStack> sortedItemStackList;
                try {
                    sortedItemStackList = objectMapper.readValue(itemService.getEncodedItemStackList(args[1]), new TypeReference<List<SortedItemStack>>(){});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                for (SortedItemStack sortedItemStack : sortedItemStackList) {

                    ItemStack itemStack;
                    try {
                        itemStack = transformationService.itemStackFromBase64(sortedItemStack.getItemStackAsString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    sender.sendMessage(String.format("Category: %s Key: %s ItemName: %s ItemType: %s",
                        sortedItemStack.getCategory(),
                        sortedItemStack.getKey(),
                        itemStack.getItemMeta().getDisplayName(),
                        itemStack.getType().getItemTranslationKey()));
                }
                sender.sendMessage(String.format("Current Page: %s", args[1]));
                return true;
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("register")) {

                ItemStack heldItem = ((Player) sender).getInventory().getItemInMainHand();
                String encodedString = transformationService.itemStackToBase64(heldItem);
                try {
                    itemService.postEncodedItemStackWithCategoryAndKey(encodedString, args[1], args[2]);
                }
                catch (IOException | InterruptedException e) {
                    sender.sendMessage("Item weird.");
                }
                sender.sendMessage("Item successfully registered.");
                return true;
            }
            if (args[0].equalsIgnoreCase("delete")) {

                itemService.deleteEncodedItemStackWithCategoryAndKey(args[1], args[2]);
                sender.sendMessage("Item successfully deleted.");
                return true;
            }
            if (args[0].equalsIgnoreCase("spawn")) {

                ItemStack itemStack;
                try {
                    itemStack = transformationService.itemStackFromBase64(itemService.getEncodedItemStackWithCategoryAndKey(args[1], args[2]));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                ((Player) sender).getInventory().addItem(itemStack);
                sender.sendMessage("Item successfully spawned.");
                return true;
            }
        }

        String[] helpMessage = new String[4];
        helpMessage[0] = "§c/itemdb list <page> : shows the list of items in a page.";
        helpMessage[1] = "§c/itemdb register <category> <key> : registers an item to a category with a unique key.";
        helpMessage[2] = "§c/itemdb delete <category> <key> : deletes item in a category from a database.";
        helpMessage[3] = "§c/itemdb spawn <category> <key> : spawns an item by its category and unique key.";
        sender.sendMessage(helpMessage);
        return true;
    }
}

