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

		public double getValue() {
				return p;
		}

		public void setValue(double p) {
				this.p = p;
		}

}
