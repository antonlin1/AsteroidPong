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
public class SinglePlayer extends GameState {

		protected Texture planetDown, planetUp;
		protected Texture[] scores;
		protected int score;

		protected boolean isDeadDown;

		public SinglePlayer(MyGdxGame game, StateManager stateManager, float width, float height,
							PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect) {
				super(game, stateManager, width, height, peerHelper, wifiDirect,
						StateManager.STATE_NAME.SINGLEPLAYER_STATE);

				planetDown = new Texture("EarthDown.png");
				planetUp = new Texture("MoonUp.png");

				scores = new Texture[6];
				scores[0] = new Texture("score0.png");
				scores[1] = new Texture("score20.png");
				scores[2] = new Texture("score40.png");
				scores[3] = new Texture("score60.png");
				scores[4] = new Texture("score80.png");
				scores[5] = new Texture("score100.png");

				score = 5;
				isDeadDown = false;

		}

		@Override
		public void update() {
				super.update();
				Ball ball = balls.get(0);
				wallCollision = PhysicsHelper.wallCollision(width, height, balls);
				paddleCollision = PhysicsHelper.paddleCollision(getPaddles(), balls);
				isDead = PhysicsHelper.isDead(width, height, balls);
				ball.move();
				handleTouchInput();
				isDeadDown = PhysicsHelper.isDeadDown(width, height, balls);

				if(isDead && isDeadDown) {

					score--;

					if(score == 0) {
						score = 5;
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
		spriteBatch.draw(scores[score], 20, height - YPOS.value - 175, 170, 75);

		spriteBatch.end();


	}
}
