package me.sangca.itemdb.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ItemService {
    public final HttpClient httpClient;

    public ItemService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String getEncodedItemStackList(String page) {
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(String.format("http://127.0.0.1:8080/itemlist/%s",page)))
            .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response.body();
    }

    public void postEncodedItemStackWithCategoryAndKey(String itemStackAsString, String category, String key) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(itemStackAsString))
            .uri(URI.create(String.format("http://127.0.0.1:8080/items/%s/%s",category,key)))
            .build();

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void deleteEncodedItemStackWithCategoryAndKey(String category, String key) {
        HttpRequest request = HttpRequest.newBuilder()
            .DELETE()
            .uri(URI.create(String.format("http://127.0.0.1:8080/items/%s/%s",category,key)))
            .build();

        try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEncodedItemStackWithCategoryAndKey(String category, String key) {
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(String.format("http://127.0.0.1:8080/items/%s/%s",category,key)))
            .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response.body();
    }
}
