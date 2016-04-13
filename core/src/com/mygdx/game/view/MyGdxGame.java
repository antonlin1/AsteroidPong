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
import com.mygdx.game.Controller.InputController;
import com.mygdx.game.model.Ball;
import com.mygdx.game.model.GameState;
import com.mygdx.game.model.Paddle;

import java.awt.Image;
import java.util.Random;

import javax.xml.soap.Text;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Sound collisionSound;

	private GameState state;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private Particles particles;
	private BlinkingStars blinkingStars;

	private InputController input;

	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		state = new GameState(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		input = new InputController(state);

		Gdx.input.setInputProcessor(this);

		particles = new Particles();
		blinkingStars = new BlinkingStars(state.getWidth(), state.getHeight());
		blinkingStars.makeBlinkingStars();

		collisionSound = Gdx.audio.newSound(Gdx.files.internal("bounce1.wav"));

	}
	@Override
	public void render() {
		Gdx.gl.glClearColor(0.075f, 0.059f, 0.188f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Ball ball = state.getBalls().get(0);
		particles.makeParticles(ball);
		particles.removeParticles();


		Paddle paddle1 = state.getPaddles()[0];
		Paddle paddle2 = state.getPaddles()[1];

		if(state.isPaddleCollision() || state.isWallCollision()) {
			collisionSound.play();
		}

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.end();

		shapeRenderer.setProjectionMatrix(camera.combined);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);


		blinkingStars.drawBlinkingStars(shapeRenderer);

		particles.drawParticles(shapeRenderer);

		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.circle(ball.getX(), ball.getY(), ball.getRadius());
		shapeRenderer.rect(paddle1.getX(), paddle1.getY(), paddle1.getLength(), paddle1.getHeight());
	//	shapeRenderer.rect(paddle2.getX(), paddle2.getY(), paddle2.getLength(), paddle2.getHeight());

		shapeRenderer.end();
		state.update();
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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return true;
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
}
