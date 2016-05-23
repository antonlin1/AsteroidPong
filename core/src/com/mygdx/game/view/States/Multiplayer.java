package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.view.MyGdxGame;

import java.util.Iterator;

/**
 * Created by hampusballdin on 2016-05-08.
 */
public abstract class Multiplayer extends GameState {

		protected boolean countDownDone = false;
		private float deltaTime = 0;
		private float prevCount = -1;
		private BitmapFont countDownFont;
		private TextButton countDownButton = null;
		private int countDownLength = 4;
		protected Stage stage;

		public Multiplayer(MyGdxGame game, StateManager stateManager, float width, float height,
						   PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect,
						   StateManager.STATE_NAME state) {
				super(game, stateManager, width, height, peerHelper, wifiDirect, state);

				FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("helvetica_bold.ttf"));
				FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
				parameter.size = 200;
				countDownFont = generator.generateFont(parameter); // font size 12 pixels
				generator.dispose();

				stage = new Stage(new ScalingViewport(Scaling.stretch, (float) game.getWidth(), (float) game.getHeight()));
		}

		public void countDown() {
				if (!countDownDone) {
						deltaTime += Gdx.graphics.getDeltaTime();
						System.out.println("DELTAAA: " + deltaTime);

						if (deltaTime < countDownLength) {
								if (((int) deltaTime) > prevCount) {
										drawCountDown();
								}
						} else {
								stage.clear();
								countDownDone = true;
						}
						prevCount = (int) deltaTime;
				} else {
						stage.clear();
				}
		}

		private void drawCountDown() {
				if (countDownButton != null) {
						stage.clear();
				}

				TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
				textButtonStyle.font = countDownFont;
				countDownButton = new TextButton("" + (countDownLength - 1 - ((int) deltaTime)), textButtonStyle);
				countDownButton.setX((float) ((game.getWidth() - countDownButton.getWidth()) / 2));
				countDownButton.setY((float) (game.getHeight() / 2));
				stage.addActor(countDownButton);
		}

		protected void resetCountDown() {
				countDownDone = false;
				deltaTime = 0;
				prevCount = -1;
		}

		protected void drawWaitingForOtherPlayer() {

				FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("coves_light.otf"));
				FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
				parameter.size = 100;

				TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
				textButtonStyle.font = generator.generateFont(parameter); // font size 12 pixels
				generator.dispose();

				TextButton waitingButton = new TextButton("Waiting for other player", textButtonStyle);

				waitingButton.setX((float) ((game.getWidth() - waitingButton.getWidth()) / 2));
				waitingButton.setY((float) (game.getHeight() / 2));

				stage.addActor(waitingButton);
		}

		public abstract boolean isOtherActive();
		public boolean isPaused() {
				return !isOtherActive() || !isActive();
		}
}
