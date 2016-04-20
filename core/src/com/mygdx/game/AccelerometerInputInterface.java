package com.mygdx.game;

import com.mygdx.game.view.MyGdxGame;

/**
 * Created by antonlin on 16-04-20.
 */
public interface AccelerometerInputInterface {

    public double getRawPosition();

    public double getNormalizedPosition(MyGdxGame game);

}
