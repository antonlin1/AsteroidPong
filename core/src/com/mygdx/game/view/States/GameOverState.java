package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.view.MyGdxGame;

/**
 * Created by johanmansson on 16-05-09.
 */
public class GameOverState extends State {

    private Texture text[];
    private Texture button1;
    private Texture button2;

    private boolean youWon;
    private int textNbr;

    public GameOverState(MyGdxGame game, StateManager stateManager,
						 WifiDirectInterface wifiDirect, PeerHelperInterface peerHelperInterface, boolean youWon) {
        super(game, stateManager, StateManager.STATE_NAME.HOW_TO_PLAY_STATE, wifiDirect, peerHelperInterface);

        text = new Texture[2];
        text[0] = new Texture("gameover.png");
        text[1] = new Texture("youWon.png");

        button1 = new Texture("button3.png");
        button2 = new Texture("buttonEX.png");
        this.youWon = youWon;

        if(youWon == false) {
            textNbr = 0;
        }
        if(youWon == true) {
            textNbr = 1;
        }


    }

    @Override
    public void update() {

        handleTouchInput();


    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        spriteBatch.begin();

        spriteBatch.draw(text[textNbr], (Gdx.graphics.getWidth()/2) - (text[textNbr].getWidth()/2), (Gdx.graphics.getHeight()/2) - text[textNbr].getHeight() - 100);
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
