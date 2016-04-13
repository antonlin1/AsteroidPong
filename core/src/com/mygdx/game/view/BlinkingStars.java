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
    private Random rand;
    private int randNumber;
    private long time;


    public BlinkingStars(float width, float height) {
        stars = new ArrayList<Star>();
        this.width = width;
        this.height = height;
        rand = new Random();
        randNumber = 0;
    }

    public void makeBlinkingStars() {
        for(int i = 0; i < 30; i++) {

            float x = width * (float) Math.random();
            float y = height * (float) Math.random();

            stars.add(new Star(x,y, TimeUtils.millis()));
            time = TimeUtils.millis();


        }
    }

    // three stars are renderd at different times
    public void drawBlinkingStars(ShapeRenderer shapeRenderer) {
        float size1 = 7;
        float size2 = 5;

        for(int i = 6; i < stars.size(); i++) {
            shapeRenderer.rect(stars.get(i).getxPos(), stars.get(i).getyPos(), size1, size1);

        }

        if(TimeUtils.timeSinceMillis(time) > 10000) {
            time = TimeUtils.millis();
        }
        if(TimeUtils.timeSinceMillis(time) > 0){
                shapeRenderer.rect(stars.get(0).getxPos(), stars.get(0).getyPos(), size2, size2);
        }
        if(TimeUtils.timeSinceMillis(time) > 1000) {
            shapeRenderer.rect(stars.get(1).getxPos(), stars.get(1).getyPos(), size2, size2);
        }
        if(TimeUtils.timeSinceMillis(time) > 2000) {
            shapeRenderer.rect(stars.get(2).getxPos(), stars.get(2).getyPos(), size2, size2);
        }
        if(TimeUtils.timeSinceMillis(time) > 3000) {
            shapeRenderer.rect(stars.get(3).getxPos(), stars.get(3).getyPos(), size2, size2);
        }
        if(TimeUtils.timeSinceMillis(time) > 4000) {
            shapeRenderer.rect(stars.get(4).getxPos(), stars.get(4).getyPos(), size2, size2);
        }
        if(TimeUtils.timeSinceMillis(time) > 5000) {
            shapeRenderer.rect(stars.get(5).getxPos(), stars.get(5).getyPos(), size2, size2);
        }

    }




}
