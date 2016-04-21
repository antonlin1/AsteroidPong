package com.mygdx.game.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.mygdx.game.AndroidLauncher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by antonlin on 16-04-20.

 */
/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private AndroidLauncher mActivity;
    private WifiP2pManager.PeerListListener myPeerListListener;

    private final List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    private final PeerHelper peerHelper;
    public WifiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       AndroidLauncher activity, final PeerHelper peerHelper, final Context context) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
        this.peerHelper = peerHelper;

        this.myPeerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {
                System.out.println("WifiDirectBroadcastReceiver onPeersAvailable");
                // Out with the old, in with the new.
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                System.out.println("Found: " + peers.size() + " number of peers");
                System.out.println(Arrays.toString(peers.toArray()));


//                ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//                if (mWifi.isConnected()) {
//                    System.out.println("Is Connected");
//                }else {
//                    System.out.println("Not Connected, Atte");
//
//                }

                if (peers.size() == 0) {
                    Log.d("wifi", "No devices found");
                    return;
                } else {
                    System.out.println("Connecting to peer ... ");
//                    if(!mWifi.isConnected()){
                       peerHelper.connect(peers.get(0));
//                    }
                }
            }
        };
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        System.out.println("WifiDirectBroadcastReceiver.onReceive action is: "+action);
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                System.out.println("WifiDirectBroadcastReceiver.onReceive: Wifi P2P is enabled");
            } else {
                // Wi-Fi P2P is not enabled
                System.out.println("WifiDirectBroadcastReceiver.onReceive: Wifi P2P NOOOT enabled");
            }

//            if (mManager != null) {
//                mManager.requestPeers(mChannel, myPeerListListener);
//                System.out.println("WifiDirectBroadcastReceiver.onReceive peerList: "+ myPeerListListener.toString());
//            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            System.out.println("WifiDirectBroadcastReceiver.onReceive: WIFI_P2P_PEERS_CHANGED_ACTION");
            if (mManager != null) {
                mManager.requestPeers(mChannel, myPeerListListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            System.out.println("WifiDirectBroadcastReceiver.onReceive: WIFI_P2P_CONNECTION_CHANGED_ACTION");
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            System.out.println("WifiDirectBroadcastReceiver.onReceive: WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
        }
    }

    public List<WifiP2pDevice> getPeers() {
        return peers;
    }
}
