package me.sangca.itemdb.command;

import me.sangca.itemdb.entity.SerializedItemStack;
import me.sangca.itemdb.service.ItemStackTransactionService;
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
    public ItemStackTransactionService transactionService;

    public AdminCommand(ItemStackTransformationService transformationService, ItemStackTransactionService transactionService) {
        this.transformationService = transformationService;
        this.transactionService = transactionService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cYou should be a player to use this command.");
            return true;
        }

        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "register":
                    sender.sendMessage("§c/itemdb register <category> <key> : registers an item to a category with a unique key.");
                    return true;

                case "delete":
                    sender.sendMessage("§c/itemdb delete <category> <key> : deletes an item in a category with a unique key.");
                    return true;

                case "spawn":
                    sender.sendMessage("§c/itemdb spawn <category> <key> : spawns an item by its category and unique key.");
                    return true;

                case "list":
                    if (Integer.parseInt(args[1]) != (int)Integer.parseInt(args[1])){
                        sender.sendMessage("§cIncorrect usage. /itemdb list <page>");
                        return true;
                    }

                    List<SerializedItemStack> serializedItemStackList;
                    try {
                        serializedItemStackList = transformationService.getItemListFromString(transactionService.getEncodedItemStackList(args[1]));
                    } catch (IOException e) {
                        sender.sendMessage("§cIOException occurred");
                        return true;
                    } catch (InterruptedException e) {
                        sender.sendMessage("§cInterruptedException occurred");
                        return true;
                    }

                    for (SerializedItemStack serializedItemStack : serializedItemStackList) {

                        ItemStack itemStack;
                        try {
                            itemStack = transformationService.encodeItemStackFromBase64(serializedItemStack.getItemStackAsString());
                        } catch (IOException e) {
                            sender.sendMessage("§cIOException occurred");
                            return true;
                        } catch (ClassNotFoundException e) {
                            sender.sendMessage("§cClassNotFoundException occurred");
                            return true;
                        }

                        sender.sendMessage(String.format("Category: %s Key: %s ItemName: %s ItemType: %s",
                            serializedItemStack.getCategory(),
                            serializedItemStack.getKey(),
                            itemStack.getItemMeta().getDisplayName(),
                            itemStack.getType().getTranslationKey()));
                    }

                    sender.sendMessage(String.format("Current Page: %s", args[1]));
                    return true;
            }
        }

        if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "register":
                    ItemStack heldItem = ((Player) sender).getInventory().getItemInMainHand();

                    try {
                        String encodedString = transformationService.encodeItemStackToBase64(heldItem);
                        transactionService.postEncodedItemStackWithCategoryAndKey(encodedString, args[1], args[2]);
                    } catch (IOException e) {
                        sender.sendMessage("§cIOException occurred");
                        return true;
                    } catch (InterruptedException e) {
                        sender.sendMessage("§cInterruptedException occurred");
                        return true;
                    }

                    sender.sendMessage("Item successfully registered.");
                    return true;

                case "delete":
                    try {
                        transactionService.deleteEncodedItemStackWithCategoryAndKey(args[1], args[2]);
                    } catch (IOException e) {
                        sender.sendMessage("§cIOException occurred");
                        return true;
                    } catch (InterruptedException e) {
                        sender.sendMessage("§cInterruptedException occurred");
                        return true;
                    }

                    sender.sendMessage("Item successfully deleted.");
                    return true;

                case "spawn":
                    ItemStack itemStack;
                    try {
                        itemStack = transformationService.encodeItemStackFromBase64(transactionService.getEncodedItemStackWithCategoryAndKey(args[1], args[2]));
                    } catch (IOException e) {
                        sender.sendMessage("§cIOException occurred");
                        return true;
                    } catch (InterruptedException e) {
                        sender.sendMessage("§cInterruptedException occurred");
                        return true;
                    } catch (ClassNotFoundException e) {
                        sender.sendMessage("§cClassNotFoundException occurred");
                        return true;
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

