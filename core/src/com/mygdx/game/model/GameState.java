package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.view.Particles;

import java.util.ArrayList;


/**
 * Created by antonlin on 16-04-11.
 */
public class GameState extends State {

    private Paddle paddle1;
    private Paddle paddle2;
    private ArrayList<Ball> balls;

    protected static int roundsPlayed = 0;
    protected static int bounces = 0;
    //  protected static boolean paddleCollision = false;
    //Size of game window
    private float width, height;

    private boolean wallCollision;
    private boolean isDead;
    private boolean paddleCollision;
    private Particles particles;

    Sound collisionSound;
    Sound gameOverSound;


    public GameState(StateManager stateManager, float width, float height) {
        super(stateManager);

        this.width = width;
        this.height = height;

        balls = new ArrayList<Ball>();
        balls.add(new Ball(width / 2, height / 2, 32));


        paddle1 = new Paddle(width / 2 - 32, height - PaddleConstant.YPOS.value, (float) PaddleConstant.LENGTH.value);
        //  paddle2 = new Paddle(width / 2 - 32, PaddleConstant.YPOS.value -20, (float)PaddleConstant.LENGTH.value);

        particles = new Particles();
        collisionSound = Gdx.audio.newSound(Gdx.files.internal("bounce1.wav"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("missedBall.wav"));


    }


    public void update() {
        Ball ball = balls.get(0);

        wallCollision = false;
        paddleCollision = false;
        isDead = false;
        wallCollision = PhysicsHelper.wallCollision(width, height, balls);
        paddleCollision = PhysicsHelper.paddleCollision(getPaddles(), balls);
        isDead = PhysicsHelper.isDead(width, height, balls);

        //		if(isDead) {
        //					isDead = false;
        //					ball.randomizePos(width, height);
//				}

        ball.move();
    }

    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {

        Ball ball = getBalls().get(0);
        particles.makeParticles(ball);
        particles.removeParticles();

        if (isDead()) {
            gameOverSound.play();
            killBall();
            randomizePos();
        }


        Paddle paddle1 = getPaddles()[0];
        //Paddle paddle2 = state.getPaddles()[1];

        if (isPaddleCollision())  {
            collisionSound.play();
            Gdx.input.vibrate(300);
        }
        if (isWallCollision()) {
            collisionSound.play();
        }


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        particles.drawParticles(shapeRenderer);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(ball.getX(), ball.getY(), ball.getRadius());

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(paddle1.getX(), paddle1.getY(), paddle1.getLength(), paddle1.getHeight());

        //	shapeRenderer.rect(paddle2.getX(), paddle2.getY(), paddle2.getLength(), paddle2.getHeight());

        shapeRenderer.end();




    }

    public enum PaddleConstant {
        HEIGHT(32), LENGTH(64 * 3), YPOS(128);

        public int value;

        private PaddleConstant(int value) {
            this.value = value;
        }
    }

    public boolean isDead() {
        return isDead;
    }

    //stops ball from moving

    public void killBall() {
        balls.get(0).kill();
    }

    //move ball object to random position


    public Paddle[] getPaddles() {
        Paddle[] paddles = {paddle1, paddle2};
        return paddles;
    }


    //So that several balls can be implemented
    public ArrayList<Ball> getBalls() {
        return balls;
    }


    public void randomizePos() {
        Ball ball = balls.get(0);
        ball.randomizePos(width, height);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isWallCollision() {
        return wallCollision;
    }

    public boolean isPaddleCollision() {
        return paddleCollision;
    }

    //	public boolean newBall() {
    //			return PhysicsHelper.isDead(width, height, balls);
    //	}


}
