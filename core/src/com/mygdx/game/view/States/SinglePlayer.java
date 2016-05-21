package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.model.Ball;
import com.mygdx.game.model.PhysicsHelper;
import com.mygdx.game.model.Player;
import com.mygdx.game.view.MyGdxGame;

import static com.mygdx.game.view.States.GameState.PaddleConstant.YPOS;

/**
 * Created by hampusballdin on 2016-05-08.
 */
public class SinglePlayer extends GameState {


		protected  Texture[] planetDown, planetUp;
//		protected int scoreUp, scoreDown;

		protected boolean isDeadDown, isDeadUp;

		public SinglePlayer(MyGdxGame game, StateManager stateManager, float width, float height,
							PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect) {
				super(game, stateManager, width, height, peerHelper, wifiDirect,
						StateManager.STATE_NAME.SINGLEPLAYER_STATE);


				planetDown = new Texture[4];
				planetDown[0] = new Texture("EarthLifeDown25.png");
				planetDown[1] = new Texture("EarthLifeDown50.png");
				planetDown[2] = new Texture("EarthLifeDown75.png");
				planetDown[3] = new Texture("EarthLifeDown100.png");


				planetUp = new Texture[4];
				planetUp[0] = new Texture("MoonLifeUp25.png");
				planetUp[1] = new Texture("MoonLifeUp50.png");
				planetUp[2] = new Texture("MoonLifeUp75.png");
				planetUp[3] = new Texture("MoonLifeUp100.png");
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

			handlePlanetHit(isDead, isDeadUp, isDeadDown);
		}

	@Override
	public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
		super.render(spriteBatch, shapeRenderer);

		spriteBatch.begin();
		spriteBatch.draw(planetDown[playerDown.getHp() - 1], 0, Gdx.graphics.getHeight() - planetDown[playerDown.getHp() - 1].getHeight());
		spriteBatch.draw(planetUp[playerUp.getHp() - 1], 0, 0);
		spriteBatch.end();

	}
}
