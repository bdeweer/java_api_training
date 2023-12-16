package fr.lernejo.navy_battle.domains;

public enum BoatType {

    PORTE_AVION(5, "p"), CROISEUR(4, "c"), CONTRE_TORPILLEUR(3, "x"), TORPILLEUR(2, "t");

    BoatType(int length, String symbol){
        this.length = length; //longueur du bateau
        this.symbol = symbol; //symbol représenté sur la carte
    }
    private final int length;
    private final String symbol;

    public int getLength() {
        return length;
    }

    public String getSymbol() {
        return symbol;
    }
}
