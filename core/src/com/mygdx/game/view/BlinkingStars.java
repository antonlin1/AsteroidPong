package com.mygdx.game.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.model.Star;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by johanmansson on 16-04-12.
 */
public class BlinkingStars {

    private ArrayList<Star> stars;
    private float width, height;
    private long time;


    public BlinkingStars(float width, float height) {
        stars = new ArrayList<Star>();
        this.width = width;
        this.height = height;
        time = TimeUtils.millis();
    }

    public void makeBlinkingStars() {
        for(int i = 0; i < 30; i++) {
            stars.add(makeNewStar());
        }
    }

    public Star makeNewStar() {

        float x = width * (float) Math.random();
        float y = height * (float) Math.random();
        float size1 = 7;
        float size2 = 5;

        if(Math.random()  <= 0.3) {
            return new Star(x, y, size1);
        }

        return new Star(x, y, size2);


    }

    public void drawBlinkingStars(ShapeRenderer shapeRenderer) {

        for(int i = 0; i < stars.size(); i++) {
            shapeRenderer.rect(stars.get(i).getxPos(), stars.get(i).getyPos(), stars.get(i).getSize(), stars.get(i).getSize());
        }

        if(TimeUtils.timeSinceMillis(time) > 2000) {
            stars.add(makeNewStar());
            stars.remove(0);
            time = TimeUtils.millis();
        }


    }







}
