package fr.lernejo.navy_battle.utils;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import fr.lernejo.navy_battle.handler.GameFireHandler;
import fr.lernejo.navy_battle.handler.GameStartHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;

public class HttpUtils {

    public void handleResponse(final HttpExchange exchange, int httpCode, final String body) throws IOException{

        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "application/json");
        exchange.sendResponseHeaders(httpCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write( body.getBytes());
        }
    }

    public String postRequest(final String clientAddress, int myPort) throws IOException, InterruptedException {
        return HttpClient.newHttpClient().send(
                HttpRequest.newBuilder().uri(
                        URI
                            .create(clientAddress + "/api/game/start")
                    ).setHeader("Accept", "application/json").setHeader("Content-Type", "application/json")
                    .POST(
                        HttpRequest.BodyPublishers.ofString("{\"id\":\"1\", \"url\":\"http://localhost:" + myPort + "\", \"message\":\"hello\"}")
                    ).build(),
                HttpResponse.BodyHandlers.ofString()).body();
    }

    public String getRequest(final String clientAddress, final String cell) throws IOException, InterruptedException{
        return HttpClient.newHttpClient().send(
                    HttpRequest.newBuilder().uri(
                            URI.create(clientAddress + "/api/game/fire?cell="+cell))
                        .setHeader("Accept", "application/json").setHeader("Content-Type", "application/json").GET()
                        .build(),HttpResponse.BodyHandlers.ofString()
                ).body();
    }
}