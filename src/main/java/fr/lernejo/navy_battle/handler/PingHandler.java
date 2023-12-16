package fr.lernejo.navy_battle.handler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.lernejo.navy_battle.utils.HttpUtils;

import java.io.IOException;
import java.io.OutputStream;

public class PingHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String body = "OK";
        exchange.sendResponseHeaders(202, body.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body.getBytes());
        }
    }
}
