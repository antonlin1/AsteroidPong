package com.mygdx.game.view.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

/**
 * Created by johanmansson on 16-04-20.
 */
public class StateManager {

		public enum STATE_NAME {
				HOW_TO_PLAY_STATE, MULTIPLAYER_SERVER_STATE, MULTIPLAYER_CLIENT_STATE,
				SINGLEPLAYER_STATE, MENU_STATE, FIND_GAME_STATE;
		}
		private Stack<com.mygdx.game.view.States.State> states;
		private HashMap<STATE_NAME, State> savedStates = new HashMap<STATE_NAME, State>();
		private STATE_NAME activeState;

		public StateManager() {
				states = new Stack<com.mygdx.game.view.States.State>();
		}

		public void push(com.mygdx.game.view.States.State state) {
				states.push(state);
				setActiveState(states.peek().getStateName());
		}

		public void pop() {
				states.pop();
				setActiveState(states.peek().getStateName());
		}


		public void set(com.mygdx.game.view.States.State state) {
				states.pop();
				states.push(state);
				setActiveState(states.peek().getStateName());
		}


		/**
		 * Associates the specified value with the specified key in this map.
		 * If the map previously contained a mapping for the key, the old value is replaced.
		 *
		 * Returns the old state associated with stateName, or null if no such value ...
		 */
		public State add(STATE_NAME stateName, State state) {
				return savedStates.put(stateName, state);
		}

		public State set(STATE_NAME stateName) {
				State state = savedStates.get(stateName);
				if(state != null) {
						states.pop();
						states.push(state);
						setActiveState(states.peek().getStateName());
				}
				return state;
		}

		private void setActiveState(STATE_NAME state) {
				activeState = state;
				System.out.println("STATE: " + activeState);
		}

		public STATE_NAME getActiveState() {
				return activeState;
		}

		public void update() {
				states.peek().update();
		}

		public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
				states.peek().render(spriteBatch, shapeRenderer);
		}

		public boolean lastItem() {
				if (states.size() == 1) {
						return true;
				}
				return false;
		}
}
