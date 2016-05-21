package com.mygdx.game.model;

/**
 * Created by antonlin on 2016-05-21.
 */
public class Player {

    private String name;

    private int hp;

    private int originalHp;
    public Player(int hp) {
        this.hp = hp;
        this.originalHp = hp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void decreaseHp() {
        if(hp > 0){
            hp--;
        }
    }

    public boolean isDead() {
        return (hp == 0);
    }

    public void resetPlayer() {
        hp = originalHp;
    }
}
