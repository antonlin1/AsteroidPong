package com.mygdx.game;

/**
 * Created by antonlin on 16-04-26.
 */
public interface WifiDirectInterface {

    public NetworkComponentInterface getNetworkComponent();

    public boolean isServer();

    public boolean isConnected();

}
