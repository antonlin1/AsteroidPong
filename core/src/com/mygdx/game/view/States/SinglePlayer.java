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


		protected  Texture[] planetDown, planetUp;
		protected Texture[] scores;
		protected int scoreUp, scoreDown;

		protected boolean isDeadDown, isDeadUp;

		public SinglePlayer(MyGdxGame game, StateManager stateManager, float width, float height,
							PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect) {
				super(game, stateManager, width, height, peerHelper, wifiDirect,
						StateManager.STATE_NAME.SINGLEPLAYER_STATE);


				planetDown = new Texture[5];
				planetDown[0] = new Texture("EarthLife20.png");
				planetDown[1] = new Texture("EarthLife40.png");
				planetDown[2] = new Texture("EarthLife60.png");
				planetDown[3] = new Texture("EarthLife80.png");
				planetDown[4] = new Texture("EarthLife100.png");

				planetUp = new Texture[5];
				planetUp[0] = new Texture("MoonLife20.png");
				planetUp[1] = new Texture("MoonLife40.png");
				planetUp[2] = new Texture("MoonLife60.png");
				planetUp[3] = new Texture("MoonLife80.png");
				planetUp[4] = new Texture("MoonLife100.png");

				scores = new Texture[6];
				scores[0] = new Texture("score0.png");
				scores[1] = new Texture("score20.png");
				scores[2] = new Texture("score40.png");
				scores[3] = new Texture("score60.png");
				scores[4] = new Texture("score80.png");
				scores[5] = new Texture("score100.png");

				scoreDown = 5;
				scoreUp = 5;
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
				handleSpeechInput();

			isDeadDown = PhysicsHelper.isDeadDown(width, height, balls);
			isDeadUp = PhysicsHelper.isDeadUp(width, height, balls);

			if(isDead && isDeadDown) {

				scoreDown--;

				if(scoreDown == 0) {
					Gdx.input.vibrate(1000);
					scoreDown = 5;
					scoreUp = 5;
					stateManager.push(new GameOverState(game, stateManager, wifiDirect, peerHelperInterface, false));
				}
			}
			if(isDead && isDeadUp) {

				scoreUp--;

				if(scoreUp == 0) {
					scoreUp = 5;
					scoreDown = 5;
					stateManager.push(new GameOverState(game, stateManager, wifiDirect, peerHelperInterface, true));
				}
			}
		}

	@Override
	public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
		super.render(spriteBatch, shapeRenderer);

		spriteBatch.begin();
		spriteBatch.draw(planetDown[scoreDown - 1], 0, Gdx.graphics.getHeight() - planetDown[scoreDown - 1].getHeight());
		spriteBatch.draw(planetUp[scoreUp - 1], 0, 0);
		//spriteBatch.draw(scores[scoreDown], 20, height - YPOS.value - 175, 170, 75);
		//spriteBatch.draw(scores[scoreUp], 20, YPOS.value + 175, 170, 75);

		spriteBatch.end();


	}
}
