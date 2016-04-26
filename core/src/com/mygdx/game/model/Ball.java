package com.mygdx.game.model;

/**
 * Created by antonlin on 16-04-11.
 */
public class Ball {

		private float xPos;
		private float yPos;
		private float radius;

		private float xVelocity;
		private float yVelocity;

	private final float VELOCITY = 8f;

		public Ball(float xPos, float yPos, float radius) {

				this.xPos = xPos;
				this.yPos = yPos;
				this.radius = radius;

				//Somewhat randomize ball start direction
				randomizeVelocity();
		}


		public float getX() {
				return xPos;
		}

		public void setX(float xPos) {
				this.xPos = xPos;
		}

		public float getY() {
				return yPos;
		}

		public float getXVel() {
				return xVelocity;
		}

		public float getYVel() {
				return yVelocity;
		}

		public void setY(float yPos) {
				this.yPos = yPos;
		}


		public float getRadius() {
				return radius;
		}

		public void setRadius(float radius) {
				this.radius = radius;
		}

		public float getVelocity() {
				return VELOCITY;
		}

		public void randomizePos(float width, float height) {

				float minX = (float) (width * 0.1);
				float minY = (float) (height * 0.1);
				float maxX = (float) (width * 0.9);
				float maxY = (float) (height * 0.9);

				float x = minX + (maxX - minX) * (float) Math.random();
				float y = minY + (maxY - minY) * (float) Math.random();

				setX(x);
				setY(y);
				randomizeVelocity();


		}

		public void randomizeVelocity() {
				xVelocity = (Math.random() < 0.5) ? VELOCITY : -VELOCITY;
				yVelocity = (Math.random() < 0.5) ? VELOCITY : -VELOCITY;
		}

		public void move() {
				xPos += xVelocity;
				yPos += yVelocity;
		}

		public void move(double dx, double dy) {
				xPos += xVelocity / Math.abs(xVelocity) * dx;
				yPos += yVelocity / Math.abs(yVelocity) * dy;
		}

		public void kill() {
				yVelocity = 0;
				xVelocity = 0;
		}

		public void reverseYVelocity(){
				yVelocity *= -1;
		}

		public void reverseXVelocity(){
				xVelocity *= -1;
		}

	public void setxPos(float xPos) {
		this.xPos = xPos;
	}

	public void setyPos(float yPos) {
		this.yPos = yPos;
	}

	public void setxVelocity(float xVelocity) {
		this.xVelocity = xVelocity;
	}

	public void setyVelocity(float yVelocity) {
		this.yVelocity = yVelocity;
	}
}
