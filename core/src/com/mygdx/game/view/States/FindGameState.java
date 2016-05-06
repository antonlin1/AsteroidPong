package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.WifiDirectInterface;

/**
 * Created by johanmansson on 16-05-06.
 */
public class FindGameState extends State {

    private Texture cancel;
    private Texture[] circles;
    private Texture text;
    private Texture button;
    private long time;


    public FindGameState(StateManager stateManager, WifiDirectInterface wifiDirect) {
       super(stateManager, StateManager.STATE_NAME.FIND_GAME_STATE, wifiDirect);
       cancel = new Texture("cancel2.png");

        circles = new Texture[5];
        circles[0] = new Texture("s1.png");
        circles[1] = new Texture("s2.png");
        circles[2] = new Texture("s3.png");
        circles[3] = new Texture("s4.png");
        circles[4] = new Texture("s5.png");

        text = new Texture("selecttext.png");
        button = new Texture("buttonREF.png");

        time = TimeUtils.millis();


    }

    @Override
    public void update() {
        handleInput();

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {

        spriteBatch.begin();
        spriteBatch.draw(cancel, Gdx.graphics.getWidth() - cancel.getWidth() - 20, 20);

        spriteBatch.draw(text, (Gdx.graphics.getWidth() / 2) - (text.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - text.getHeight() - 500);

        if(TimeUtils.timeSinceMillis(time) > 0 && TimeUtils.timeSinceMillis(time) < 150) {
            spriteBatch.draw(circles[0], (Gdx.graphics.getWidth() / 2) - (circles[0].getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (circles[0].getHeight() / 2));
        }
        if(TimeUtils.timeSinceMillis(time) > 150 && TimeUtils.timeSinceMillis(time) < 300) {
            spriteBatch.draw(circles[1], (Gdx.graphics.getWidth()/2) - (circles[0].getWidth()/2), (Gdx.graphics.getHeight()/2) - (circles[0].getHeight()/2));
        }
        if(TimeUtils.timeSinceMillis(time) > 300 && TimeUtils.timeSinceMillis(time) < 450) {
            spriteBatch.draw(circles[2], (Gdx.graphics.getWidth()/2) - (circles[0].getWidth()/2), (Gdx.graphics.getHeight()/2) - (circles[0].getHeight()/2));
        }
        if(TimeUtils.timeSinceMillis(time) > 450 && TimeUtils.timeSinceMillis(time) < 600) {
            spriteBatch.draw(circles[3], (Gdx.graphics.getWidth()/2) - (circles[0].getWidth()/2), (Gdx.graphics.getHeight()/2) - (circles[0].getHeight()/2));
        }
        if(TimeUtils.timeSinceMillis(time) > 600 && TimeUtils.timeSinceMillis(time) < 750) {
            spriteBatch.draw(circles[4], (Gdx.graphics.getWidth()/2) - (circles[0].getWidth()/2), (Gdx.graphics.getHeight()/2) - (circles[0].getHeight()/2));
        }
        if(TimeUtils.timeSinceMillis(time) > 750) {
            time = TimeUtils.millis();
        }

        Color c = spriteBatch.getColor();

        spriteBatch.setColor(c.r, c.g, c.b, 0.3f);
        spriteBatch.draw(button, (Gdx.graphics.getWidth() / 2) - (button.getWidth() / 2), Gdx.graphics.getHeight() - button.getHeight() - 100);
        spriteBatch.setColor(c.r, c.g, c.b, 1f);


        spriteBatch.end();

    }



    public void handleInput() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();

            float x1 = Gdx.graphics.getWidth() - cancel.getWidth() - 20;
            float x2 = Gdx.graphics.getWidth() + cancel.getWidth() - 20;
            float y1 = 20;
            float y2 = cancel.getHeight() + 20;

            if (x > x1 && x < x2 && y > y1 && y < y2) {
                System.out.println("Exit pressed");
                stateManager.pop();

            }
        }
    }

}
