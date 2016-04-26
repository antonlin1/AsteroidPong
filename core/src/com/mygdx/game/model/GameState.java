package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.AccelerometerInputInterface;
import com.mygdx.game.Controller.InputController;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.view.MyGdxGame;
import com.mygdx.game.view.Particles;

import java.util.ArrayList;


public class GameState extends State {

    private Paddle paddle1;
    private Paddle paddle2;
    private ArrayList<Ball> balls;

    protected static int roundsPlayed = 0;
    protected static int bounces = 0;
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

    private PeerHelperInterface peerHelper;
    private WifiDirectInterface wifiDirect;

    /**
     * Created by antonlin on 16-04-11.
     */

    public enum PaddleConstant {
        HEIGHT(32), LENGTH(64 * 3), YPOS(128);

        public int value;

        private PaddleConstant(int value) {
            this.value = value;
        }
    }

    public GameState(StateManager stateManager, float width, float height,
                     PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect) {
        super(stateManager, StateManager.STATES.GAME_STATE);

        this.width = width;
        this.height = height;

        this.peerHelper = peerHelper;
        this.wifiDirect = wifiDirect;

        balls = new ArrayList<Ball>();
        balls.add(new Ball(width / 2, height / 2, 32));


        paddle1 = new Paddle(width / 2 - 32, height - PaddleConstant.YPOS.value - 100, (float) PaddleConstant.LENGTH.value);
        paddle2 = new Paddle(width / 2 - 32, PaddleConstant.YPOS.value -20, (float)PaddleConstant.LENGTH.value);

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

        if(wifiDirect.isServer()){
            System.out.println("SERVER UUUUUPDATE");
            wallCollision = PhysicsHelper.wallCollision(width, height, balls);
            paddleCollision = PhysicsHelper.paddleCollision(getPaddles(), balls);
            isDead = PhysicsHelper.isDead(width, height, balls);
            setPaddle2(wifiDirect.getNetworkComponent().getOpponentPaddleX());

            ball.move();

            handleInput();

//            wifiDirect.getNetworkComponent().setServerToClientData(this.isActive(), paddleCollision, wallCollision,
//                    paddle1.getX(), ball.getX(), ball.getY(), ball.getXVel(), ball.getYVel(), ball.getVelocity());

            wifiDirect.getNetworkComponent().setServerToClientData(this.isActive(), paddleCollision, wallCollision,
                    paddle1.getX(), ball.getX(), ball.getY(), 0, 0, 0);
        } else {
            System.out.println("CLIENT UUUUUPDATE");
            ServerToClientMessage state = wifiDirect.getNetworkComponent().getServerData();

            isDead = false;
            wallCollision = state.isWallCollision();
            paddleCollision = state.isPaddleCollision();
            isDead = PhysicsHelper.isDead(width, height, balls);
            setPaddle2(wifiDirect.getNetworkComponent().getOpponentPaddleX());

            ball.setX(this.width - state.getBallX());
            ball.setY(this.height - state.getBallY());

            setPaddle2(state.getPaddleX());

            handleInput();

            wifiDirect.getNetworkComponent().setClientToServerData(paddle1.getX());
        }
    }

//    private void setFullState(boolean gameActive, boolean paddleCollision, boolean wallCollision, float paddleX, float ballX, float ballY,
//                              float ballXVelocity, float ballYVelocity, float ballVelocity) {
//
//        Ball ball = balls.get(0);
//
//        this.paddleCollision = paddleCollision;
//        this.wallCollision = wallCollision;
//
//        ball.setX(ballX);
//        ball.setY(ballY);
//
////        wallCollision = PhysicsHelper.wallCollision(width, height, balls);
////        paddleCollision = PhysicsHelper.paddleCollision(getPaddles(), balls);
//        isDead = PhysicsHelper.isDead(width, height, balls);
//        setPaddle2(paddleX);
//
//
//        //ball.move();
//    }

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
        Paddle paddle2 = getPaddles()[1];

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

        shapeRenderer.rect(paddle2.getX(), paddle2.getY(), paddle2.getLength(), paddle2.getHeight());

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

    // OpponentPadle, will mirror xPos
    public void setPaddle2(float xPos) {
        paddle2.setX(this.width - xPos);
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

    @Override
    public boolean isActive(){
        return (this.stateManager.getActiveState().equals(this.stateName));
    }

    //	public boolean newBall() {
    //			return PhysicsHelper.isDead(width, height, balls);
    //	}


}
