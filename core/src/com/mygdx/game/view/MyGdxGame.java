package com.mygdx.game.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.AccelerometerInputInterface;
import com.mygdx.game.Controller.InputController;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.SpeechHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.view.States.GameState;
import com.mygdx.game.view.States.MenuState;
import com.mygdx.game.view.States.MultiplayerClient;
import com.mygdx.game.view.States.MultiplayerServer;
import com.mygdx.game.view.States.SinglePlayer;
import com.mygdx.game.view.States.StateManager;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	private StateManager stateManager;

	private OrthographicCamera camera;

	private BlinkingStars blinkingStars;

	private AccelerometerInputInterface accelerometerInput;
	private InputController input;

	private PeerHelperInterface peerHelper;

	private WifiDirectInterface wifiDirect;
	private SpeechHelperInterface speechHelper;
	private InputMultiplexer inputMultiplexer;

	//private Texture planet1;

	public MyGdxGame(AccelerometerInputInterface accelerometerInput,
					 PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect, SpeechHelperInterface speechHelper) {
		this.accelerometerInput = accelerometerInput;
		this.peerHelper = peerHelper;
		this.wifiDirect = wifiDirect;
		this.speechHelper = speechHelper;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		stateManager = new StateManager(this, wifiDirect, peerHelper);

		GameState serverMultiplayer = new MultiplayerServer(this, stateManager,
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), peerHelper, wifiDirect);
		stateManager.add(serverMultiplayer.getStateName(), serverMultiplayer);

		GameState clientMultiplayer = new MultiplayerClient(this, stateManager,
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), peerHelper, wifiDirect);
		stateManager.add(clientMultiplayer.getStateName(), clientMultiplayer);

		GameState singlePlayer = new SinglePlayer(this, stateManager,
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), peerHelper, wifiDirect);
		stateManager.add(singlePlayer.getStateName(), singlePlayer);

		MenuState menuState = new MenuState(this, stateManager, wifiDirect, peerHelper);
		stateManager.push(menuState);

		blinkingStars = new BlinkingStars(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		blinkingStars.makeBlinkingStars();

		input = new InputController();

		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this);

		Gdx.input.setInputProcessor(this);

		//planet1 = new Texture("planet1.png");
		//planet2 = new Texture("planet2.png");
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.075f, 0.059f, 0.188f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// WHY ?!?!?!
		//input.movePaddleToAbsPos((float) accelerometerInput.getNormalizedPosition(this));

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


	public InputMultiplexer getInputMultiplexer() {
		return inputMultiplexer;
	}

	public SpeechHelperInterface getSpeechHelper() {
		return speechHelper;
	}

	public StateManager getStateManager() {
		return stateManager;
	}
}
