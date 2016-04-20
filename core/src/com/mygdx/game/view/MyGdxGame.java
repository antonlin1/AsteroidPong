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
import com.mygdx.game.model.Ball;
import com.mygdx.game.model.GameState;
import com.mygdx.game.model.Paddle;
import com.mygdx.game.model.State;
import com.mygdx.game.model.StateManager;


import java.awt.Image;
import java.util.Random;


import javax.xml.soap.Text;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
		SpriteBatch batch;

		private StateManager stateManager;
		private GameState state;
		private ShapeRenderer shapeRenderer;
		private OrthographicCamera camera;

		private BlinkingStars blinkingStars;
		private AccelerometerInputInterface accelerometerInput;

		private InputController input;

		public MyGdxGame(AccelerometerInputInterface accelerometerInput) {
			this.accelerometerInput = accelerometerInput;
		}


		@Override
		public void create() {
				batch = new SpriteBatch();
				shapeRenderer = new ShapeRenderer();

				camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

				stateManager = new StateManager();
				state = new GameState(stateManager,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				stateManager.push(state);


				input = new InputController(state);

				Gdx.input.setInputProcessor(this);


				blinkingStars = new BlinkingStars(state.getWidth(), state.getHeight());
				blinkingStars.makeBlinkingStars();



		}

		@Override
		public void render() {
			Gdx.gl.glClearColor(0.075f, 0.059f, 0.188f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			input.movePaddleToAbsPos((float) accelerometerInput.getNormalizedPosition(this));

			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			batch.end();

			shapeRenderer.setProjectionMatrix(camera.combined);

			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(Color.WHITE);
			blinkingStars.drawBlinkingStars(shapeRenderer);
			shapeRenderer.end();

			stateManager.render(batch, shapeRenderer);
			stateManager.update();

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
}
