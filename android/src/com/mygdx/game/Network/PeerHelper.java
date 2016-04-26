package com.mygdx.game.Network;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;

import com.mygdx.game.PeerHelperInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by antonlin on 16-04-20.
 */
public class PeerHelper implements PeerHelperInterface {

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;

    private DiscoverThread discoverThread;
    private ConnectThread connectThread;


    /**
     * The actual role of this side of the peer
     */
    private String role = "";


    /**
     * Does not attempt to connect
     */
    public static final String ROLE_PASSIVE = "PASSIVE";

    /**
     * Does attempt to connect
     */
    public static final String ROLE_ACTIVE = "ACTIVE";

    public PeerHelper(WifiP2pManager manager, WifiP2pManager.Channel channel) {
        this.manager = manager;
        this.channel = channel;
    }

    /**
     * Called when all states reset successfully ...
     */
    private void onResetSuccess() {
        System.out.println("onResetSuccess");
        NetworkState.IS_GROUPING = false;
        discover();
//		if (role.equals(ROLE_ACTIVE)) {
//			PeerHelper.this.discoverActive();
//		} else {
//			PeerHelper.this.discoverPassive();
//		}
    }


    public void requestResetState() {

        System.out.println("requestResetState");

        if (NetworkState.IS_CONNECTING.get()) {
            manager.cancelConnect(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    NetworkState.IS_CONNECTING.set(false);
                    NetworkState.IS_CONNECTING_RESET.set(true);
                    if (NetworkState.IS_DISCOVERING_RESET.get()) {
                        onResetSuccess();
                    }
                }

                @Override
                public void onFailure(int reason) {
                    NetworkState.IS_CONNECTING.set(false);
                    System.err.println("CONNECTING_RESET FAILED");
                }
            });
        }

        if (NetworkState.IS_DISCOVERING) {
            manager.stopPeerDiscovery(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    NetworkState.IS_DISCOVERING = false;
                    NetworkState.IS_DISCOVERING_RESET.set(true);
                    if (NetworkState.IS_CONNECTING_RESET.get()) {
                        onResetSuccess();
                    }
                }

                @Override
                public void onFailure(int reason) {
                    NetworkState.IS_DISCOVERING = false;
                    System.err.println("DISCOVERING_RESET FAILED");
                }
            });
        }

        if (!NetworkState.IS_DISCOVERING && !NetworkState.IS_CONNECTING.get()) {
            onResetSuccess();
        }
    }


//	public void discoverActive() {
//		this.role = ROLE_ACTIVE;
//		if (!NetworkState.IS_DISCOVERING && !NetworkState.IS_GROUPING) {
//			discoverThread = new DiscoverThread();
//			discoverThread.start();
//		}
//	}

    public void discover() {
        if (!NetworkState.IS_DISCOVERING && !NetworkState.IS_GROUPING) {
            discoverThread = new DiscoverThread();
            discoverThread.start();
        }
    }

