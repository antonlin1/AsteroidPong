package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.mygdx.game.AccelerometerInputInterface;
import com.mygdx.game.view.MyGdxGame;

/**
 * Created by johanmansson on 16-04-20.
 */
public class MenuState extends State {
    private Texture button1;



    public MenuState(StateManager stateManager) {
        super(stateManager);
        button1 = new Texture("button2.png");


    }



    @Override
    public void update() {
        handleInput();

    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {

        spriteBatch.begin();
        spriteBatch.draw(button1, (Gdx.graphics.getWidth() / 2) - (button1.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (button1.getHeight() / 2));
        spriteBatch.end();


    }

    public void handleInput() {

        if(Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();

            float x1 = (Gdx.graphics.getWidth() / 2) - (button1.getWidth() / 2);
            float x2 = (Gdx.graphics.getWidth() / 2) + (button1.getWidth() / 2);
            float y1 = (Gdx.graphics.getHeight() / 2) - (button1.getHeight() / 2);
            float y2 = (Gdx.graphics.getHeight() / 2) + (button1.getHeight() / 2);

            if(x > x1 && x < x2 && y > y1 && y < y2) {
                System.out.println("Button pressed");
                stateManager.pop();


            }

        }

    }
}
