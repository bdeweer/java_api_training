package fr.lernejo.navy_battle.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import fr.lernejo.navy_battle.domains.Cell;
import fr.lernejo.navy_battle.domains.Consequence;
import fr.lernejo.navy_battle.dto.HitDTO;
import fr.lernejo.navy_battle.utils.HttpUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GameFireHandler implements HttpHandler {

    private final Cell[][] sea;
    private final GameStartHandler startHandler;
    private final AtomicInteger port;
    private final AtomicInteger hitCell;

    public GameFireHandler(final Cell [][] sea,  final AtomicInteger port, GameStartHandler startHandler){
        this.startHandler = startHandler;
        this.sea = sea;
        this.port = port;
        this.hitCell = new AtomicInteger(0);
    }

    @Override
    public void handle(HttpExchange exchange)throws   IOException {

        if (!exchange.getRequestMethod().equals("GET")) new HttpUtils().handleResponse(exchange, 404, "");
        else if (exchange.getRequestURI().getQuery() == null) new HttpUtils().handleResponse(exchange, 400, "");
         else {
            final String[] query = exchange.getRequestURI().getQuery().split("=");
            if(query.length > 2 || !query[0].equals("cell")) new HttpUtils().handleResponse(exchange, 400, "");
            else handleFire(query, exchange);
        }
    }

    private static char numToLetterBySubstr(int i) {
        String letters = "ABCDEFGHIJ";

        return letters.substring(i - 1, i).charAt(0);
    }

    private void fire() throws IOException{
        if(hitCell.get()<17){
            int xFire = new Random().nextInt(1, 11);
            int yFire =  new Random().nextInt(1, 11);
            try {
                if(port.get() == -1){
                    port.set(startHandler.getAdversaryPort().get());
                }
                new HttpUtils().getRequest("http://localhost:"+port, String.valueOf(numToLetterBySubstr(xFire)) + yFire);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean sunk(boolean miss, int x, int y){
        if(!miss){
            hitCell.incrementAndGet();
            sea[x-1][y-1].getBoat().increaseHit();
            return sea[x-1][y-1].getBoat().isSunk();
        }
        return false;
    }

    private String test(final InputStream inputStream, boolean miss, boolean sunk) throws JsonProcessingException {
        final Schema schema = SchemaLoader.load(new JSONObject(new JSONTokener(inputStream)));
        final HitDTO hitDTO = new HitDTO(miss ? Consequence.MISS.name().toLowerCase() : sunk ? Consequence.SUNK.name().toLowerCase() : Consequence.HIT.name().toLowerCase(), hitCell.get() >= 17);
        final String hitDTOString = new ObjectMapper().writeValueAsString(hitDTO);
        final JSONObject jsonObject = new JSONObject(hitDTOString);
        schema.validate(jsonObject);
        return hitDTOString;
    }

    private void handleFire(final String [] query, final HttpExchange exchange) throws IOException {
        int x = Integer.parseInt(query[1].length() == 2 ? query[1].substring(1,2) : query[1].substring(1,3));
        int y = query[1].substring(0,1).toLowerCase().charAt(0) - 'a' + 1;

        try (final InputStream inputStream = getClass().getResourceAsStream("/fire-schema.json")) {
            try {
                boolean miss = sea[x-1][y-1].getSymbol().equals("o");
                new HttpUtils().handleResponse(exchange, 202, test(inputStream, miss, sunk(miss, x, y)));
                fire();
            } catch (ValidationException | JSONException e) {
                new HttpUtils().handleResponse(exchange, 400, "");
            }
        }
    }
}