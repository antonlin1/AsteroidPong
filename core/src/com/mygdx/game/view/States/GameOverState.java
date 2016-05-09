package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.view.MyGdxGame;

/**
 * Created by johanmansson on 16-05-09.
 */
public class GameOverState extends State {

    private Texture cancel;
    private Texture text;

    public GameOverState(MyGdxGame game, StateManager stateManager, WifiDirectInterface wifiDirect) {
        super(game, stateManager, StateManager.STATE_NAME.HOW_TO_PLAY_STATE, wifiDirect);

        cancel = new Texture("cancel2.png");
        text = new Texture("gameover.png");

    }

    @Override
    public void update() {
        handleTouchInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        spriteBatch.begin();
        spriteBatch.draw(cancel, Gdx.graphics.getWidth() - cancel.getWidth() - 20, 20);
        spriteBatch.draw(text, (Gdx.graphics.getWidth()/2) - (text.getWidth()/2), (Gdx.graphics.getHeight()/2) -600);
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
