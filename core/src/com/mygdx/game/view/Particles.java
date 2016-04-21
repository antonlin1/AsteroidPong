package com.mygdx.game.view;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.model.Ball;
import com.mygdx.game.model.Particle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by johanmansson on 16-04-11.
 */
public class Particles  {

    private ArrayList<Particle> particles;
    public Batch batch;
    public Ball ball;


    public Particles() {
        particles = new ArrayList<Particle>();

    }

    public void makeParticles(Ball ball) {
        for(int i = 0; i < 10; i++) {
            float x = ball.getX() + ((float) Math.random() * ball.getRadius()) -25;
            float y = ball.getY() + ((float) Math.random() * ball.getRadius()) -25;

            particles.add(new Particle(x, y, TimeUtils.millis()));

        }
    }

    public void drawParticles(ShapeRenderer shapeRenderer) {
        float size1 = 10;
        float size2 = 7;
        float size3 = 4;

        for(int i = 0; i < particles.size(); i++) {
            shapeRenderer.setColor(Color.YELLOW);
            if(TimeUtils.timeSinceMillis(particles.get(i).getTimeStamp()) > 300) {
                shapeRenderer.rect(particles.get(i).getX(), particles.get(i).getY(), size3, size3);
            } else if(TimeUtils.timeSinceMillis(particles.get(i).getTimeStamp()) > 150) {
                shapeRenderer.rect(particles.get(i).getX(), particles.get(i).getY(), size2, size2);
            } else {
                shapeRenderer.rect(particles.get(i).getX(), particles.get(i).getY(), size1, size1);

            }
        }

    }

    public void removeParticles() {
        for(int i = 0; i < particles.size(); i++) {
            if(TimeUtils.timeSinceMillis(particles.get(i).getTimeStamp()) > 350) {
                particles.remove(i);
            }
        }
    }











}
