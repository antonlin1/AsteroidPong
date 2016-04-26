package com.mygdx.game.model;

/**
 * Created by antonlin on 16-04-26.
 */
public class ClientToServerMessage {

    // Client device's x position
    private float paddleX;

    public ClientToServerMessage() {
        paddleX = 0;
    }

    public ClientToServerMessage(float paddleX) {
        this.paddleX = paddleX;
    }

    public ClientToServerMessage(String data) {
        parse(data);
    }

    public float getPaddleX() {
        return paddleX;
    }

    public String toString() {
        return paddleX + "";
    }

    private void parse(String data) {
        paddleX = Float.parseFloat(data);
    }
}
