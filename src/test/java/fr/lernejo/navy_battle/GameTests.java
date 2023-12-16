package fr.lernejo.navy_battle;

import fr.lernejo.navy_battle.domains.Boat;
import fr.lernejo.navy_battle.domains.BoatType;
import fr.lernejo.navy_battle.domains.Cell;
import fr.lernejo.navy_battle.domains.Game;
import org.junit.jupiter.api.Test;



public class GameTests {

    @Test
    void testPlaceBoat() {

        Game game = new Game();
        final Cell sea[][] = new Cell[10][10]; //Création de la mer (carte du jeu 10x10)
        game.init(sea, "o");
        game.placeBoat(sea, new Boat(BoatType.PORTE_AVION));

    }
}
