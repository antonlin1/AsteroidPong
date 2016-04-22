package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.AccelerometerInputInterface;
import com.mygdx.game.Controller.InputController;
import com.mygdx.game.view.MyGdxGame;
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

    private InputController input;

    private Sound collisionSound;
    private Sound gameOverSound;

    private Texture cancel;

    private Texture[] scores;
    private int score;


    public GameState(StateManager stateManager, float width, float height) {
        super(stateManager);

        this.width = width;
        this.height = height;

        balls = new ArrayList<Ball>();
        balls.add(new Ball(width / 2, height / 2, 32));


        paddle1 = new Paddle(width / 2 - 32, height - PaddleConstant.YPOS.value - 100, (float) PaddleConstant.LENGTH.value);
        //  paddle2 = new Paddle(width / 2 - 32, PaddleConstant.YPOS.value -20, (float)PaddleConstant.LENGTH.value);

        particles = new Particles();
        collisionSound = Gdx.audio.newSound(Gdx.files.internal("bounce1.wav"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("missedBall.wav"));

        cancel = new Texture("cancel2.png");

        scores = new Texture[6];

        scores[0] = new Texture("score0.png");
        scores[1] = new Texture("score20.png");
        scores[2] = new Texture("score40.png");
        scores[3] = new Texture("score60.png");
        scores[4] = new Texture("score80.png");
        scores[5] = new Texture("score100.png");

        score = 5;

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

        handleInput();

    }

    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {


        Ball ball = getBalls().get(0);
        particles.makeParticles(ball);
        particles.removeParticles();


        if (isDead()) {
            gameOverSound.play();
            killBall();
            randomizePos();
            if(score > 0) {
                score--;
            }
        }

        if(score == 0) {
            score = 5;
        }


        Paddle paddle1 = getPaddles()[0];
        //Paddle paddle2 = state.getPaddles()[1];

        if (isPaddleCollision())  {
            collisionSound.play();
            Gdx.input.vibrate(50);
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

        spriteBatch.begin();
        spriteBatch.draw(cancel, Gdx.graphics.getWidth() - cancel.getWidth() - 20, 20);
        spriteBatch.draw(scores[score], 20, height - PaddleConstant.YPOS.value - 175, 170, 75);
        spriteBatch.draw(scores[5], 20, PaddleConstant.YPOS.value + 100, 170, 75);

        spriteBatch.end();


    }

    public void handleInput() {

        if(Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();

            float x1 = Gdx.graphics.getWidth() - cancel.getWidth() - 20;
            float x2 = Gdx.graphics.getWidth() + cancel.getWidth() - 20;
            float y1 = 20;
            float y2 = cancel.getHeight() + 20;

            if(x > x1 && x < x2 && y > y1 && y < y2) {
                System.out.println("Button pressed");
                stateManager.push(new MenuState(stateManager));


            }

        }

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
