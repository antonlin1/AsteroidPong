package com.mygdx.game.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;

import com.mygdx.game.Network.client.UDPClient;
import com.mygdx.game.Network.server.UDPServer;
import com.mygdx.game.NetworkComponentInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.view.MyGdxGame;
import com.mygdx.game.view.States.FindGameState;
import com.mygdx.game.view.States.StateManager;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by antonlin on 16-04-20.
 */
public class WifiDirectBroadcastReceiver extends BroadcastReceiver implements WifiDirectInterface {

	public static final String DATE_FORMAT = "yyyy.MM.dd.HH.mm.ss.SSS";
	private static final String GROUP_NAME = "AP";
	private static final MessageHolder messageHolder = MessageHolder.getInstance();
	//private ViewModifier viewModifier;
	private static PeerHelper peerHelper;
	private Map<String, WifiP2pDevice> peerMap = new HashMap<>();

	private WifiP2pManager mManager;

	private WifiP2pManager.Channel mChannel;
	private WifiP2pManager.PeerListListener myPeerListListener;
	private DirectConnectionInfoListener directConnectionInfoListener;
	private String thisDeviceName;

	private MyGdxGame game;

	// Client or server
	private NetworkComponentInterface networkComponent;
	public WifiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, final PeerHelper peerHelper) {
		super();
		this.mManager = manager;
		this.mChannel = channel;
		this.peerHelper = peerHelper;

		this.myPeerListListener = new WifiP2pManager.PeerListListener() {
			@Override
			public void onPeersAvailable(WifiP2pDeviceList peerList) {
				System.out.println("WifiDirectBroadcastReceiver onPeersAvailable");
				// Out with the old, in with the new.
				for (WifiP2pDevice device : peerList.getDeviceList()) {
					if (device.deviceName.startsWith(GROUP_NAME)) {
						peerMap.put(device.deviceName.toString().toUpperCase(), device);
						if(game != null && game.getStateManager().getActiveStateName() == StateManager.STATE_NAME.FIND_GAME_STATE){
							((FindGameState) game.getStateManager().getActiveState()).setupPeerNames(peerMap.keySet());
						}
					}
				}
			}
		};
		directConnectionInfoListener = new DirectConnectionInfoListener(this);
//			Runtime.getRuntime().addShutdownHook(new Thread() {
//				public void run() {
//					System.out.println("SHUTDOWN HOOK INITIATED");
//					NetworkState.NETWORK_SHUTDOWN = true;
//				}
//			});
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		System.out.println("WifiDirectBroadcastReceiver.onReceive action is: " + action);

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				// Wifi P2P is enabled
				System.out.println("WifiDirectBroadcastReceiver.onReceive: Wifi P2P is enabled");
			} else {
				// Wi-Fi P2P is not enabled
				System.out.println("WifiDirectBroadcastReceiver.onReceive: Wifi P2P NOOOT enabled");
			}

		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			System.out.println("WifiDirectBroadcastReceiver.onReceive: WIFI_P2P_PEERS_CHANGED_ACTION");
			if (mManager != null) {
				if (!NetworkState.IS_CONNECTED) {
					mManager.requestPeers(mChannel, myPeerListListener);
				}
			}
		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			// Indicates the state of Wi-Fi P2P connectivity has changed.
			System.out.println("WifiDirectBroadcastReceiver.onReceive: WIFI_P2P_CONNECTION_CHANGED_ACTION");
			onConnectionChange(intent);


		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			System.out.println("WifiDirectBroadcastReceiver.onReceive: WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");

			WifiP2pDevice thisDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
			thisDeviceName = thisDevice.deviceName;
			System.out.println("THIS DEVICE NAME: " + thisDeviceName);
			String adr = thisDevice.deviceAddress;
			thisDevice.isGroupOwner();
		}
	}

	public void onConnectionChange(Intent intent) {


		NetworkInfo networkInfo = (NetworkInfo) intent
				.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

		if (networkInfo.isConnected()) {

			// We are connected with the other device, request connection
			// info to find group owner IP

			System.out.println("We are connected, requesting info.");
			mManager.requestConnectionInfo(mChannel, directConnectionInfoListener);
			///	mManager.requestGroupInfo();
			NetworkState.IS_CONNECTED = true;

			//NetworkState.IS_CONNECTING = false;

		} else {
			System.out.println("We are not connected.");
			NetworkState.IS_CONNECTED = false;
			System.out.println("Reason: " + networkInfo.getReason());
			System.out.println("Detailed: " + networkInfo.getDetailedState());
			System.out.println("Extra: " + networkInfo.getExtraInfo());
			System.out.println("networkInfo.isConnectedOrConnecting(): " + networkInfo.isConnectedOrConnecting());

			if (networkInfo.getReason() == null)
				return;
			if (networkInfo.getDetailedState().equals("FAILED")) {
				peerHelper.requestResetState();
			}
		}
	}

