package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.model.Ball;
import com.mygdx.game.model.PhysicsHelper;
import com.mygdx.game.view.MyGdxGame;

import static com.mygdx.game.view.States.GameState.PaddleConstant.YPOS;

/**
 * Created by hampusballdin on 2016-05-08.
 */
public class MultiplayerServer extends Multiplayer {

	protected Texture planetDown, planetUp;
	protected Texture[] scores;
	protected int scoreDown, scoreUp;
	protected boolean isDeadUp, isDeadDown;

	public MultiplayerServer(MyGdxGame game, StateManager stateManager, float width, float height, PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect) {

		super(game, stateManager, width, height, peerHelper, wifiDirect, StateManager.STATE_NAME.MULTIPLAYER_SERVER_STATE);
		planetDown = new Texture("EarthDown.png");
		planetUp = new Texture("MoonUp.png");

		scores = new Texture[6];
		scores[0] = new Texture("score0.png");
		scores[1] = new Texture("score20.png");
		scores[2] = new Texture("score40.png");
		scores[3] = new Texture("score60.png");
		scores[4] = new Texture("score80.png");
		scores[5] = new Texture("score100.png");

		scoreDown = 5;
		scoreUp = 5;
		isDeadUp = false;
		isDeadDown = false;

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


			isDeadDown = PhysicsHelper.isDeadDown(width, height, balls);
			isDeadUp = PhysicsHelper.isDeadUp(width, height, balls);

			if(isDead && isDeadDown) {

				scoreDown--;

				if(scoreDown == 0) {
					scoreDown = 5;
					stateManager.push(new GameOverState(game, stateManager, wifiDirect, peerHelperInterface));
				}
			}
			if(isDead && isDeadUp) {

				scoreUp--;

				if(scoreUp == 0) {
					scoreUp = 5;
					stateManager.push(new GameOverState(game, stateManager, wifiDirect, peerHelperInterface));
				}
			}



		}


	@Override
	public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
		super.render(spriteBatch, shapeRenderer);

		spriteBatch.begin();

		spriteBatch.draw(planetDown, 0, Gdx.graphics.getHeight() - planetDown.getHeight());
		spriteBatch.draw(planetUp, 0, 0);
		spriteBatch.draw(scores[scoreDown], 20, height - YPOS.value - 175, 170, 75);
		spriteBatch.draw(scores[scoreUp], 20, YPOS.value + 175, 170, 75);

		spriteBatch.end();

	}

}
