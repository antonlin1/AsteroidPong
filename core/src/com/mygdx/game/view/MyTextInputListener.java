package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;

/**
 * Created by johanmansson on 16-05-16.
 */
public class MyTextInputListener implements Input.TextInputListener {

    public enum Outcome {
        NOT_SET, CANCELLED, NAME_SET;
    }

    private Outcome outcome;
    private String gameName;
    private WifiDirectInterface wifiDirect;
    private PeerHelperInterface peerHelper;
    private boolean isClient;

    public MyTextInputListener(WifiDirectInterface wifiDirect, PeerHelperInterface peerHelper,boolean isClient ) {
        gameName = "";
        outcome = Outcome.NOT_SET;
        this.wifiDirect = wifiDirect;
        this.peerHelper = peerHelper;
        this.isClient = isClient;
    }

    @Override
    public void input(String text) {
        gameName = text.trim();
        if(gameName.length() > 32) {
            Gdx.input.getTextInput(this, "Choose game name(max 32 letters)", "", "");
        } else {
            peerHelper.discover(isClient);
            wifiDirect.setDeviceName(gameName);
        }
    }

    @Override
    public void canceled() {
        outcome = Outcome.CANCELLED;
    }

    public Outcome getOutcome() {
        return  outcome;
    }

    public String getGameName() {
        return gameName;
    }

}
