package org.mamn01.pong.controller.num;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.Controller.InputController;
import com.mygdx.game.view.MyGdxGame;

/**
 * Created by hampusballdin on 2016-04-12.
 * Converts Acceleration to position
 */
public class Converter {
		private Sampler sampler;

		private Acceleration acceleration;
		private Velocity velocity;
		private Position position;
		private MyGdxGame game;

		public Converter(MyGdxGame game) {
				sampler = new Sampler();
				this.game = game;

				acceleration = new Acceleration(sampler);
				velocity = new Velocity(acceleration);
				position = new Position(velocity);

				sampler.registerSampleGatheredCallback(acceleration);
				sampler.init();
		}

		public void convert(float[] accelerationData, long time) {
				// Only interested in vertical movement
				double dt = sampler.gatherSample(accelerationData[0], time);
				if (dt == 0.0)
						return;

				InputController controller = game.getInput();

				velocity.onAccelerationUpdate(dt);
				if (controller != null) {

						if (controller.isAtLeftBoundary()) {
								position.onVelocityUpdatePositive(dt);

								if(acceleration.getValue() < 0)
										acceleration.reset();
								if(velocity.getValue() < 0)
										velocity.reset();
						}else if(controller.isAtRightBoundary()) {
								position.onVelocityUpdateNegative(dt);

								if(acceleration.getValue() > 0)
										acceleration.reset();
								if(velocity.getValue() > 0)
										velocity.reset();
						}else {
								position.onVelocityUpdate(dt);
						}

						float WIDTH = Gdx.graphics.getWidth();
						float xPos = (WIDTH / 2 + (float) position.getValue() * 200000 / 4);

						controller.movePaddleToAbsPos(xPos);
				}
		}

		public double getElapsedTime() {
				return sampler.getElapsedTime();
		}

		public double getAcceleration() {
				return acceleration.getOutputHistory(1);
		}

		public double getAccelerationRateOfChange() {
				return acceleration.getRateOfChange();
		}

		public double getVelocity() {
				return velocity.getDisplayValue();
		}

		public double getPosition() {
				return position.getValue();
		}

		/*
		public double[] getAcceleration() {
				return a;
		}

		public double getLastTime() {
				return prevTime;
		}

		public double[] getVelocity() {
				return v;
		}


		*/
}
