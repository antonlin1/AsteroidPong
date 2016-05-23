package com.mygdx.game.Network.client;

import com.mygdx.game.Network.InetUtil;
import com.mygdx.game.Network.MessageHolder;
import com.mygdx.game.Network.WifiDirectBroadcastReceiver;
import com.mygdx.game.NetworkComponentInterface;
import com.mygdx.game.model.ClientToServerMessage;
import com.mygdx.game.model.ServerToClientMessage;
import com.mygdx.game.view.States.GameState;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by hampusballdin on 2016-04-20.
 */
public class UDPClient extends Thread implements NetworkComponentInterface {

		private DatagramSocket socket;
		private InetAddress serverAddress;
		private Reader reader;
		private Writer writer;
		private MessageHolder messageHolder;
		private ServerToClientMessage serverToClientMessage = new ServerToClientMessage();
		private static boolean IS_CONNECTION_OPEN = false;
		private WifiDirectBroadcastReceiver wifiDirectBroadcastReceiver;
		private static boolean isUpdated = false;

		private static final Object serverAddressLock = new Object();


		/**
		 * @param address, will be null if groupowner == client
		 * @param messageHolder
		 * @param wifiDirectBroadcastReceiver
		 */
		public UDPClient(InetAddress address, MessageHolder messageHolder, WifiDirectBroadcastReceiver wifiDirectBroadcastReceiver) {
				this.serverAddress = address;
				this.messageHolder = messageHolder;
				try {
						socket = new DatagramSocket(null);
						socket.setReuseAddress(true);
						socket.bind(new InetSocketAddress(InetUtil.PORT));
				} catch (SocketException e) {
						e.printStackTrace();
				}
				this.wifiDirectBroadcastReceiver = wifiDirectBroadcastReceiver;

		}

		// Write from this thread, read from new Thread ...
		@Override
		public void run() {
				System.out.println("Client Start");

				IS_CONNECTION_OPEN = true;
				reader = new Reader();
				reader.start();

				boolean hasServerAddress = false;

				//Test
				if(serverAddress == null) {
						synchronized (serverAddressLock) {
								while (serverAddress == null) {
										try {
												serverAddressLock.wait();
										} catch (InterruptedException e) {
												e.printStackTrace();
										}
								}
								serverAddressLock.notifyAll();
						}
				}else {
						hasServerAddress = true;
				}
				writer = new Writer();


				if(hasServerAddress) { // Server does not yet have our address
						try {
								writer.writeLine(ClientToServerMessage.DEFAULT_MESSAGE.toString());
						} catch (IOException e) {
								e.printStackTrace();
						}
				}
				// Write Forever ...
				try {
						while (IS_CONNECTION_OPEN && !isInterrupted()) {
								writer.writeLine(messageHolder.withdraw());
						}
				} catch (IOException e) {
						e.printStackTrace();
				} finally {
						socket.close();
				}
				wifiDirectBroadcastReceiver.reconnectToServer();
		}

		private class Reader extends Thread {
			//	private ReaderCallback readerCallback;
				private boolean isClosed = false;
				private DatagramPacket dp;
				private byte[] data = new byte[InetUtil.UDP_RECEIVE_BUFFER_SIZE];

				public Reader() {
						dp = new DatagramPacket(data, data.length);
				}

				@Override
				public void run() {
						try {
								if(serverAddress == null) {
										socket.receive(dp);
										String message = new String(dp.getData(), 0, dp.getLength());
										dp.setLength(data.length);
										serverToClientMessage.setMessage(message);

										synchronized (serverAddressLock) {
												if (serverAddress == null) {
														serverAddress = dp.getAddress();
														serverAddressLock.notifyAll();
												}
										}
								}

								while (!isInterrupted() && !isClosed) {
												//String line = br.readLine();
												socket.receive(dp);
												String line = new String(dp.getData(), 0, dp.getLength());
												dp.setLength(data.length);
												serverToClientMessage.setMessage(line);
												isUpdated = true;
								}

						} catch (IOException e) {
								e.printStackTrace();
								IS_CONNECTION_OPEN = false;
						}
				}
		}

		private class Writer {
				private DatagramPacket dp;
				private byte[] bytes = new byte[1];

				public Writer() {
						dp = new DatagramPacket(bytes, bytes.length, serverAddress, InetUtil.PORT);
				}

				public void writeLine(String line) throws IOException {
								bytes = line.getBytes();
								dp.setData(bytes);
								dp.setLength(bytes.length);
								socket.send(dp);
				}
		}

		@Override
		public void setClientToServerData(boolean gameActive, boolean gamePaused, float paddleX, float paddleY) {
				ClientToServerMessage message = new ClientToServerMessage(gameActive, gamePaused, paddleX, paddleY);
				messageHolder.deposit(message.toString());
		}

		@Override
		public void setServerToClientData(boolean gameActive, boolean gamePaused, boolean paddleCollision,
										  boolean wallCollision, float paddleX, float paddleY,
										  float ballX, float ballY, float ballXVelocity,
										  float ballYVelocity, float ballVelocity,
										  double screenWidth, double screenHeight, int hpUp, int hpDown, GameState.GameOverEvent gameOverEvent) {

		}

		@Override
		public String getData() {
//        if(serverMessage == null){
//            return
//        }
				return null;
		}

		@Override
		public ClientToServerMessage getClientData() {
				return null;
		}

		@Override
		public ServerToClientMessage getServerData() {
				return serverToClientMessage;
		}

		@Override
		public float getOpponentPaddleX() {
				return serverToClientMessage.getPaddleX();
		}

		@Override
		public boolean isConnectionOpen() {
				return IS_CONNECTION_OPEN;
		}

		@Override
		public boolean isClientUpdated() {
				boolean tmp = isUpdated;
				isUpdated = false;
				return tmp;
		}

		@Override
		public boolean isServerUpdated() {
				return false;
		}

		@Override
		public float getOpponentPaddleY() {
				return serverToClientMessage.getPaddleY()
						;
		}
}
