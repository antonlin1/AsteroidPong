package com.mygdx.game.model;

/**
 * Created by antonlin on 16-04-26.
 */
public class ClientToServerMessage {

	private boolean gameActive = false;
	private boolean gamePaused = false;

	// Client device's x position
		private float paddleX;
	private float paddleY;
		public ClientToServerMessage(boolean gameActive, boolean gamePaused, float paddleX, float paddleY) {
				this.gameActive = gameActive;
				this.gamePaused = gamePaused;
				this.paddleX = paddleX;
				this.paddleY = paddleY;
		}

		public static final ClientToServerMessage DEFAULT_MESSAGE = new ClientToServerMessage(false, false,0f, 0f);

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

			StringBuilder returnString = new StringBuilder("");

			returnString.append((gameActive) ? "1" : "0");
			returnString.append(":");
			returnString.append((gamePaused) ? "1" : "0");
			returnString.append(":");
			returnString.append(paddleX);
			returnString.append(":");
			returnString.append(paddleY);

			System.out.print(returnString.toString());

			return returnString.toString();
		}


		private void parse(String data) {
				if(data != null) {
						String[] attributes = data.split(":");
						gameActive = (Integer.parseInt(attributes[0]) == 1);
						gamePaused = (Integer.parseInt(attributes[1]) == 1);
						paddleX = Float.parseFloat(attributes[2]);
						paddleY = Float.parseFloat(attributes[3]);
				}
		}

		public void setMessage(String data) {
				parse(data);
		}

	public boolean isGamePaused() {
		return gamePaused;
	}

	public boolean isGameActive() {
		return gameActive;
	}
}
