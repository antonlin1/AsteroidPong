package com.mygdx.game.Controller;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.view.States.GameState;

/**
 * Created by antonlin on 16-04-12.
 */
public class InputController {
		private float x;

		private GameState state;

		public void movePaddleToAbsPos(float x) {
				this.x = x;
				if(state != null) {
						state.getPaddles()[0].setX(x);
				}
		}

		public void setGameState(GameState state) {
				this.state = state;
		}

		public void movePaddleWithDelta(int x) {

		}

		/**
		 *        GameState.PaddleConstant.LENGTH.value
		 * o----------------------------------------------o
		 * x
		 *
		 *
		 * @return
		 */

		public boolean isAtRightBoundary() {

				return x > (Gdx.graphics.getWidth() - GameState.PaddleConstant.LENGTH.value);
		}

		public boolean isAtLeftBoundary() {

				return x < 0;
		}

		public boolean isAtBoundary() {
				return isAtLeftBoundary() || isAtRightBoundary();
		}

		public double getRightBoundary() {

				return Gdx.graphics.getWidth() - GameState.PaddleConstant.LENGTH.value;

		}

		public double getLeftBoundary() {

				return 0.0;

		}

}
