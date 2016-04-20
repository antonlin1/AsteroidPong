package com.mygdx.game.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Stack;

/**
 * Created by johanmansson on 16-04-20.
 */
public class StateManager {

    private Stack<State> states;

    public StateManager(){
        states = new Stack<State>();
    }

    public void push(State state){
        states.push(state);
    }

    public void pop(){
        states.pop();
    }


    public void set(State state){
        states.pop();
        states.push(state);
    }

    public void update(){
        states.peek().update();
    }

    public void render(SpriteBatch spriteBatch,  ShapeRenderer shapeRenderer){
        states.peek().render(spriteBatch, shapeRenderer);
    }


}
