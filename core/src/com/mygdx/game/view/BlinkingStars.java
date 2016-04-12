package com.mygdx.game.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.model.GameState;
import com.mygdx.game.model.Star;

import java.util.ArrayList;

/**
 * Created by johanmansson on 16-04-12.
 */
public class BlinkingStars {

    private ArrayList<Star> stars;
    private float width, height;
    private ShapeRenderer shapeRenderer;


    public BlinkingStars(float width, float height) {
        stars = new ArrayList<Star>();
        this.width = width;
        this.height = height;

    }

    public void makeBlinkingStars() {
        for(int i = 0; i < 20; i++) {

            float x = width * (float) Math.random();
            float y = height * (float) Math.random();

            stars.add(new Star(x,y));

        }
    }

    public void drawBlinkingStars(ShapeRenderer shapeRenderer) {
        float size1 = 10;
        float size2 = 5;
        shapeRenderer.setColor(Color.WHITE);

        for(int i = 0; i < stars.size(); i++) {

            shapeRenderer.rect(stars.get(i).getxPos(), stars.get(i).getyPos(), size1, size1);



//            if(TimeUtils.timeSinceMillis(stars.get(i).getTimeStamp()) > 250) {
//                shapeRenderer.rect(stars.get(i).getxPos(), stars.get(i).getyPos(), size1, size1);
//            } else if (TimeUtils.timeSinceMillis(stars.get(i).getTimeStamp()) <= 250) {
//                shapeRenderer.rect(stars.get(i).getxPos(), stars.get(i).getyPos(), size2, size2);
//            }
//            stars.get(i).updateTimeStamp();
        }


    }


}
