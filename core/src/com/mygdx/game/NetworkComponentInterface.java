package com.mygdx.game;

import com.mygdx.game.model.ClientToServerMessage;
import com.mygdx.game.model.ServerToClientMessage;
import com.mygdx.game.view.States.GameState;

/**
 * Created by antonlin on 16-04-26.
 */
public interface NetworkComponentInterface {

		public void setClientToServerData(boolean gameActive, boolean gamePaused, float paddleX, float paddleY);

		public void setServerToClientData(boolean gameActive , boolean gamePaused, boolean paddleCollision, boolean wallCollision, float paddleX, float paddleY, float ballX, float ballY,
										  float ballXVelocity, float ballYVelocity, float ballVelocity, double screenWidth, double screenHeight, int hpUp, int hpDown, GameState.GameOverEvent gameOverEvent);

		// Get data received from other peer to update gamestate
		public String getData();

		public ClientToServerMessage getClientData();

		public ServerToClientMessage getServerData();

		public float getOpponentPaddleX();

		public float getOpponentPaddleY();

		public boolean isConnectionOpen();

		public boolean isClientUpdated();

		public boolean isServerUpdated();

}
