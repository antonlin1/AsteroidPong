package com.mygdx.game.Network.client;

import com.mygdx.game.Network.InetUtil;
import com.mygdx.game.Network.MessageHolder;
import com.mygdx.game.Network.WifiDirectBroadcastReceiver;
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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by hampusballdin on 2016-04-20.
 */
public class UDPClient extends Thread implements NetworkComponentInterface {

		private DatagramSocket socket;
		private InetAddress address;
		private Connection connection;
		private MessageHolder messageHolder;
		private ServerToClientMessage serverToClientMessage = new ServerToClientMessage();
		private static boolean IS_CONNECTION_OPEN = false;
		private WifiDirectBroadcastReceiver wifiDirectBroadcastReceiver;
		private static boolean isUpdated = false;

		public UDPClient(InetAddress address, MessageHolder messageHolder, WifiDirectBroadcastReceiver wifiDirectBroadcastReceiver) {
				this.address = address;
				this.messageHolder = messageHolder;
				try {
						socket = new DatagramSocket(InetUtil.PORT);
				} catch (SocketException e) {
						e.printStackTrace();
				}
				this.wifiDirectBroadcastReceiver = wifiDirectBroadcastReceiver;

		}

		public void connect(InetAddress server) {
				connection = new Connection(this, socket, server);
				IS_CONNECTION_OPEN = true;
		}

		// Write from this thread, read from new Thread ...
		@Override
		public void run() {
				System.out.println("Client Start");

				connect(address);

				if (connection != null)
						connection.readStart();
				else
						IS_CONNECTION_OPEN = false;

				// Write Forever ...
				try {
						while (IS_CONNECTION_OPEN && !isInterrupted()) {
								connection.write(messageHolder.withdraw());
						}
				} catch (IOException e) {
						e.printStackTrace();
				} finally {
						socket.close();
				}
				wifiDirectBroadcastReceiver.reconnectToServer();

		}

		private static class Connection {

				private UDPClient.Reader reader;
				private UDPClient.Writer writer;
				private boolean IS_READ_START = false;

				public Connection(UDPClient client, DatagramSocket socket, InetAddress server) {
					//	reader = new UDPClient.Reader(new ReaderCallback(), client, socket);
						reader = new UDPClient.Reader(socket, client);
						writer = new UDPClient.Writer(socket, server);
				}

				/**
				 * Starts reader thread, reads forever ...
				 */
				public void readStart() {
						if (!IS_READ_START) {
								reader.start();
								IS_READ_START = true;
						}
				}

				public void write(String line) throws IOException {
						writer.writeLine(line);
				}
		}

		private static class Reader extends Thread {
				private UDPClient client;
			//	private ReaderCallback readerCallback;
				private boolean isClosed = false;
				private DatagramSocket socket;
				private DatagramPacket dp;
				private byte[] data = new byte[InetUtil.UDP_RECEIVE_BUFFER_SIZE];
				private ServerToClientMessage serverToClientMessage = new ServerToClientMessage();

				public Reader(DatagramSocket socket, UDPClient client) {
						//this.readerCallback = readerCallback;
						this.client = client;
						this.socket = socket;
						dp = new DatagramPacket(data, data.length);


				}

				@Override
				public void run() {
						try {
								while (true && !isInterrupted()) {
										if (!isClosed) {
												//String line = br.readLine();
												socket.receive(dp);
												String line = new String(dp.getData(), 0, dp.getLength());
												dp.setLength(data.length);
												serverToClientMessage.setMessage(line);
												client.serverToClientMessage = serverToClientMessage;
												isUpdated = true;
												//readerCallback.onLineRead(line);
										}
								}
						} catch (IOException e) {
								e.printStackTrace();
								IS_CONNECTION_OPEN = false;
						}
				}
		}

		/*private static class ReaderCallback {

				public void onLineRead(String line) throws IOException {
			//			System.out.println("Client Read Line: " + line);
						isUpdated = true;
						if (line == null) {
								throw new IOException("Socket probably closed");
						}
				}
		}*/

		private static class Writer {
				private boolean isClosed = false;
				private DatagramSocket socket;
				private InetAddress server;
				private DatagramPacket dp;
				private byte[] bytes = new byte[1];


				public Writer(DatagramSocket socket, InetAddress server) {
						this.socket = socket;
						this.server = server;
						dp = new DatagramPacket(bytes, bytes.length, server, InetUtil.PORT);
				}

				public void writeLine(String line) throws IOException {
						if (!isClosed) {
					//			System.out.println("Client Write: " + line);
								bytes = line.getBytes();
								dp.setData(bytes);
								dp.setLength(bytes.length);
								socket.send(dp);
						} else {
								System.err.println("Is Closed!");
						}
				}
		}

		@Override
		public void setClientToServerData(float paddleX, float paddleY) {
				ClientToServerMessage message = new ClientToServerMessage(paddleX, paddleY);
				messageHolder.deposit(message.toString());
		}

		@Override
		public void setServerToClientData(boolean gameActive, boolean paddleCollision,
										  boolean wallCollision, float paddleX, float paddleY,
										  float ballX, float ballY, float ballXVelocity,
										  float ballYVelocity, float ballVelocity,
										  double screenWidth, double screenHeight) {

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
