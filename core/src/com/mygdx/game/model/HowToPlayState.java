package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.WifiDirectInterface;

/**
 * Created by johanmansson on 16-04-26.
 */
public class HowToPlayState extends State {

    private Texture cancel;
    private Texture text;
    private Texture[] movingphone;
    private long time;



    public HowToPlayState(StateManager stateManager, WifiDirectInterface wifiDirect) {
        super(stateManager, StateManager.STATE_NAME.HOW_TO_PLAY_STATE, wifiDirect);
        cancel = new Texture("cancel2.png");
        movingphone = new Texture[3];
        movingphone[0] = new Texture("phone1.png");
        movingphone[1] = new Texture("phone2.png");
        movingphone[2] = new Texture("phone3.png");

        time = TimeUtils.millis();

        text = new Texture("howtoplaytext.png");




    }


    @Override
    public void update() {
        handleInput();

    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {

        spriteBatch.begin();
        spriteBatch.draw(cancel, Gdx.graphics.getWidth() - cancel.getWidth() - 20, 20);
        float x1 = (Gdx.graphics.getWidth() / 2) - 250;
        float x2 = (Gdx.graphics.getWidth() / 2) - (text.getWidth()/2);

        spriteBatch.draw(text, x2, 850);

        if(TimeUtils.timeSinceMillis(time) > 0) {
            spriteBatch.draw(movingphone[0], x1, 300, 500, 500);
        }
        if(TimeUtils.timeSinceMillis(time) > 600) {
            spriteBatch.draw(movingphone[1], x1, 300, 500, 500);
        }
        if(TimeUtils.timeSinceMillis(time) > 900) {
            spriteBatch.draw(movingphone[0], x1, 300, 500, 500);
        }
        if(TimeUtils.timeSinceMillis(time) > 1600) {
            spriteBatch.draw(movingphone[2], x1, 300, 500, 500);
        }

        if(TimeUtils.timeSinceMillis(time) > 1900) {
            time = TimeUtils.millis();
        }


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

    @Override
    public boolean isActive() {
        return (this.stateManager.getActiveState().equals(this.stateName));    }
}
