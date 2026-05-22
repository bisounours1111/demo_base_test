package com.example;

public class DiceScore {

    private final Ide de;

    public DiceScore(Ide de) {
        this.de = de;
    }

    public int getScore() {
        int roll1 = de.getRoll();
        int roll2 = de.getRoll();

        if (roll1 == roll2) {
            if (roll1 == 6) {
                return 30;
            }
            return roll1 * 2 + 10;
        }

        return Math.max(roll1, roll2);
    }
}
