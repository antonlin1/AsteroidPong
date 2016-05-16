package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.view.MyGdxGame;

/**
 * Created by Viktor on 2016-05-16.
 */
public class GamePausedState extends State {

    private Texture text;
    private Texture button1;
    private Texture button2;

    public GamePausedState(MyGdxGame game, StateManager stateManager, WifiDirectInterface wifiDirect, PeerHelperInterface peerHelperInterface) {
        super(game, stateManager, StateManager.STATE_NAME.PAUSE_STATE, wifiDirect, peerHelperInterface);


        text = new Texture("gamepaused.png");
        button1 = new Texture("buttonRE.png");
        button2 = new Texture("buttonEX.png");


    }

    @Override
    public void update() {
        handleTouchInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        spriteBatch.begin();

        spriteBatch.draw(text, (Gdx.graphics.getWidth()/2) - (text.getWidth()/2), (Gdx.graphics.getHeight()/2) - text.getHeight() - 100);
        spriteBatch.draw(button1, (Gdx.graphics.getWidth() / 2) - (button1.getWidth() / 2), (Gdx.graphics.getHeight() / 2) + 50);
        spriteBatch.draw(button2, (Gdx.graphics.getWidth() / 2) - (button2.getWidth() / 2), (Gdx.graphics.getHeight() / 2) + 70 + (button1.getHeight()));
        spriteBatch.end();

    }

    @Override
    public boolean isActive() {
        return false;
    }

    public void handleTouchInput() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();

            float x1 = (Gdx.graphics.getWidth() / 2) - (button1.getWidth() / 2);
            float x2 = (Gdx.graphics.getWidth() / 2) + (button1.getWidth() / 2);

            float y11 = (Gdx.graphics.getHeight() / 2) + 50;
            float y12 = (Gdx.graphics.getHeight() / 2) + 50 + button1.getHeight();

            float y21 = (Gdx.graphics.getHeight() / 2) + 70 + (button1.getHeight());
            float y22 = (Gdx.graphics.getHeight() / 2) + 70 + 2 * (button1.getHeight());


            if (x > x1 && x < x2 && y > y11 && y < y12) {
                System.out.println("Button1 pressed");
                stateManager.pop();
            }

            if (x > x1 && x < x2 && y > y21 && y < y22) {
                System.out.println("Button2 pressed");
                stateManager.push(new MenuState(game, stateManager, wifiDirect, peerHelperInterface));
            }
        }
    }




}
