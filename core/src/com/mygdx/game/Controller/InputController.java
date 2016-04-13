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

    public void movePaddleToAbsPos(float x) {

        float validX = (x > Gdx.graphics.getWidth() - GameState.PaddleConstant.LENGTH.value) ?
                Gdx.graphics.getWidth() - GameState.PaddleConstant.LENGTH.value : x;

        validX = (validX < 0) ? 0 : validX;

        state.getPaddles()[0].setX(validX);
        state.getPaddles()[1].setX(validX);
    }

    public void movePaddleWithDelta(int x) {

    }

}
