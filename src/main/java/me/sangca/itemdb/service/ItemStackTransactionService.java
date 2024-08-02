package me.sangca.itemdb.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ItemStackTransactionService {
    public final HttpClient httpClient;

    public ItemStackTransactionService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String getEncodedItemStackList(String page) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(String.format("http://127.0.0.1:8080/items?page=%s",page)))
            .build();

        HttpResponse<String> response;
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public void postEncodedItemStackWithCategoryAndKey(String itemStackAsString, String category, String key) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(itemStackAsString))
            .uri(URI.create(String.format("http://127.0.0.1:8080/items/%s/%s",category,key)))
            .build();

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void deleteEncodedItemStackWithCategoryAndKey(String category, String key) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .DELETE()
            .uri(URI.create(String.format("http://127.0.0.1:8080/items/%s/%s",category,key)))
            .build();

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String getEncodedItemStackWithCategoryAndKey(String category, String key) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(String.format("http://127.0.0.1:8080/items/%s/%s",category,key)))
            .build();

        HttpResponse<String> response;
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
