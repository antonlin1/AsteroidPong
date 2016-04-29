package com.mygdx.game.view.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.AccelerometerInputInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.view.MyGdxGame;


/**
 * Created by johanmansson on 16-04-20.
 */
public abstract class State {

    protected StateManager stateManager;
    protected AccelerometerInputInterface accelerometerInput;
    protected MyGdxGame game;
    protected StateManager.STATE_NAME stateName;
    protected WifiDirectInterface wifiDirect;

    public State(StateManager stateManager, StateManager.STATE_NAME stateName, WifiDirectInterface wifiDirect) {
        this.stateManager = stateManager;
        this.stateName = stateName;
        this.wifiDirect = wifiDirect;
    }

    public StateManager.STATE_NAME getStateName() {
        return  stateName;
    }

    public abstract void update();
    public abstract void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer);

    public abstract boolean isActive();

}
