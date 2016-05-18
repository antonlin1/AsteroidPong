package com.mygdx.game.view;

import com.badlogic.gdx.Input;

/**
 * Created by johanmansson on 16-05-16.
 */
public class MyTextInputListener implements Input.TextInputListener {

    String gameName;

    public MyTextInputListener() {
        gameName = "Test Game";
    }

    @Override
    public void input(String text) {

        gameName = text;

    }

    @Override
    public void canceled() {

    }

    public String getGameName() {
        return gameName;
    }

}
