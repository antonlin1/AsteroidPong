package com.mygdx.game.view.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Stack;

/**
 * Created by johanmansson on 16-04-20.
 */
public class StateManager {

    public enum STATE_NAME {
        HOW_TO_PLAY_STATE, GAME_STATE, MENU_STATE, FIND_GAME_STATE;
    }

    private Stack<com.mygdx.game.view.States.State> states;

    private STATE_NAME activeState;

    public StateManager(){
        states = new Stack<com.mygdx.game.view.States.State>();
    }

    public void push(com.mygdx.game.view.States.State state){
        states.push(state);
        setActiveState(states.peek().getStateName());
    }

    public void pop(){
        states.pop();
        setActiveState(states.peek().getStateName());
    }


    public void set(com.mygdx.game.view.States.State state) {
        states.pop();
        states.push(state);
        setActiveState(states.peek().getStateName());
    }

    private void setActiveState(STATE_NAME state) {
        activeState = state;
        System.out.println("STAAATE: "+activeState);
    }

    public STATE_NAME getActiveState() {
        return activeState;
    }

    public void update(){
        states.peek().update();
    }

    public void render(SpriteBatch spriteBatch,  ShapeRenderer shapeRenderer){
        states.peek().render(spriteBatch, shapeRenderer);
    }

    public boolean lastItem() {
        if(states.size() == 1) {
            return true;
        }
        return false;
    }
}
