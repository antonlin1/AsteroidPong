package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.view.MyGdxGame;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by johanmansson on 16-04-20.
 */
public class StateManager {

		public enum STATE_NAME {
				HOW_TO_PLAY_STATE, MULTIPLAYER_SERVER_STATE, MULTIPLAYER_CLIENT_STATE,
				SINGLEPLAYER_STATE, MENU_STATE, FIND_GAME_STATE, PAUSE_STATE, CREATE_GAME_DIALOGUE_STATE;
		}
		private Stack<com.mygdx.game.view.States.State> states;
		private HashMap<STATE_NAME, State> savedStates = new HashMap<STATE_NAME, State>();
		private STATE_NAME activeStateName;
		private MyGdxGame game;
		private State activeState;

		public StateManager(MyGdxGame game, WifiDirectInterface wifiDirect,
							PeerHelperInterface peerHelperInterface) {
				states = new Stack<com.mygdx.game.view.States.State>();
				this.game = game;
		}

		public void push(com.mygdx.game.view.States.State state) {
				states.push(state);
			activeState = state;
			setActiveStateName(activeState.getStateName());
			System.out.println(state.getStateName());

			handleInputProcessor();
		}

		public void pop() {
				states.pop();
			activeState = states.peek();
			setActiveStateName(activeState.getStateName());
				handleInputProcessor();
		}


		public void set(com.mygdx.game.view.States.State state) {
				states.pop();
				states.push(state);
				setActiveStateName(states.peek().getStateName());
		}

		public void setStandardInputProcessor() {
			Gdx.input.setInputProcessor(game);
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
						setActiveStateName(states.peek().getStateName());
				}
				return state;
		}

		private void setActiveStateName(STATE_NAME state) {
				activeStateName = state;
				System.out.println("STATE: " + activeStateName);
		}

		private void handleInputProcessor() {

			if(activeStateName == STATE_NAME.FIND_GAME_STATE){
				((FindGameState)activeState).setupCustomInputProcessor();
			} else if(activeStateName == STATE_NAME.CREATE_GAME_DIALOGUE_STATE) {
				((CreateGameDialogueState) activeState).setupCustomInputProcessor();
			} else {
				setStandardInputProcessor();
			}
		}

		public STATE_NAME getActiveStateName() {
				return activeStateName;
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
