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
    private Texture button2;
    private Texture text1;



    public MenuState(StateManager stateManager) {
        super(stateManager);
        button1 = new Texture("button3.png");
        button2 = new Texture("button4.png");
        text1 = new Texture("text1.png");


    }



    @Override
    public void update() {
        handleInput();

    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {

        spriteBatch.begin();
        spriteBatch.draw(text1, (Gdx.graphics.getWidth() / 2) - (text1.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (text1.getHeight() / 2) - 150);
        spriteBatch.draw(button1, (Gdx.graphics.getWidth() / 2) - (button1.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (button1.getHeight() / 2) + 300);
        spriteBatch.draw(button2, (Gdx.graphics.getWidth() / 2) - (button2.getWidth() / 2), (Gdx.graphics.getHeight() / 2) + 340 + (button1.getHeight()/2));
        spriteBatch.end();


    }

    public void handleInput() {

        if(Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();

            float x1 = (Gdx.graphics.getWidth() / 2) - (button1.getWidth() / 2);
            float x2 = (Gdx.graphics.getWidth() / 2) + (button1.getWidth() / 2);
            float y1 = (Gdx.graphics.getHeight() / 2) - (button1.getHeight() / 2) + 300;
            float y2 = (Gdx.graphics.getHeight() / 2) + (button1.getHeight() / 2) + 300;

            if(x > x1 && x < x2 && y > y1 && y < y2) {
                System.out.println("Button1 pressed");
                stateManager.pop();


            }

            float x21 = (Gdx.graphics.getWidth() / 2) - (button2.getWidth() / 2);
            float x22 = (Gdx.graphics.getWidth() / 2) + (button2.getWidth() / 2);
            float y21 = (Gdx.graphics.getHeight() / 2) + 340 - (button2.getHeight() / 2);
            float y22 = (Gdx.graphics.getHeight() / 2) + 340 + (button2.getHeight() / 2);

            if(x > x21 && x < x22 && y > y21 && y < y22) {
                System.out.println("Button2 pressed");
                //stateManager.push(new HowToPlayState(stateManager));
            }


        }

    }
}
