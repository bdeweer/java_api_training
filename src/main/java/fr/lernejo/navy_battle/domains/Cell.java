package fr.lernejo.navy_battle.domains;

public class Cell {

    private final Boat boat;
    private final String symbol;

    public Cell(final Boat boat, final String symbol){
        this.boat = boat;
        this.symbol = symbol;
    }

    public Boat getBoat() {
        return boat;
    }

    /*public void setBoat(Boat boat) {
        this.boat = boat;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }*/

    public String getSymbol() {
        return symbol;
    }
}
