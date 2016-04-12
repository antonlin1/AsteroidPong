package com.mygdx.game.Controller;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.model.GameState;

/**
 * Created by antonlin on 16-04-12.
 */
public class InputController {

    GameState state;

    public InputController(GameState state) {
        this.state = state;
    }

    public void movePaddleToAbsPos(int x) {

        int validX = (x > Gdx.graphics.getWidth() - GameState.PaddleConstant.LENGTH.value) ?
                Gdx.graphics.getWidth() - GameState.PaddleConstant.LENGTH.value : x;

        state.getPaddles()[0].setX(validX);
        state.getPaddles()[1].setX(validX);
    }

    public void movePaddleWithDelta(int x) {

    }

}
