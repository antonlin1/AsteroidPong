package com.mygdx.game.model;

/**
 * Created by antonlin on 16-04-26.
 */
public class ClientToServerMessage {

		// Client device's x position
		private float paddleX;
		private float paddleY;

		public ClientToServerMessage(float paddleX, float paddleY) {
				this.paddleX = paddleX;
				this.paddleY = paddleY;
		}

		public static final ClientToServerMessage DEFAULT_MESSAGE = new ClientToServerMessage(0f, 0f);

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
						paddleX = Float.parseFloat(attributes[0]);
						paddleY = Float.parseFloat(attributes[1]);
				}
		}

		public void setMessage(String data) {
				parse(data);
		}
}
