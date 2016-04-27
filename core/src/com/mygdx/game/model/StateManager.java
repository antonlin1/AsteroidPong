package com.mygdx.game.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Stack;

/**
 * Created by johanmansson on 16-04-20.
 */
public class StateManager {

    public enum STATE_NAME {
        HOW_TO_PLAY_STATE, GAME_STATE, MENU_STATE;
    }

    private Stack<State> states;

    private STATE_NAME activeState;

    public StateManager(){
        states = new Stack<State>();
    }

    public void push(State state){
        states.push(state);
        setActiveState(states.peek().getStateName());
    }

    public void pop(){
        states.pop();
        setActiveState(states.peek().getStateName());
    }


    public void set(State state) {
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
