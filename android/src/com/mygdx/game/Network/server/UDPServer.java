package com.mygdx.game.Network.server;

import com.mygdx.game.Network.InetUtil;
import com.mygdx.game.Network.MessageHolder;
import com.mygdx.game.NetworkComponentInterface;
import com.mygdx.game.model.ClientToServerMessage;
import com.mygdx.game.model.ServerToClientMessage;

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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by hampusballdin on 2016-04-20.
 */
public class UDPServer extends Thread implements NetworkComponentInterface {
		private DatagramSocket socket;
		private MessageHolder messageHolder;
		private ClientToServerMessage clientToServerMessage = new ClientToServerMessage(0.0f, 0.0f);
		private static boolean IS_CONNECTION_OPEN = false;
		private static InetAddress clientAddress = null;
		private static final Object clientAddressLock = new Object();

		//private GameState gameState;
		public UDPServer(MessageHolder messageHolder/*, GameState gameState*/) {
				this.messageHolder = messageHolder;
//        this.gameState = gameState;
				try {
						socket = new DatagramSocket(InetUtil.PORT);
				} catch (SocketException e) {
						e.printStackTrace();
				}
		}

		@Override
		public void run() {
				System.out.print("Server Start");

				ClientMessageReader clientMessageReader = new ClientMessageReader(messageHolder, this, socket);
				clientMessageReader.start();

				while (!isInterrupted()) {

						System.out.println("Waiting for a new line");

						//Message from either self or other phone
						String message = messageHolder.withdraw();
						System.out.println("Server sending: " + message);

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

						byte[] bytes = message.getBytes();
						DatagramPacket dp = new DatagramPacket(bytes, bytes.length, clientAddress, InetUtil.PORT);

						try {
								socket.send(dp);
						} catch (IOException e) {
								e.printStackTrace();
						}
				}

				socket.close();

		}

		private static class ClientMessageReader extends Thread {

				private MessageHolder messageHolder;

				private UDPServer server;
				private DatagramSocket socket;


				public ClientMessageReader(MessageHolder messageHolder, UDPServer server, DatagramSocket socket) {
						this.messageHolder = messageHolder;
						this.server = server;
						this.socket = socket;
				}

				@Override
				public void run() {
						try {
								while (!isInterrupted()) {
										byte[] data = new byte[InetUtil.UDP_RECEIVE_BUFFER_SIZE];
										DatagramPacket dp = new DatagramPacket(data, data.length);
										socket.receive(dp);
										String message = new String(dp.getData(), 0, dp.getLength());
										System.out.println("Server Receive: " + message);

										synchronized (clientAddressLock) {
												if (clientAddress == null) {
														clientAddress = dp.getAddress();
														clientAddressLock.notifyAll();
												}
										}
										server.clientToServerMessage = new ClientToServerMessage(message);
								}
						} catch (IOException e) {
								e.printStackTrace();
						}
						//server.setServerMessage(message);
				}
		}

		@Override
		public void setClientToServerData(float paddleX, float paddleY) {
				//Do nothing
		}

		@Override
		public void setServerToClientData(boolean gameActive, boolean paddleCollision,
										  boolean wallCollision, float paddleX,float paddleY,
										  float ballX, float ballY, float ballXVelocity,
										  float ballYVelocity, float ballVelocity,
										  double screenWidth, double screenHeight) {

				ServerToClientMessage serverToClientMessage =
						new ServerToClientMessage(gameActive, paddleCollision, wallCollision,
								paddleX,paddleY, ballX, ballY, ballXVelocity,
						ballYVelocity, ballVelocity, screenWidth, screenHeight);


				messageHolder.deposit(serverToClientMessage.toString());
//        ClientToServerMessage clientMessage = new ClientToServerMessage(data);
//        gameState.setPaddle2(clientMessage.getPaddleX());
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
				return clientToServerMessage.getPaddleY()
						;
		}
}
