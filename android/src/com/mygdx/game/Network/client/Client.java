package com.mygdx.game.Network.client;

import com.mygdx.game.Network.InetUtil;
import com.mygdx.game.Network.MessageHolder;
import com.mygdx.game.Network.WifiDirectBroadcastReceiver;
import com.mygdx.game.NetworkInterface;
import com.mygdx.game.model.ClientToServerMessage;
import com.mygdx.game.model.ServerToClientMessage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by hampusballdin on 2016-04-20.
 */
public class Client extends Thread implements NetworkInterface{

    private Socket socket;
    private InetAddress address;
    private Connection connection;
    private MessageHolder messageHolder;
    private ServerToClientMessage serverToClientMessage = new ServerToClientMessage();

    private static boolean IS_CONNECTION_OPEN = false;

    private WifiDirectBroadcastReceiver wifiDirectBroadcastReceiver;

    private static final int MAX_NUMBER_CONNECTION_RETRY = 10;
    private int nbrConnectionRetry = 0;

    public Client(InetAddress address, MessageHolder messageHolder, WifiDirectBroadcastReceiver wifiDirectBroadcastReceiver) {
        this.address = address;
        this.messageHolder = messageHolder;
        socket = new Socket();
        this.wifiDirectBroadcastReceiver = wifiDirectBroadcastReceiver;

    }

    public void connect(InetAddress server) {
        nbrConnectionRetry++;

        if(nbrConnectionRetry > MAX_NUMBER_CONNECTION_RETRY) {
            IS_CONNECTION_OPEN = false;
            return;
        }
        try {
            socket.connect(new InetSocketAddress(address, InetUtil.PORT));
        }catch(IOException e) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.out.println(e);
            System.out.println("Attempt Socket Connection Again ... ");
            connect(server);
        }

        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            connection = new Connection(in, out, this);
            IS_CONNECTION_OPEN = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Write from this thread, read from new Thread ...
    @Override
    public void run() {
        System.out.println("Client Start");

        connect(address);

        if(connection != null)
            connection.readStart();
        else
            IS_CONNECTION_OPEN = false;

        // Write Forever ...
        try {
            while (IS_CONNECTION_OPEN && !isInterrupted() ) {
                String msg = messageHolder.withdraw(); // Blocks until message
                connection.write(msg);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            connection.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        wifiDirectBroadcastReceiver.reconnectToServer();

    }

    private static class Connection {

        private Client.Reader reader;
        private Client.Writer writer;
        private boolean IS_READ_START = false;
        public Connection(InputStream in, OutputStream out, Client client) {
            reader = new Client.Reader(in, new ReaderCallback(), client);
            writer = new Client.Writer(out);
        }

        /**
         * Starts reader thread, reads forever ...
         */
        public void readStart() {
            if(!IS_READ_START) {
                reader.start();
                IS_READ_START = true;
            }
        }

        public void write(String line) throws IOException{
            writer.writeLine(line);
        }

        public void close() {
            System.out.println("Closing Connection ... ");
            reader.close();
            writer.close();
        }

    }
    private static class Reader extends Thread {

        private InputStream in;

        private Client client;
        private ReaderCallback readerCallback;
        private boolean isClosed = false;
        public Reader(InputStream in, ReaderCallback readerCallback, Client client) {
            this.in = in;
            this.readerCallback = readerCallback;
            this.client = client;
        }

        @Override
        public void run() {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            try {
                while(true && !isInterrupted()) {
                    if(!isClosed) {
                        String line = br.readLine();
                        System.out.println("Client Received from server: " + line);

                        client.serverToClientMessage = new ServerToClientMessage(line);

                        readerCallback.onLineRead(line);
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
                IS_CONNECTION_OPEN = false;
            }
        }
        public void close() {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isClosed = true;
            }
        }

    }
    private static class ReaderCallback {

        public void onLineRead(String line) throws IOException {
            System.out.println("Client Read Line: " + line);
            if(line == null) {
                throw new IOException("Socket probably closed");
            }
        }
    }
    private static class Writer {

        private BufferedWriter bw;
        private boolean isClosed = false;
        public Writer(OutputStream out){
            this.bw = new BufferedWriter(new OutputStreamWriter(out));
        }

        public void writeLine(String line) throws IOException{
            if(!isClosed) {
                System.out.println("Client Write: " + line);
                bw.write(line + "\n"); // Note linefeed ...
                bw.flush();
            }else {
                System.err.println("Is Closed!");
            }
        }

        public void close() {
            if(bw != null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isClosed = true;
            }
        }

    }

    @Override
    public void setClientToServerData(float paddleX) {
        ClientToServerMessage message = new ClientToServerMessage(paddleX);
        messageHolder.deposit(message.toString());
    }

    @Override
    public void setServerToClientData(boolean gameActive, boolean paddleCollision, boolean wallCollision, float paddleX,
                                      float ballX, float ballY, float ballXVelocity, float ballYVelocity, float ballVelocity) {
        //DO NOTHING
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
}
