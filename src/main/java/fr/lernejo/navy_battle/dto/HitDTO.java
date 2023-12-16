package fr.lernejo.navy_battle.dto;

public class HitDTO {
    private final String consequence;
    private final boolean shipLeft;

    public HitDTO(String consequence, boolean shipLeft) {
        this.consequence = consequence;
        this.shipLeft = shipLeft;
    }

    public String getConsequence() {
        return consequence;
    }

    public boolean isShipLeft() {
        return shipLeft;
    }
}