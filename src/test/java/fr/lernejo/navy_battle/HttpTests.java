package fr.lernejo.navy_battle;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lernejo.navy_battle.domains.Boat;
import fr.lernejo.navy_battle.domains.BoatType;
import fr.lernejo.navy_battle.domains.Cell;
import fr.lernejo.navy_battle.domains.Game;
import fr.lernejo.navy_battle.dto.NavyBattleDTO;
import fr.lernejo.navy_battle.handler.GameFireHandler;
import fr.lernejo.navy_battle.handler.GameStartHandler;
import fr.lernejo.navy_battle.handler.PingHandler;
import fr.lernejo.navy_battle.utils.HttpUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTests {

    @Test
    void pingHandlerRespondsWith200() throws Exception {
        String host = System.getProperty("http.server.host");
        String port = System.getProperty("http.server.port");
        URL url = URI.create("http://" + host + ":" + port + "/ping").toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        assertEquals(202, responseCode);
    }

    @Test
    void gameStartHandlerRespondsWith404() throws Exception {
        String host = System.getProperty("http.server.host");
        String port = System.getProperty("http.server.port");
        URL url = URI.create("http://" + host + ":" + port + "/api/game/start").toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        assertEquals(404, responseCode);
    }

    @Test
    void gameStartHandlerRespondsWith400() throws Exception {
        String host = System.getProperty("http.server.host");
        String port = System.getProperty("http.server.port");
        URL url = URI.create("http://" + host + ":" + port + "/api/game/start").toURL();

        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = "{\"key\":\"1\", \"value\":\"2\"}";

        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        assertEquals(400, responseCode);

    }

    @Test
    void gameStartHandlerRespondsWith200() throws Exception {
        String host = System.getProperty("http.server.host");
        String port = System.getProperty("http.server.port");
        URL url = URI.create("http://" + host + ":" + port + "/api/game/start").toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        NavyBattleDTO request = new NavyBattleDTO(UUID.randomUUID().toString(),"http://localhost:" + port , "I will crush you");


        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = new ObjectMapper().writeValueAsString(request).getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        assertEquals(202, responseCode);

    }

    @Test
    void gameFireHandlerRespondsWith404() throws IOException {
        String host = System.getProperty("http.server.host");
        String port = System.getProperty("http.server.port");
        URL url = URI.create("http://" + host + ":" + port + "/api/game/fire").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        int responseCode = connection.getResponseCode();
        assertEquals(404, responseCode);
    }

    @Test
    void gameFireHandlerRespondsWith400NullQuery() throws IOException {
        String host = System.getProperty("http.server.host");
        String port = System.getProperty("http.server.port");
        URL url = URI.create("http://" + host + ":" + port + "/api/game/fire").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");


        int responseCode = connection.getResponseCode();
        assertEquals(400, responseCode);
    }

    @Test
    void gameFireHandlerRespondsWith400InvalidQuery() throws IOException {
        String host = System.getProperty("http.server.host");
        String port = System.getProperty("http.server.port");
        URL url = URI.create("http://" + host + ":" + port + "/api/game/fire?mykey=myvalue").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(400, responseCode);
    }

   /* @Test
    void gameFireHandlerRespondsWith200() throws IOException {
        final Cell sea[][] = new Cell[10][10]; //Création de la mer (carte du jeu 10x10)
        Game.init(sea, "o"); //Création d'une Cell vide pour chaque emplacement
        Game.placeBoat(sea, new Boat(BoatType.PORTE_AVION));
        Game.placeBoat(sea, new Boat(BoatType.CROISEUR));
        Game.placeBoat(sea, new Boat(BoatType.CONTRE_TORPILLEUR));
        Game.placeBoat(sea, new Boat(BoatType.CONTRE_TORPILLEUR));
        Game.placeBoat(sea, new Boat(BoatType.TORPILLEUR));

        String host = System.getProperty("http.server.host");
        String port = System.getProperty("http.server.port");
        URL url = URI.create("http://" + host + ":" + port + "/api/game/fire?cell=A1").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");


        int responseCode = connection.getResponseCode();
        assertEquals(202, responseCode);
    }*/


}
