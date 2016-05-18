package com.mygdx.game.view;

import com.badlogic.gdx.Input;

/**
 * Created by johanmansson on 16-05-16.
 */
public class MyTextInputListener implements Input.TextInputListener {

    public enum Outcome {
        NOT_SET, CANCELLED, NAME_SET;
    }

    private Outcome outcome;
    private String gameName;

    public MyTextInputListener() {
        gameName = "";
        outcome = Outcome.NOT_SET;
    }

    @Override
    public void input(String text) {
        gameName = text;
        outcome = Outcome.NAME_SET;
    }

    @Override
    public void canceled() {
        outcome = Outcome.CANCELLED;
    }

    public Outcome getOutcome() {
        return  outcome;
    }

    public String getGameName() {
        return gameName;
    }

}
