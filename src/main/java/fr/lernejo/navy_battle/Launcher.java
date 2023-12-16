package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;
import fr.lernejo.navy_battle.domains.*;
import fr.lernejo.navy_battle.handler.GameFireHandler;
import fr.lernejo.navy_battle.handler.GameStartHandler;
import fr.lernejo.navy_battle.handler.PingHandler;
import fr.lernejo.navy_battle.utils.HttpUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Launcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        final Cell sea[][] = new Cell[10][10]; //Création de la mer (carte du jeu 10x10)
        Game game = new Game();
        game.init(sea, "o"); //Création d'une Cell vide pour chaque emplacement
        placeBoats(game, sea); //Placements des bateaux
        final HttpServer httpServer = initServer(Integer.parseInt(args[0]));
        GameStartHandler startHandler = new GameStartHandler(Integer.parseInt(args[0]));
        httpServer.createContext("/api/game/start", startHandler);
        if (args.length == 2) httpServer.createContext("/api/game/fire", new GameFireHandler(sea, new AtomicInteger(Integer.parseInt(args[1].split(":")[2])), startHandler));
        httpServer.start();
        if (args.length == 2) System.out.println(new HttpUtils().postRequest(args[1], Integer.parseInt(args[0]))); //appel du start
        else httpServer.createContext("/api/game/fire", new GameFireHandler(sea, new AtomicInteger(-1), startHandler));
    }

    private static void placeBoats(final Game game, final Cell sea[][]){

        //Positionnement des différents bateaux
        game.placeBoat(sea, new Boat(BoatType.PORTE_AVION));
        game.placeBoat(sea, new Boat(BoatType.CROISEUR));
        game.placeBoat(sea, new Boat(BoatType.CONTRE_TORPILLEUR));
        game.placeBoat(sea, new Boat(BoatType.CONTRE_TORPILLEUR));
        game.placeBoat(sea, new Boat(BoatType.TORPILLEUR));
    }

    private static HttpServer initServer(int port) throws IOException{
        final HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.setExecutor(Executors.newSingleThreadExecutor());
        httpServer.createContext("/ping", new PingHandler());
        return httpServer;
    }
}