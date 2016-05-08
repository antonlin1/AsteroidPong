package com.mygdx.game.view.States;

import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.view.MyGdxGame;

/**
 * Created by hampusballdin on 2016-05-08.
 */
public abstract class Multiplayer extends GameState {

		public Multiplayer(MyGdxGame game, StateManager stateManager, float width, float height,
						   PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect, StateManager.STATE_NAME state) {
				super(game, stateManager, width, height, peerHelper, wifiDirect, state);
		}
}
