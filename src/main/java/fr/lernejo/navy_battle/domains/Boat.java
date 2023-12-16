package fr.lernejo.navy_battle.domains;

import java.util.concurrent.atomic.AtomicInteger;

public class Boat {

    private final BoatType boatType;

    public Boat(BoatType boatType) {
        this.boatType = boatType;
    }

    private final AtomicInteger hit = new AtomicInteger(0);

    public boolean isSunk(){
        return hit.get() == boatType.getLength();
    }

    public void increaseHit() {

        this.hit.incrementAndGet();
    }

    public BoatType getBoatType() {
        return boatType;
    }
}