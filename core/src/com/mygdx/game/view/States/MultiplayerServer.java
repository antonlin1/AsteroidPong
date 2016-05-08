package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.model.Ball;
import com.mygdx.game.model.PhysicsHelper;
import com.mygdx.game.view.MyGdxGame;

/**
 * Created by hampusballdin on 2016-05-08.
 */
public class MultiplayerServer extends Multiplayer {
		public MultiplayerServer(MyGdxGame game, StateManager stateManager, float width, float height,
								 PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect) {
				super(game, stateManager, width, height, peerHelper, wifiDirect, StateManager.STATE_NAME.MULTIPLAYER_SERVER_STATE);
		}

		@Override
		public void update() {
				super.update();
				Ball ball = balls.get(0);
				wallCollision = PhysicsHelper.wallCollision(width, height, balls);
				paddleCollision = PhysicsHelper.paddleCollision(getPaddles(), balls);
				isDead = PhysicsHelper.isDead(width, height, balls);
				setPaddle2X(wifiDirect.getNetworkComponent().getOpponentPaddleX() *
						Gdx.graphics.getWidth(), (float) Gdx.graphics.getWidth());

				setPaddle2Y(wifiDirect.getNetworkComponent().getOpponentPaddleY() *
						Gdx.graphics.getHeight(), (float) Gdx.graphics.getHeight());

				ball.move();
				handleTouchInput();

				double w = Gdx.graphics.getWidth();
				double h = Gdx.graphics.getHeight();

				wifiDirect.getNetworkComponent().setServerToClientData(this.isActive(),
						paddleCollision, wallCollision, (float) (paddle1.getX() / w),
						(float) (paddle1.getY() / h), (float) (ball.getX() / w),
						(float) (ball.getY() / h), ball.getXVel(), ball.getYVel(), ball.getVelocity(), w, h);
		}
}
