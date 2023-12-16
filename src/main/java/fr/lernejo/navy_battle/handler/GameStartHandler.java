package fr.lernejo.navy_battle.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import fr.lernejo.navy_battle.dto.NavyBattleDTO;
import fr.lernejo.navy_battle.utils.HttpUtils;
import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class GameStartHandler implements HttpHandler {

    private final int port;

    private final AtomicInteger adversaryPort = new AtomicInteger(-1);

    public GameStartHandler(int port) {
        this.port = port;
    }

    public AtomicInteger getAdversaryPort() {
        return adversaryPort;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) new HttpUtils().handleResponse(exchange, 404, "");
        else {
            try (final InputStream inputStream = getClass().getResourceAsStream("/schema.json")) {
                final Schema schema = SchemaLoader.load(new JSONObject(new JSONTokener(inputStream)));
                try {
                    final JSONObject jsonObject = new JSONObject(IOUtils.toString(exchange.getRequestBody(), StandardCharsets.UTF_8));
                    schema.validate(jsonObject);
                    adversaryPort.set(Integer.parseInt(jsonObject.getString("url").split(":")[2]));
                    new HttpUtils().handleResponse(exchange, 202, new ObjectMapper().writeValueAsString(new NavyBattleDTO(UUID.randomUUID().toString(), "http://localhost:" + port, "I will crush you")));
                    System.out.println(new HttpUtils().getRequest("http://localhost:" + Integer.parseInt(jsonObject.getString("url").split(":")[2]), String.valueOf(numToLetterBySubstr(new Random().nextInt(1, 11))) + new Random().nextInt(1, 11)));
                } catch (ValidationException | JSONException | InterruptedException e) {
                    exchange.sendResponseHeaders(400, 0);
                }
            }
        }
    }

    private static char numToLetterBySubstr(int i) {
        String letters = "ABCDEFGHIJ";

        return letters.substring(i - 1, i).charAt(0);

    }
}