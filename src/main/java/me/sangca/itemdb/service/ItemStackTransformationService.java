package me.sangca.itemdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.sangca.itemdb.entity.SerializedItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ItemStackTransformationService {
    public final ObjectMapper objectMapper;

    public ItemStackTransformationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String encodeItemStackToBase64(ItemStack itemStack) throws IllegalStateException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

        dataOutput.writeObject(itemStack);
        return Base64Coder.encodeLines(outputStream.toByteArray());
    }

    public ItemStack encodeItemStackFromBase64(String itemStackAsString) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(itemStackAsString));
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

        return (ItemStack) dataInput.readObject();
    }

    public List<SerializedItemStack> getItemListFromString(String itemListAsString) throws JsonProcessingException {

        return objectMapper.readValue(itemListAsString, new TypeReference<List<SerializedItemStack>>(){});
    }
}
