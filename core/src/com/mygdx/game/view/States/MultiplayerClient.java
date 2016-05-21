package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.model.Ball;
import com.mygdx.game.model.PhysicsHelper;
import com.mygdx.game.model.ServerToClientMessage;
import com.mygdx.game.view.MyGdxGame;

import static com.mygdx.game.view.States.GameState.PaddleConstant.YPOS;

/**
 * Created by hampusballdin on 2016-05-08.
 */
public class MultiplayerClient extends Multiplayer {
	private long lastNanoTimeUpdate = 0l;
	protected  Texture[] planetDown, planetUp;
	protected int scoreDown, scoreUp;
	protected boolean isDeadUp, isDeadDown;

	public MultiplayerClient(MyGdxGame game, StateManager stateManager, float width, float height, PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect) {

		super(game, stateManager, width, height, peerHelper, wifiDirect,
				StateManager.STATE_NAME.MULTIPLAYER_CLIENT_STATE);

		planetDown = new Texture[4];
		planetDown[0] = new Texture("MoonLifeDown25.png");
		planetDown[1] = new Texture("MoonLifeDown50.png");
		planetDown[2] = new Texture("MoonLifeDown75.png");
		planetDown[3] = new Texture("MoonLifeDown100.png");


		planetUp = new Texture[4];
		planetUp[0] = new Texture("EarthLifeUp25.png");
		planetUp[1] = new Texture("EarthLifeUp50.png");
		planetUp[2] = new Texture("EarthLifeUp75.png");
		planetUp[3] = new Texture("EarthLifeUp100.png");

		scoreDown = 4;
		scoreUp = 4;

	}

	@Override
	public void update() {
		super.update();
		Ball ball = balls.get(0);
		handleTouchInput();
		handleSpeechInput();
		double w = Gdx.graphics.getWidth();
		double h = Gdx.graphics.getHeight();
		wifiDirect.getNetworkComponent().setClientToServerData(
				(float) (paddle1.getX() / w), (float) (paddle1.getY() / h));

		if (wifiDirect.getNetworkComponent().isClientUpdated()) {
			System.out.println("CLIENT UUUUUPDATE");
			ServerToClientMessage state = wifiDirect.getNetworkComponent().getServerData();

			long currentNanoTime = state.getNanoTime();

			if (currentNanoTime > lastNanoTimeUpdate) {
				isDead = false;
				ball.setxVelocity(-state.getBallXVelocity());
				ball.setyVelocity(-state.getBallYVelocity());

				ball.setX((float) (w - (state.getBallX()) * w));
				ball.setY((float) (h - (state.getBallY()) * h));

				setPaddle2X((float) (state.getPaddleX() * w), (float) w);
				setPaddle2Y((float) (state.getPaddleY() * h), (float) h);
				lastNanoTimeUpdate = currentNanoTime;
			} else {
				clientRegularUpdate(ball);
			}
		} else {
			clientRegularUpdate(ball);
		}

		isDeadDown = PhysicsHelper.isDeadDown(width, height, balls);
		isDeadUp = PhysicsHelper.isDeadUp(width, height, balls);

		if(isDead && isDeadDown) {

			scoreDown--;

			if(scoreDown == 0) {
				scoreDown = 4;
				scoreUp = 4;
				stateManager.push(new GameOverState(game, stateManager, wifiDirect, peerHelperInterface, false));
			}
		}
		if(isDead && isDeadUp) {

			scoreUp--;

			if(scoreUp == 0) {
				scoreUp = 4;
				scoreDown = 4;
				stateManager.push(new GameOverState(game, stateManager, wifiDirect,peerHelperInterface, true));
			}
		}
	}




	private void clientRegularUpdate(Ball ball) {
		wallCollision = PhysicsHelper.wallCollision(width, height, balls);
		paddleCollision = PhysicsHelper.paddleCollision(getPaddles(), balls);
		isDead = PhysicsHelper.isDead(width, height, balls);
		ball.move();
	}


	@Override
	public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
		super.render(spriteBatch, shapeRenderer);

		spriteBatch.begin();
		spriteBatch.draw(planetDown[scoreDown - 1], 0, Gdx.graphics.getHeight() - planetDown[scoreDown - 1].getHeight());
		spriteBatch.draw(planetUp[scoreUp - 1], 0, 0);
		spriteBatch.end();

	}
}

