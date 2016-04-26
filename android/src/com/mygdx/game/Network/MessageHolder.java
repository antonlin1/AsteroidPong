package com.mygdx.game.Network;
/**
 * Created by hampusballdin on 2016-04-20.
 */
public class MessageHolder {

    String message = null;

    private static MessageHolder instance = null;
    private MessageHolder() {
        // Exists only to defeat instantiation.
    }
    public static MessageHolder getInstance() {
        if(instance == null) {
            instance = new MessageHolder();
        }
        return instance;
    }

    public synchronized void deposit(String message) {
        while(this.message != null){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.message = message;
        notifyAll();
    }



    public synchronized String withdraw() {
        while(this.message == null){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String tmp = message;
        message = null;
        notifyAll();
        return tmp;
    }

}
