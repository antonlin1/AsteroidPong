package com.mygdx.game.model;

import java.util.IllegalFormatConversionException;
import java.util.IllegalFormatException;

/**
 * Created by antonlin on 16-04-26.
 */

public class ServerToClientMessage {
		private boolean gameActive;
		private boolean paddleCollision;
		private boolean wallCollision;
		// Server device's x position
		private float paddleX;
		private float paddleY;

		private float ballX;
		private float ballY;

		private float ballXVelocity;

		private float ballYVelocity;

		private float ballVelocity;

		private double screenWidth;
		private double screenHeight;


		private long nanoTime;

		public ServerToClientMessage() {
				this.gameActive = false;
				this.paddleCollision = false;
				this.wallCollision = false;
				this.paddleX = 0;
				this.ballX = 0;
				this.ballY = 0;

				this.ballXVelocity = 0;
				this.ballYVelocity = 0;
				this.ballYVelocity = 0;

				this.screenWidth = 0;
				this.screenHeight = 0;

		}

		public ServerToClientMessage(boolean gameActive, boolean paddleCollision, boolean wallCollision,
									 float paddleX, float paddleY, float ballX, float ballY,
									 float ballXVelocity, float ballYVelocity, float ballVelocity,
									 double screenWidth, double screenHeight) {
				this.gameActive = gameActive;
				this.paddleCollision = paddleCollision;
				this.wallCollision = wallCollision;
				this.paddleX = paddleX;
				this.paddleY = paddleY;
				this.ballX = ballX;
				this.ballY = ballY;

				this.ballXVelocity = ballXVelocity;
				this.ballYVelocity = ballYVelocity;
				this.ballVelocity = ballVelocity;

				this.screenWidth = screenWidth;
				this.screenHeight = screenHeight;
		}

		public ServerToClientMessage(String data) {
				parse(data);
		}

		public boolean isGameActive() {
				return gameActive;
		}

		public boolean isPaddleCollision() {
				return paddleCollision;
		}

		public boolean isWallCollision() {
				return wallCollision;
		}

		public float getPaddleX() {
				return paddleX;
		}

		public float getPaddleY() {
				return paddleY;
		}

		public float getBallX() {
				return ballX;
		}

		public float getBallY() {
				return ballY;
		}

		public float getBallXVelocity() {
				return ballXVelocity;
		}

		public float getBallYVelocity() {
				return ballYVelocity;
		}

		public float getBallVelocity() {
				return ballVelocity;
		}

		public double getScreenWidth() {
				return screenWidth;
		}

		public double getScreenHeight() {
				return screenHeight;
		}

		public long getNanoTime() {
				return nanoTime;
		}
		//Every attribute separated by ':'
		public String toString() {
				StringBuilder returnString = new StringBuilder("");

				returnString.append((gameActive) ? "1" : "0");
				returnString.append(":");
				returnString.append((paddleCollision) ? "1" : "0");
				returnString.append(":");
				returnString.append((wallCollision) ? "1" : "0");
				returnString.append(":");
				returnString.append(paddleX);
				returnString.append(":");
				returnString.append(paddleY);
				returnString.append(":");
				returnString.append(ballX);
				returnString.append(":");
				returnString.append(ballY);
				returnString.append(":");
				returnString.append(ballXVelocity);
				returnString.append(":");
				returnString.append(ballYVelocity);
				returnString.append(":");
				returnString.append(ballVelocity);
				returnString.append(":");
				returnString.append(System.nanoTime());
				returnString.append(":");
				returnString.append(screenWidth);
				returnString.append(":");
				returnString.append(screenHeight);
				System.out.print(returnString.toString());

				return returnString.toString();
		}

		private void parse(String data) throws IllegalFormatException {
				String[] attributes = data.split(":");

				try {

						/*
						 * Unnecessary syntax
						 */
						gameActive = (Integer.parseInt(attributes[0]) == 1) ? true : false;
						paddleCollision = (Integer.parseInt(attributes[1]) == 1) ? true : false;
						wallCollision = (Integer.parseInt(attributes[2]) == 1) ? true : false;

						paddleX = Float.parseFloat(attributes[3]);
						paddleY = Float.parseFloat(attributes[4]);

						ballX = Float.parseFloat(attributes[5]);
						ballY = Float.parseFloat(attributes[6]);

						ballXVelocity = Float.parseFloat(attributes[7]);
						ballYVelocity = Float.parseFloat(attributes[8]);
						ballVelocity = Float.parseFloat(attributes[9]);
						nanoTime = Long.parseLong(attributes[10]);

						screenWidth = Double.parseDouble(attributes[11]);
						screenHeight = Double.parseDouble(attributes[12]);

				} catch (Exception e) {
						throw new IllegalFormatConversionException('b', this.getClass());
				}
		}
}
