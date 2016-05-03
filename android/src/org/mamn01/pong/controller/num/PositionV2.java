package org.mamn01.pong.controller.num;

/**
 * Created by hampusballdin on 2016-04-12.
 */
public class PositionV2 {

		private VelocityV2 velocity;
		private double p;

		public PositionV2(VelocityV2 velocity) {
				this.velocity = velocity;
		}

		public void onVelocityUpdate(double dt) {
				double v = velocity.getValue();
				p += v * dt;
		}

		double getValue() {
				return p;
		}

		public void onVelocityUpdateLeftBoundary(double dt) {
				double v = velocity.getValue();
				if(v > 0)
						p += v * dt;
		}

		public void onVelocityUpdateRightBoundary(double dt) {
				double v = velocity.getValue();
				if(v < 0)
						p += v * dt;
		}


}
