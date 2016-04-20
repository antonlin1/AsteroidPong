package com.mygdx.game.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.AccelerometerInputInterface;
import com.mygdx.game.view.MyGdxGame;


/**
 * Created by johanmansson on 16-04-20.
 */
public abstract class State {

    protected StateManager stateManager;
    protected AccelerometerInputInterface accelerometerInput;
    protected MyGdxGame game;

    public State(StateManager stateManager) {

        this.stateManager = stateManager;



    }


    public abstract void update();
    public abstract void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer);




}