//	public List<WifiP2pDevice> getPeers() {

	public Set<String> getPeerNames() {
		return peerMap.keySet();
	}

	@Override
	public void connectToDevice(String deviceName) {
		WifiP2pDevice connectDevice = peerMap.get(deviceName);

		System.out.println("TRYING TO CONNECT!");
		if (!NetworkState.IS_CONNECTING.get() && connectDevice != null) {
			peerHelper.connect(connectDevice);
		}

	}

	/**
	 * If Client Loses Connection to Server ...
	 */
	public void reconnectToServer() {
		mManager.requestConnectionInfo(mChannel, directConnectionInfoListener);
	}

	public void resetNetwork() {
		mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {

			@Override
			public void onSuccess() {
				System.out.println("Succesfully removed group!");
			}

			@Override
			public void onFailure(int reason) {
				System.out.println("NOT succesful in removing group!");
			}
		});
//			for(Thread thread : childThreads){
//				thread.interrupt();
//			}

	}

	public void setDeviceName(String name) {
		String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
		System.out.println("TIMEEEE: " + timeStamp);

		try {
			Method m = mManager.getClass().getMethod("setDeviceName",
					new Class[]{mChannel.getClass(), String.class, WifiP2pManager.ActionListener.class});

			m.invoke(mManager, mChannel, "AP" + ":" + name, new WifiP2pManager.ActionListener() {

				@Override
				public void onSuccess() {
					System.out.println("Change name success");
				}

				@Override
				public void onFailure(int reason) {
					System.out.println("Change name failure, reason: " + reason);
				}

			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public NetworkComponentInterface getNetworkComponent() {
		return networkComponent;
	}

	@Override
	public boolean isClient() {
		return NetworkState.IS_CLIENT.get();
	}

	@Override
	public boolean isConnected() {
		return NetworkState.IS_CONNECTED;
	}

	private static class DirectConnectionInfoListener implements WifiP2pManager.ConnectionInfoListener {

		private WifiDirectBroadcastReceiver wifiDirectBroadcastReceiver;

		public DirectConnectionInfoListener(WifiDirectBroadcastReceiver wifiDirectBroadcastReceiver) {
			this.wifiDirectBroadcastReceiver = wifiDirectBroadcastReceiver;
		}

		@Override
		public void onConnectionInfoAvailable(WifiP2pInfo info) {
			// InetAddress from WifiP2pInfo struct.
			if (info == null)
				return;
			if (info.groupOwnerAddress == null)
				return;
			String host = info.groupOwnerAddress.getHostAddress();


			System.out.println("Group Owner: " + host);


			// After the group negotiation, we can determine the group owner.
			if (info.groupFormed && !NetworkState.IS_CLIENT.get()) {
				// Do whatever tasks are specific to the group owner.
				// One common case is creating a server thread and accepting
				// incoming connections.

				// Do server stuff ...
//				NetworkState.IS_CLIENT.set(false);


				InetAddress address = info.isGroupOwner ? null : info.groupOwnerAddress;
				//Server server = new Server(wifiDirectBroadcastReceiver.messageHolder);
				UDPServer server = new UDPServer(wifiDirectBroadcastReceiver.messageHolder,
						address);
				wifiDirectBroadcastReceiver.networkComponent = server;
				server.start();
				//System.out.println("DirectConnectionInfoListener, I AM GROUPOWNER: " + server);

			} else if (info.groupFormed) {
				// The other device acts as the client. In this case,
				// you'll want to create a client thread that connects to the group
				// owner.
//				NetworkState.IS_CLIENT.set(true);
				//System.out.println("DirectConnectionInfoListener, OTHER IS GROUP OWNER");
				// Do client stuff ...

				// Wait for server to setup ...
				//Client client = new Client(info.groupOwnerAddress, wifiDirectBroadcastReceiver.messageHolder, wifiDirectBroadcastReceiver);

				InetAddress address = info.isGroupOwner ? null : info.groupOwnerAddress;
				UDPClient client = new UDPClient(address,
						wifiDirectBroadcastReceiver.messageHolder, wifiDirectBroadcastReceiver);

				wifiDirectBroadcastReceiver.networkComponent = client;

				client.start();

			} else {
				peerHelper.requestResetState();
			}
			//wifiDirectBroadcastReceiver.viewModifier.visibleMessageField();
		}

	}

	private static class DirectGroupInfoListener implements WifiP2pManager.GroupInfoListener {

		@Override
		public void onGroupInfoAvailable(WifiP2pGroup group) {
			System.out.println("DirectGroupInfoListener, onGroupInfoAvailable");
		}

	}

	public MyGdxGame getGame() {
		return game;
	}

	public void setGame(MyGdxGame game) {
		this.game = game;
	}
}
