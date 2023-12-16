package fr.lernejo.navy_battle.domains;

import java.util.Random;

public class Game {

    public void init(Cell [][] sea, String defaultSymbol){

        for(int i=0;i<=sea.length-1;i++){
            for(int j=0;j<sea.length;j++){
                sea[i][j] = new Cell(null, defaultSymbol);
            }
        }
    }

    public void placeBoat(Cell [][] sea, Boat boat){

        final Random positionRandom = new Random();
        boolean horizontal = positionRandom.nextBoolean(); //Positionnement vertical ou horizontal ?
        boolean conflict = true;

        while(conflict){ //Tant qu'il est des conflits, on cherche à positionner le bateau
            int x = new Random().nextInt(0, horizontal ? 10 - boat.getBoatType().getLength() : 10); //bound is exclusive
            int y = new Random().nextInt(0, horizontal ? 10 : 10 - boat.getBoatType().getLength()); //bound is exclusive
            conflict = isConflict(sea, horizontal, x, y, boat.getBoatType().getLength());
            if(conflict)continue;
            for(int i=horizontal?x:y;i<boat.getBoatType().getLength()+(horizontal?x:y);i++)
                sea[horizontal ? y : i][horizontal ? i : x] = new Cell(boat, boat.getBoatType().getSymbol());
        }
    }


    private boolean isConflict(Cell[][] sea, boolean horizontal, int x, int y, int boatLength){

        for(int i=0;i<boatLength;i++){

            if(horizontal){
                if(!sea[y][x+i].getSymbol().equals("o") && sea[y][x+i].getSymbol() != null)return true;

            }else {
                if(!sea[y+i][x].getSymbol().equals("o") && sea[y+i][x].getSymbol() != null)return true;

            }
        }
        return false;
    }
}
