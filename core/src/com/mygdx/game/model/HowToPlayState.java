package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by johanmansson on 16-04-26.
 */
public class HowToPlayState extends State {

    private Texture cancel;

    public HowToPlayState(StateManager stateManager) {
        super(stateManager);
        cancel = new Texture("cancel2.png");


    }


    @Override
    public void update() {
        handleInput();

    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {

        spriteBatch.begin();
        spriteBatch.draw(cancel, Gdx.graphics.getWidth() - cancel.getWidth() - 20, 20);
        spriteBatch.end();


    }

    public void handleInput() {
        if(Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();

            float x1 = Gdx.graphics.getWidth() - cancel.getWidth() - 20;
            float x2 = Gdx.graphics.getWidth() + cancel.getWidth() - 20;
            float y1 = 20;
            float y2 = cancel.getHeight() + 20;

            if(x > x1 && x < x2 && y > y1 && y < y2) {
                System.out.println("Button pressed");
                stateManager.pop();

            }

        }


    }
}
