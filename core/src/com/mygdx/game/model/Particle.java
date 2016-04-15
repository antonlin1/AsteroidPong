package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by johanmansson on 16-04-11.
 */
public class Particle {

		private float xPos;
		private float yPos;
		private long timeStamp;


		public Particle(float xPos, float yPos, long timeStamp) {
				this.xPos = xPos;
				this.yPos = yPos;
				this.timeStamp = timeStamp;


		}

		public float getX() {
				return xPos;
		}

		public float getY() {
				return yPos;
		}

		public long getTimeStamp() {
				return timeStamp;
		}


}
