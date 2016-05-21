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
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hampusballdin on 2016-04-20.
 */
public class Server extends Thread implements NetworkComponentInterface {
		private ServerSocket serverSocket;
		private Socket clientSocket;
		private MessageHolder messageHolder;
		private ClientToServerMessage clientToServerMessage = new ClientToServerMessage(false,false,0.0f, 0.0f);
		private static boolean IS_CONNECTION_OPEN = false;

		//private GameState gameState;
		public Server(MessageHolder messageHolder/*, GameState gameState*/) {
				this.messageHolder = messageHolder;
//        this.gameState = gameState;
		}

		@Override
		public void run() {
				System.out.print("Server Start");

				try {

						serverSocket = new ServerSocket(InetUtil.PORT);
						serverSocket.setSoTimeout(Integer.MAX_VALUE);

						String serverAddress = serverSocket.getInetAddress().getHostAddress();
						int port = serverSocket.getLocalPort();
						System.out.println(" bound at IP: " + serverAddress + ", PORT: " + port);
						clientSocket = serverSocket.accept();
						clientSocket.setSoTimeout(100000);

						System.out.println("New Connection from IP " + clientSocket.getInetAddress().getHostAddress());

						InputStream in = clientSocket.getInputStream();
						OutputStream out = clientSocket.getOutputStream();

						BufferedReader br = new BufferedReader(new InputStreamReader(in));
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));

						ClientMessageReader cMReader = new ClientMessageReader(messageHolder, this, br);
						cMReader.start();

						IS_CONNECTION_OPEN = true;

						while (!isInterrupted()) {

								System.out.println("Waiting for a new line");

								//Message from either self or other phone
								String message = messageHolder.withdraw();
								System.out.println("Server sending: " + message);

								//viewModifier.setTextView(concatenatedString);
								bw.write(message + "\n"); // Line feed ...
								bw.flush();
						}

				} catch (IOException e) {
						e.printStackTrace();
				} finally {
						System.out.println("Closing Server Socket ... ");
						try {
								serverSocket.close();
						} catch (IOException e) {
								e.printStackTrace();
						}
				}
		}

		private static class ClientMessageReader extends Thread {

				private MessageHolder messageHolder;

				private Server server;
				private BufferedReader br;

				public ClientMessageReader(MessageHolder messageHolder, Server server, BufferedReader br) {
						this.messageHolder = messageHolder;
						this.server = server;
						this.br = br;
				}

				@Override
				public void run() {
						try {
								while (!isInterrupted()) {
										String message = br.readLine().toString();
										server.clientToServerMessage = new ClientToServerMessage(message);
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
		public void setServerToClientData(boolean gameActive, boolean gamePaused,boolean paddleCollision, boolean wallCollision, float paddleX, float paddleY,
										  float ballX, float ballY, float ballXVelocity, float ballYVelocity, float ballVelocity,
										  double screenWidth, double screenHeight, int hpUp, int hpDown) {
				ServerToClientMessage serverToClientMessage = new ServerToClientMessage(gameActive, gamePaused,paddleCollision,
						wallCollision, paddleX,paddleY, ballX, ballY, ballXVelocity, ballYVelocity, ballVelocity, screenWidth, screenHeight, hpUp, hpDown);
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
		 *
		 * Not yet implemented
		 */
		@Override
		public boolean isServerUpdated() {
				return false;
		}

		/**
		 *
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
