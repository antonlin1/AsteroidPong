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

		private int N = 8;

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

				if (controller != null && game.getStateManager().getActiveState().isActive()) {
						position.onVelocityUpdate(dt);
						double currentPos = getNormalizedPosition(null); //% controller.getRightBoundary();

						//if(currentPos < 0)
						//		currentPos += controller.getRightBoundary();


						if (currentPos < controller.getLeftBoundary()) {
								position.setValue(getPositionFromNormalized(controller.getLeftBoundary() + 1));
						}else if(currentPos > controller.getRightBoundary()) {
								position.setValue(getPositionFromNormalized(controller.getRightBoundary() - 1));
						}

						//System.out.println("controller.getLeftBoundary() = " + controller.getLeftBoundary());
						//System.out.println("controller.getRightBoundary() = " + controller.getRightBoundary());
						//System.out.println("currentPos = " + currentPos);

						currentPos = getNormalizedPosition(null);
						controller.movePaddleToAbsPos((float)currentPos);
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

		private static final double SCALE = 200000 / 40;

		@Override
		public double getNormalizedPosition(MyGdxGame game) {
				double WIDTH = Gdx.graphics.getWidth();
				double xPos = (WIDTH / 2 + position.getValue() * SCALE);
				return xPos;
		}

		public double getPositionFromNormalized(double normalizedPosition) {
				double WIDTH = Gdx.graphics.getWidth();
				return (normalizedPosition - WIDTH / 2) / SCALE;
		}

}
