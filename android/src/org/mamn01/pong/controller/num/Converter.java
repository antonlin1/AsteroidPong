package org.mamn01.pong.controller.num;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.AccelerometerInputInterface;
import com.mygdx.game.Controller.InputController;
import com.mygdx.game.view.MyGdxGame;

/**
 * Created by hampusballdin on 2016-04-12.
 * Converts Acceleration to position
 */
public class Converter implements AccelerometerInputInterface {
		private Sampler sampler;

		private Acceleration acceleration;
		private Velocity velocity;
		private Position position;
		//private MyGdxGame game;

		public Converter() {
				sampler = new Sampler();
				//this.game = game;

				acceleration = new Acceleration(sampler);
				velocity = new Velocity(acceleration);
				position = new Position(velocity);

				sampler.registerSampleGatheredCallback(acceleration);
				sampler.init();
		}

		public void convert(float[] accelerationData, long time, MyGdxGame game) {
				// Only interested in vertical movement
				double dt = sampler.gatherSample(accelerationData[0], time);
				if (dt == 0.0)
						return;

				InputController controller = game.getInput();

				velocity.onAccelerationUpdate(dt);
				if (controller != null) {

						if (controller.isAtLeftBoundary()) {
								if(acceleration.getValue() < 0)
										acceleration.resetAcceleration();
								if(velocity.getValue() < 0)
										velocity.reset();
						}else if(controller.isAtRightBoundary()) {
								if(acceleration.getValue() > 0)
										acceleration.resetAcceleration();
								if(velocity.getValue() > 0)
										velocity.reset();


						}
						position.onVelocityUpdate(dt);
						//controller.movePaddleToAbsPos(xPos);
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

	@Override
	public double getRawPosition() {
		return 0;
	}

	@Override
	public double getNormalizedPosition(MyGdxGame game) {

		float WIDTH = Gdx.graphics.getWidth();

		float xPos = (WIDTH / 2 + (float) position.getValue() * 200000 / 20);

		return xPos;
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
