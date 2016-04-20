package com.mygdx.game.Network;

import android.content.BroadcastReceiver;
import android.net.wifi.p2p.WifiP2pManager;

import com.mygdx.game.PeerHelperInterface;

/**
 * Created by antonlin on 16-04-20.
 */
public class PeerHelper implements PeerHelperInterface {

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver;

    public PeerHelper(WifiP2pManager manager, WifiP2pManager.Channel channel, BroadcastReceiver receiver) {
        this.manager = manager;
        this.channel = channel;
        this.receiver = receiver;
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
}
