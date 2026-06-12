package com.example;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    private int score;
    private boolean lastFrame;
    private IGenerateur generateur;
    private List<Roll> rolls;

    public Frame(IGenerateur generateur, boolean lastFrame) {
        this.lastFrame = lastFrame;
        this.generateur = generateur;
        this.rolls = new ArrayList<>();
    }

    public boolean makeRoll() {
        if (!canRoll()) {
            return false;
        }
        int pins = generateur.randomPin(getMaxPins());
        rolls.add(new Roll(pins));
        score += pins;
        return true;
    }

    public int getScore() {
        return score;
    }

    private boolean canRoll() {
        if (rolls.isEmpty()) {
            return true;
        }
        if (!lastFrame) {
            if (rolls.size() >= 2) {
                return false;
            }
            return !isStrike(0);
        }
        if (rolls.size() >= 3) {
            return false;
        }
        if (rolls.size() == 1) {
            return true;
        }
        return isStrike(0) || isSpare();
    }

    private int getMaxPins() {
        if (rolls.isEmpty()) {
            return 10;
        }
        if (lastFrame && (rolls.size() >= 2 || isStrike(0))) {
            return 10;
        }
        return 10 - rolls.get(0).getPins();
    }

    private boolean isStrike(int index) {
        return rolls.get(index).getPins() == 10;
    }

    private boolean isSpare() {
        return rolls.get(0).getPins() + rolls.get(1).getPins() == 10;
    }
}
