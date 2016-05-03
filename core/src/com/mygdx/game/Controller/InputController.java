package com.mygdx.game.Controller;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.view.States.GameState;

/**
 * Created by antonlin on 16-04-12.
 */
public class InputController {

		private GameState state;
		private float x;


		public InputController(GameState state) {
				this.state = state;
		}

		public void movePaddleToAbsPos(float x) {
				this.x = x;

				//float validX = (x > Gdx.graphics.getWidth() - GameState.PaddleConstant.LENGTH.value) ?
				//		Gdx.graphics.getWidth() - GameState.PaddleConstant.LENGTH.value : x;


				//validX = (validX < 0) ? 0 : validX;



				if(isAtRightBoundary()) {
						state.getPaddles()[0].setX(Gdx.graphics.getWidth() - GameState.PaddleConstant.LENGTH.value);
				}else if(isAtLeftBoundary()){
						state.getPaddles()[0].setX(0);
				}else {
						state.getPaddles()[0].setX(x);
				}

//        state.getPaddles()[1].setX(validX);
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
