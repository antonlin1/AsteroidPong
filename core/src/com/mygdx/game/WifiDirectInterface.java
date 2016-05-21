package com.mygdx.game;

import java.util.List;
import java.util.Set;

/**
 * Created by antonlin on 16-04-26.
 */
public interface WifiDirectInterface {

    NetworkComponentInterface getNetworkComponent();

    boolean isClient();

    boolean isConnected();

    void setDeviceName(String name);

    Set<String> getPeerNames();

    void connectToDevice(String name);
}
