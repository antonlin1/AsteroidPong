package com.mygdx.game.model;

/**
 * Created by antonlin on 16-04-26.
 */
public class ClientToServerMessage {

		private boolean isActive;
		private boolean isPaused;
		// Client device's x position
		private float paddleX;
		private float paddleY;

		public ClientToServerMessage(boolean isActive, boolean isPaused,float paddleX, float paddleY) {
				this.isActive = isActive;
				this.isPaused = isPaused;
				this.paddleX = paddleX;
				this.paddleY = paddleY;
		}

		public ClientToServerMessage(String data) {
				parse(data);
		}

		public float getPaddleX() {
				return paddleX;
		}

		public float getPaddleY() {
				return paddleY;
		}


		public String toString() {
				return paddleX + ":" + paddleY;
		}

		private void parse(String data) {
				if(data != null) {
						String[] attributes = data.split(":");
						isActive = Boolean.parseBoolean(attributes[0]);
						isPaused = Boolean.parseBoolean(attributes[1]);
						paddleX = Float.parseFloat(attributes[2]);
						paddleY = Float.parseFloat(attributes[3]);
				}
		}

		public void setMessage(String data) {
				parse(data);
		}
}
