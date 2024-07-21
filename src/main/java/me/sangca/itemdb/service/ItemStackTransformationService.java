package me.sangca.itemdb.service;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemStackTransformationService {

    public ItemStackTransformationService() {}

    public String itemStackToBase64(ItemStack itemStack) throws IllegalStateException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeObject(itemStack);
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public ItemStack itemStackFromBase64(String itemStackAsString) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(itemStackAsString));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            return (ItemStack) dataInput.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
