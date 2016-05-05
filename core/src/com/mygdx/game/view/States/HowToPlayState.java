package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.WifiDirectInterface;

/**
 * Created by johanmansson on 16-04-26.
 */
public class HowToPlayState extends com.mygdx.game.view.States.State {

    private Texture cancel;
    private Texture text;
    private Texture text2;
    private Texture[] movingphone;
    private Texture button;

    private long time;
    private boolean connected;



    public HowToPlayState(StateManager stateManager, WifiDirectInterface wifiDirect) {
        super(stateManager, StateManager.STATE_NAME.HOW_TO_PLAY_STATE, wifiDirect);
        cancel = new Texture("cancel2.png");
        movingphone = new Texture[3];
        movingphone[0] = new Texture("phone1.png");
        movingphone[1] = new Texture("phone2.png");
        movingphone[2] = new Texture("phone3.png");

        time = TimeUtils.millis();

        text = new Texture("howtoplaytext.png");
        text2 = new Texture("waitingtext2.png");
        button = new Texture("buttonSG.png");

        connected = false;

    }

    public void changeConnected() {
        connected = true;
    }


    @Override
    public void update() {
        handleInput();

    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {

        spriteBatch.begin();
        Color c = spriteBatch.getColor();
        spriteBatch.draw(cancel, Gdx.graphics.getWidth() - cancel.getWidth() - 20, 20);

        if(!connected) {
            spriteBatch.draw(text2, (Gdx.graphics.getWidth() / 2) - (text2.getWidth() / 2), 20);
            spriteBatch.setColor(c.r, c.g, c.b, 0.3f);
        }
        spriteBatch.draw(button, (Gdx.graphics.getWidth() / 2) - (button.getWidth() / 2), Gdx.graphics.getHeight() - button.getHeight() - 100);
        spriteBatch.setColor(c.r, c.g, c.b, 1f);

        float x1 = (Gdx.graphics.getWidth() / 2) - 250;
        float x2 = (Gdx.graphics.getWidth() / 2) - (text.getWidth()/2);

        spriteBatch.draw(text, x2, 750);


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
