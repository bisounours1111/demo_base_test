package com.example;

import jdk.jshell.spi.ExecutionControl;

public class DiceScore {

    private final Ide de;

    public DiceScore(Ide de) {
        this.de = de;
    }

    public int getScore() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not implemented function");
    }
}
