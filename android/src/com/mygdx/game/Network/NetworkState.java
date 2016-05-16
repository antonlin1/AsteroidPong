package com.mygdx.game.Network;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hampusballdin on 2016-04-21.
 */
public class NetworkState {

		//	public volatile static boolean IS_CONNECTING = false;
		public volatile static boolean IS_CONNECTED = false;

		public volatile static boolean IS_DISCOVERING = false;

		public volatile static boolean HAS_DISCOVERED = false;
		public volatile static boolean IS_GROUPING = false;

		public volatile static boolean IS_CONNECTED_TO_SERVER = false;

		public volatile static boolean HAS_CONNECTION_FROM_CLIENT = false;

		public static volatile AtomicBoolean IS_CONNECTING = new AtomicBoolean(false);

		public static volatile AtomicBoolean IS_SERVER = new AtomicBoolean(false);

		public static volatile AtomicBoolean IS_CLIENT = new AtomicBoolean(false);

		public static volatile AtomicBoolean IS_CONNECTING_RESET = new AtomicBoolean(false);

		public static volatile AtomicBoolean IS_DISCOVERING_RESET = new AtomicBoolean(false);

/*

		public static void onBeginConnect() {


		}


		public static void onConnectBeginFailure() {


		}


		public static void onConnectBeginSuccess() {


		}


		public static void onBeginDiscover() {


		}



		public static void onDiscoverBeginFailure() {


		}


 		 //Discover on physical (wifi-direct) layer begin.

		public static void onDiscoverBeginSuccess() {


		}


		// Discovered on physical (wifi-direct) layer.

		public static void onDiscovered() {


		}


	//	 Connected on physical (wifi-direct) layer.

		public static void onConnected() {


		}


		  //IF IS_CLIENT:
		  // 	We started a socket and connected to remote server
		 //  IF IS_SERVER:
		 // 	Other client has connected to us.

		public static void onServerClientConnected() {
				if (IS_CLIENT) {

				}

				if (IS_SERVER) {

				}
		}


		public static void onClientDisconnected() {
				if (IS_CLIENT) {

				}

				if (IS_SERVER) {

				}
		}


		// IF IS_CLIENT:
		//  	We started a socket and connected to remote server
		//  IF IS_SERVER:
		//  	Other client has connected to us.

		public static void onServerDisconnected() {
				if (IS_CLIENT) {


				}

				if (IS_SERVER) {


				}
		}*/
}
