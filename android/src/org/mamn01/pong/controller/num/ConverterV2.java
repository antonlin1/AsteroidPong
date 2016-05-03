package org.mamn01.pong.controller.num;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.AccelerometerInputInterface;
import com.mygdx.game.Controller.InputController;
import com.mygdx.game.view.MyGdxGame;

/**
 * Created by hampusballdin on 2016-04-12.
 * Converts AccelerationV2 to position
 */
public class ConverterV2 implements AccelerometerInputInterface {
		private SamplerV2 sampler;

		private AccelerationV2 acceleration;
		private VelocityV2 velocity;
		private PositionV2 position;

		private int N = 16;

		public ConverterV2() {
				sampler = new SamplerV2(N);

				acceleration = new AccelerationV2(sampler);
				velocity = new VelocityV2(acceleration);
				position = new PositionV2(velocity);

				sampler.registerSampleGatheredCallback(acceleration);
				sampler.init();


//				System.out.println(Arrays.toString(fftLowpass.getGaussianValues()));
		}

		public void convert(float[] accelerationData, long time, MyGdxGame game) {
				// Only interested in vertical movement
				double dt = sampler.gatherSample(accelerationData[0], time);
				if(dt == 0.0)
						return;

				velocity.onAccelerationUpdate(dt);
				//position.onVelocityUpdate(dt);

				InputController controller = game.getInput();

				if (controller != null) {

						if (controller.isAtLeftBoundary()) {
								if(velocity.getValue() < 0) {
										velocity.reset();
										//acceleration.reset();
										//sampler.reset();
								}

						}else if(controller.isAtRightBoundary()) {
								if(velocity.getValue() > 0) {
										velocity.reset();
										//acceleration.reset();
										//sampler.reset();
								}
						}
						position.onVelocityUpdate(dt);
						//controller.movePaddleToAbsPos(xPos);
				}
		}

		public double getElapsedTime() {
				return sampler.getElapsedTime();
		}

		public double getAcceleration() {
				return acceleration.getValue();
		}

		/*public double getAccelerationRateOfChange() {
				return acceleration.getRateOfChange();
		}*/

		public double getVelocity() {
				return velocity.getValue();
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

				float xPos = (WIDTH / 2 + (float) position.getValue() * 200000 / 100);

				return xPos;
		}
}
