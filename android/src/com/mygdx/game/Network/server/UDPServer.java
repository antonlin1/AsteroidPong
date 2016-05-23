package com.mygdx.game.Network.server;

import com.mygdx.game.Network.InetUtil;
import com.mygdx.game.Network.MessageHolder;
import com.mygdx.game.NetworkComponentInterface;
import com.mygdx.game.model.ClientToServerMessage;
import com.mygdx.game.model.ServerToClientMessage;
import com.mygdx.game.view.States.GameState;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by hampusballdin on 2016-04-20.
 */
public class UDPServer extends Thread implements NetworkComponentInterface {
		private DatagramSocket socket;
		private MessageHolder messageHolder;
		private ClientToServerMessage clientToServerMessage = new ClientToServerMessage(false, false, 0.0f, 0.0f);
		private static boolean IS_CONNECTION_OPEN = false;
		private InetAddress clientAddress = null;
		private final Object clientAddressLock = new Object();

		//private GameState gameState;
		public UDPServer(MessageHolder messageHolder, InetAddress clientAddress) {
				this.messageHolder = messageHolder;
				this.clientAddress = clientAddress;


				System.out.println("Creating Socket");
				try {
						socket = new DatagramSocket(null);
						socket.setReuseAddress(true);
						socket.bind(new InetSocketAddress(InetUtil.PORT));
				} catch (SocketException e) {
						e.printStackTrace();
				}

				System.out.println("socket: " + socket);
		}

		@Override
		public void run() {
				System.out.println("Server Start");
				ClientMessageReader clientMessageReader = new ClientMessageReader();
				clientMessageReader.start();
				byte[] bytes = new byte[1];

				if(clientAddress == null) {
						synchronized (clientAddressLock) {
								while (clientAddress == null) {
										try {
												clientAddressLock.wait();
										} catch (InterruptedException e) {
												e.printStackTrace();
										}
								}
								clientAddressLock.notifyAll();
						}
				}else {
						/**
						 * Send dummy message to client for it to discover server address ...
						 */

						byte[] tmp = ServerToClientMessage.DEFAULT_MESSAGE.toString().getBytes();
						try {
								socket.send(new DatagramPacket(tmp, tmp.length, clientAddress, InetUtil.PORT));
						} catch (IOException e) {
								e.printStackTrace();
						}

				}
				DatagramPacket dp = new DatagramPacket(bytes, bytes.length, clientAddress, InetUtil.PORT);

				while (!isInterrupted()) {
						//Message from either self or other phone
						bytes = messageHolder.withdraw().getBytes();
						dp.setData(bytes);
						dp.setLength(bytes.length);

						try {
								socket.send(dp);
						} catch (IOException e) {
								e.printStackTrace();
						}
				}

				socket.close();

		}

		private class ClientMessageReader extends Thread {
				private DatagramPacket dp;
				private byte[] data = new byte[InetUtil.UDP_RECEIVE_BUFFER_SIZE];

				public ClientMessageReader() {
						dp = new DatagramPacket(data, data.length);
				}

				@Override
				public void run() {
						try {
								// Receive first to get client IP (will always be the same after) ...
								if(clientAddress == null) {
										socket.receive(dp);
										String message = new String(dp.getData(), 0, dp.getLength());
										dp.setLength(data.length);
										clientToServerMessage.setMessage(message);

										synchronized (clientAddressLock) {
												if (clientAddress == null) {
														clientAddress = dp.getAddress();
														System.out.println("GOT ADDRESS!");
														clientAddressLock.notifyAll();
												}
										}
								}
								String message = "";
								while (!isInterrupted()) {
										socket.receive(dp);
										message = new String(dp.getData(), 0, dp.getLength());
										dp.setLength(data.length);
										clientToServerMessage.setMessage(message);
								}

						} catch (IOException e) {
								e.printStackTrace();
						}
						//server.setServerMessage(message);
				}
		}

		@Override
		public void setClientToServerData(boolean gameActive, boolean gamePaused, float paddleX, float paddleY) {
				//Do nothing
		}

		@Override
		public void setServerToClientData(boolean gameActive, boolean gamePaused, boolean paddleCollision,
										  boolean wallCollision, float paddleX, float paddleY,
										  float ballX, float ballY, float ballXVelocity,
										  float ballYVelocity, float ballVelocity,
										  double screenWidth, double screenHeight, int hpUp, int hpDown, GameState.GameOverEvent gameOverEvent) {

				ServerToClientMessage serverToClientMessage =
						new ServerToClientMessage(gameActive, gamePaused,paddleCollision, wallCollision,
								paddleX,paddleY, ballX, ballY, ballXVelocity,
						ballYVelocity, ballVelocity, screenWidth, screenHeight, hpUp, hpDown, gameOverEvent);

				messageHolder.deposit(serverToClientMessage.toString());
		}

		@Override
		public String getData() {
				return clientToServerMessage.toString();
		}

		@Override
		public ClientToServerMessage getClientData() {
				return clientToServerMessage;
		}

		@Override
		public ServerToClientMessage getServerData() {
				return null;
		}

		@Override
		public float getOpponentPaddleX() {
				return clientToServerMessage.getPaddleX();
		}

		@Override
		public boolean isConnectionOpen() {
				return IS_CONNECTION_OPEN;
		}


		/**
		 * Not yet implemented
		 */
		@Override
		public boolean isServerUpdated() {
				return false;
		}

		/**
		 * Do nothing
		 */
		@Override
		public boolean isClientUpdated() {
				return false;
		}

		@Override
		public float getOpponentPaddleY() {
				return clientToServerMessage.getPaddleY();
		}
}
