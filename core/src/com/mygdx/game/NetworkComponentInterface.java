package com.mygdx.game;

import com.mygdx.game.model.ClientToServerMessage;
import com.mygdx.game.model.ServerToClientMessage;

/**
 * Created by antonlin on 16-04-26.
 */
public interface NetworkComponentInterface {

    public void setClientToServerData(float paddleX);

    public void setServerToClientData(boolean gameActive, boolean paddleCollision, boolean wallCollision, float paddleX, float ballX, float ballY,
                                      float ballXVelocity, float ballYVelocity, float ballVelocity);

    // Get data received from other peer to update gamestate
    public String getData();

    public ClientToServerMessage getClientData();

    public ServerToClientMessage getServerData();

    public float getOpponentPaddleX();

    public boolean isConnectionOpen();
}
