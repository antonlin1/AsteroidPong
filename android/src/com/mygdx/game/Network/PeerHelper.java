package com.mygdx.game.Network;

import android.content.BroadcastReceiver;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

import com.mygdx.game.PeerHelperInterface;

/**
 * Created by antonlin on 16-04-20.
 */
public class PeerHelper implements PeerHelperInterface {

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;

    public PeerHelper(WifiP2pManager manager, WifiP2pManager.Channel channel) {
        this.manager = manager;
        this.channel = channel;
    }

    public void discover() {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("Found peers");
            }

            @Override
            public void onFailure(int reasonCode) {
                System.out.println("NO peers");
            }
        });
    }

    public void connect(WifiP2pDevice device) {
        //obtain a peer from the WifiP2pDeviceList
        //WifiP2pDevice device = receiver.getPeers().get(0);
        if(device != null){
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;
            manager.connect(channel, config, new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    System.out.println("PeerHelper.connect: success!");
                    stopDiscovery();
                }

                @Override
                public void onFailure(int reason) {
                    //failure logic
                    System.out.println("PeerHelper.connect: failure!");
                }
            });
        }
    }

    public void stopDiscovery() {
        manager.stopPeerDiscovery(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("stopPeerDiscovery onSuccess");
            }

            @Override
            public void onFailure(int reason) {
                System.out.println("stopPeerDiscovery onFailure");

            }
        }) ;
    }
}
