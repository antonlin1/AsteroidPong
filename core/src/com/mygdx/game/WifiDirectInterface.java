package com.mygdx.game;

import java.util.List;

/**
 * Created by antonlin on 16-04-26.
 */
public interface WifiDirectInterface {

    NetworkComponentInterface getNetworkComponent();

    boolean isServer();

    boolean isConnected();

    void setDeviceName(String name);

    List<String> getPeerNames();

    void connectToDevice(String name);
}
