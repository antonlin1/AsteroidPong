package com.mygdx.game.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


/**
 * Created by johanmansson on 16-04-20.
 */
public abstract class State {

    private StateManager stateManager;

    public State(StateManager stateManager) {

        this.stateManager = stateManager;


    }

    public abstract void update();
    public abstract void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer);



}
