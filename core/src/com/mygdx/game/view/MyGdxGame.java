package com.mygdx.game.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.AccelerometerInputInterface;
import com.mygdx.game.Controller.InputController;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.model.Ball;
import com.mygdx.game.model.GameState;
import com.mygdx.game.model.MenuState;
import com.mygdx.game.model.Paddle;
import com.mygdx.game.model.State;
import com.mygdx.game.model.StateManager;


import java.awt.Image;
import java.util.Random;
import java.util.concurrent.TimeoutException;


import javax.xml.soap.Text;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
		private SpriteBatch batch;
		private ShapeRenderer shapeRenderer;

		private StateManager stateManager;
		private GameState state;
		private MenuState menuState;

		private OrthographicCamera camera;

		private BlinkingStars blinkingStars;
		private AccelerometerInputInterface accelerometerInput;

		private InputController input;

		private PeerHelperInterface peerHelper;
		private WifiDirectInterface wifiDirect;

		//private Texture planet1;
		//private Texture planet2;

		public MyGdxGame(AccelerometerInputInterface accelerometerInput, PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect) {
			this.accelerometerInput = accelerometerInput;
			this.peerHelper = peerHelper;
			this.wifiDirect = wifiDirect;
		}


		@Override
		public void create() {
				batch = new SpriteBatch();
				shapeRenderer = new ShapeRenderer();

				camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

				stateManager = new StateManager();

				state = new GameState(stateManager,Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), peerHelper, wifiDirect);
				stateManager.push(state);

				menuState = new MenuState(stateManager, wifiDirect);
				stateManager.push(menuState);

				input = new InputController(state);

				Gdx.input.setInputProcessor(this);

				blinkingStars = new BlinkingStars(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				blinkingStars.makeBlinkingStars();

				//planet1 = new Texture("planet1.png");
				//planet2 = new Texture("planet2.png");

		}

		@Override
		public void render() {
			Gdx.gl.glClearColor(0.075f, 0.059f, 0.188f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			input.movePaddleToAbsPos((float) accelerometerInput.getNormalizedPosition(this));

			batch.setProjectionMatrix(camera.combined);

			shapeRenderer.setProjectionMatrix(camera.combined);

			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(Color.WHITE);
			blinkingStars.drawBlinkingStars(shapeRenderer);
			shapeRenderer.end();

			batch.setProjectionMatrix(camera.combined);
			stateManager.render(batch, shapeRenderer);
			stateManager.update();

			batch.begin();
			//batch.draw(planet1, Gdx.graphics.getWidth() - planet1.getWidth(), Gdx.graphics.getHeight() - planet1.getHeight());
			//batch.draw(planet2, 0, 0);
			batch.end();


		}



		@Override
		public boolean keyDown(int keycode) {
				return false;
		}

		@Override
		public boolean keyUp(int keycode) {
				return false;
		}

		@Override
		public boolean keyTyped(char character) {
				return false;
		}


		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				//peerHelper.discover();
				return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
				//	input.movePaddleToAbsPos(screenX);

				//	System.out.println("screenX = " + screenX + " screenY = " + screenY);
				//	state.getPaddles()[0].setX((float) screenX);
				//state.getPaddles()[1].setX((float) screenX);


				return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
				return false;
		}

		@Override
		public boolean scrolled(int amount) {
				return false;
		}

		public InputController getInput() {
				return input;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				return false;
		}


		public double getWidth() {
				return Gdx.graphics.getWidth();
		}

		public double getHeight() {
				return Gdx.graphics.getHeight();
		}
}