//	public void discoverPassive() {
//		this.role = ROLE_PASSIVE;
//		if (!NetworkState.IS_DISCOVERING && !NetworkState.IS_GROUPING) {
//			discoverThread = new DiscoverThread();
//			discoverThread.start();
//		}
//	}

    public void connect(final WifiP2pDevice device) {
        if (!NetworkState.IS_CONNECTING.get() && !NetworkState.IS_GROUPING && role.equals(ROLE_ACTIVE)) {
            connectThread = new ConnectThread(device);
            connectThread.start();
        }
    }

    private class ConnectThread extends Thread {

        private final WifiP2pDevice device;
        private static final int CONNECT_TIMEOUT = 1000;

        public ConnectThread(final WifiP2pDevice device) {
            this.device = device;
        }

        @Override
        public void run() {
            connect(device);
        }

        public void connect(final WifiP2pDevice device) {
            //obtain a peer from the WifiP2pDeviceList
            //WifiP2pDevice device = receiver.getPeers().get(0);

            if (device != null) {
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;

                System.out.println("ConnectThread, CONNECT TO " + device.deviceName + " ... ");
                NetworkState.IS_CONNECTING.set(true);
                manager.connect(channel, config, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        System.out.println("Initiating Connecting to peer Successfully");
                        NetworkState.IS_CONNECTING.set(false);
                        NetworkState.IS_GROUPING = true;
                    }

                    @Override
                    public void onFailure(int reason) {
                        //failure logic
                        System.err.print("Connection of peers failed because ");
                        NetworkState.IS_CONNECTING.set(false);
                        NetworkState.IS_GROUPING = false;
                        switch (reason) {
                            case WifiP2pManager.BUSY:
                                System.err.println("the framework is busy and unable to service the request");
                                try {
                                    Thread.sleep(CONNECT_TIMEOUT);
                                } catch (InterruptedException e) {

                                }
                                PeerHelper.this.connect(device);
                                break;
                            case WifiP2pManager.ERROR:
                                System.err.println("an internal error occurred");
                                break;
                            case WifiP2pManager.P2P_UNSUPPORTED:
                                System.err.println("p2p is unsupported on the device");
                                break;
                        }

                    }
                });
            } else {
                System.out.println("Device is NULL ... ");
            }
        }
    }

    private class DiscoverThread extends Thread {
        private static final int DISCOVER_TIMEOUT = 1000;

        @Override
        public void run() {
            discover();
        }

        public void discover() {
            NetworkState.IS_DISCOVERING = true;
            manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    System.out.println("DiscoverThread, Discover");
                    NetworkState.IS_DISCOVERING = false;
                }

                @Override
                public void onFailure(int reasonCode) {
                    System.err.print("Discovery of peers failed because ");
                    NetworkState.IS_DISCOVERING = false;
                    switch (reasonCode) {
                        case WifiP2pManager.BUSY:
                            System.err.println("the framework is busy and unable to service the request");
                            try {
                                Thread.sleep(DISCOVER_TIMEOUT);
                            } catch (InterruptedException e) {

                            }
//							// Attempt to discover again ...
//							if (role.equals(ROLE_ACTIVE)) {
//								PeerHelper.this.discoverActive();
//							} else {
//								PeerHelper.this.discoverPassive();
//							}
                            discover();
                            break;
                        case WifiP2pManager.ERROR:
                            System.err.println("an internal error occurred");
                            break;
                        case WifiP2pManager.P2P_UNSUPPORTED:
                            System.err.println("p2p is unsupported on the device");
                            break;
                    }

                }
            });
        }
    }

    //Will do millisecond comparison between devices. If discovery set on exactly same millisecond => FUCK UP
    public void decideConnectionRole(String thisDeviceName, String peerDeviceName) {

        try {
            String[] thisSubStr = thisDeviceName.split(":");
            String[] peerSubStr = peerDeviceName.split(":");

            System.out.println("thisSubStr[0]: "+thisSubStr[0] + " [1]:"+ thisSubStr[1].substring(1));
            System.out.println("peerSubStr[0]: "+peerSubStr[0] + " [1]:"+ peerSubStr[1].substring(1));


            Date thisDate = new SimpleDateFormat(WifiDirectBroadcastReceiver.DATE_FORMAT).parse(thisSubStr[1]);
            Date peerDate = new SimpleDateFormat(WifiDirectBroadcastReceiver.DATE_FORMAT).parse(peerSubStr[1]);


            this.role = (thisDate.compareTo(peerDate) < 0) ? ROLE_ACTIVE : ROLE_PASSIVE;

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getNetworkRole() {
        return role;
    }

    public void disconnect() {
        if (manager != null && channel != null) {
            manager.requestGroupInfo(channel, new WifiP2pManager.GroupInfoListener() {
                @Override
                public void onGroupInfoAvailable(WifiP2pGroup group) {
                    if (group != null && manager != null && channel != null
                            && group.isGroupOwner()) {
                        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {

                            @Override
                            public void onSuccess() {
                                System.out.println("removeGroup onSuccess");
                            }

                            @Override
                            public void onFailure(int reason) {
                                System.out.println( "removeGroup onFailure: " + reason);
                            }
                        });
                    }
                }
            });
        }
    }
}