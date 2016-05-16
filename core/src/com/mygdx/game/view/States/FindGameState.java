package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.view.MyGdxGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johanmansson on 16-05-06.
 */
public class FindGameState extends State {

    private Texture cancel;
    private Texture[] circles;
    private Texture text;
    private Texture button;
    private long time1, time2, time3;
    private Stage stage;


    public FindGameState(MyGdxGame game, StateManager stateManager,
						 WifiDirectInterface wifiDirect, PeerHelperInterface peerHelperInterface) {
       super(game, stateManager, StateManager.STATE_NAME.FIND_GAME_STATE, wifiDirect, peerHelperInterface);
       cancel = new Texture("cancel2.png");

        circles = new Texture[7];
        circles[0] = new Texture("s1.png");
        circles[1] = new Texture("s2.png");
        circles[2] = new Texture("s3.png");
        circles[3] = new Texture("s4.png");
        circles[4] = new Texture("s5.png");
        circles[5] = new Texture("s6.png");
        circles[6] = new Texture("s7.png");

        text = new Texture("selecttext.png");
        button = new Texture("buttonREF.png");

        time1 = TimeUtils.millis();
        time2 = TimeUtils.millis() + 420;

        //Button test
        stage = new Stage(new ScalingViewport(Scaling.stretch,(float) game.getWidth(), (float)game.getHeight()));
        setupCustomInputProcessor();

        //DUMMY LIST
        List<String> goodGames = new ArrayList<String>();
        goodGames.add("Anton");
        goodGames.add("Viktor");
        goodGames.add("Hampus");
        goodGames.add("Johan");

        setupPeerList(goodGames);
    }

    @Override
    public void update() {
        handleInput();
        stage.draw();
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {

        spriteBatch.begin();
        spriteBatch.draw(cancel, Gdx.graphics.getWidth() - cancel.getWidth() - 20, 20);

        spriteBatch.draw(text, (Gdx.graphics.getWidth() / 2) - (text.getWidth() / 2),
				(Gdx.graphics.getHeight() / 2) - text.getHeight() - 500);

        if(TimeUtils.timeSinceMillis(time1) > 0 && TimeUtils.timeSinceMillis(time1) < 100) {
            spriteBatch.draw(circles[0], (Gdx.graphics.getWidth() / 2) - (circles[0].getWidth() / 2),
					(Gdx.graphics.getHeight() / 2) - (circles[0].getHeight() / 2));
        }
        if(TimeUtils.timeSinceMillis(time1) > 100 && TimeUtils.timeSinceMillis(time1) < 230) {
            spriteBatch.draw(circles[1], (Gdx.graphics.getWidth()/2) - (circles[0].getWidth()/2),
					(Gdx.graphics.getHeight()/2) - (circles[0].getHeight()/2));
        }
        if(TimeUtils.timeSinceMillis(time1) > 230 && TimeUtils.timeSinceMillis(time1) < 360) {
            spriteBatch.draw(circles[2], (Gdx.graphics.getWidth()/2) - (circles[0].getWidth()/2),
					(Gdx.graphics.getHeight()/2) - (circles[0].getHeight()/2));
        }
        if(TimeUtils.timeSinceMillis(time1) > 360 && TimeUtils.timeSinceMillis(time1) < 490) {
            spriteBatch.draw(circles[3], (Gdx.graphics.getWidth()/2) - (circles[0].getWidth()/2),
					(Gdx.graphics.getHeight()/2) - (circles[0].getHeight()/2));
        }
        if(TimeUtils.timeSinceMillis(time1) > 490 && TimeUtils.timeSinceMillis(time1) < 530) {
            spriteBatch.draw(circles[4], (Gdx.graphics.getWidth()/2) - (circles[0].getWidth()/2),
					(Gdx.graphics.getHeight()/2) - (circles[0].getHeight()/2));
        }
        if(TimeUtils.timeSinceMillis(time1) > 530 && TimeUtils.timeSinceMillis(time1) < 680) {
            spriteBatch.draw(circles[5], (Gdx.graphics.getWidth()/2) - (circles[0].getWidth()/2),
					(Gdx.graphics.getHeight()/2) - (circles[0].getHeight()/2));
        }
        if(TimeUtils.timeSinceMillis(time1) > 680 && TimeUtils.timeSinceMillis(time1) < 840) {
            spriteBatch.draw(circles[6], (Gdx.graphics.getWidth()/2) - (circles[0].getWidth()/2),
					(Gdx.graphics.getHeight()/2) - (circles[0].getHeight()/2));
        }
        if(TimeUtils.timeSinceMillis(time1) > 840) {
            time1 = TimeUtils.millis();
        }


        Color c = spriteBatch.getColor();

        if(!wifiDirect.isConnected()){
            spriteBatch.setColor(c.r, c.g, c.b, 0.3f);
        }
        spriteBatch.draw(button, (Gdx.graphics.getWidth() / 2) - (button.getWidth() / 2), Gdx.graphics.getHeight() - button.getHeight() - 100);
        spriteBatch.setColor(c.r, c.g, c.b, 1f);


        spriteBatch.end();

    }


    public void handleInput() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();

            float x1 = Gdx.graphics.getWidth() - cancel.getWidth() - 20;
            float x2 = Gdx.graphics.getWidth() + cancel.getWidth() - 20;
            float y1 = 20;
            float y2 = cancel.getHeight() + 20;

            if (x > x1 && x < x2 && y > y1 && y < y2) {
                System.out.println("Exit pressed");
                stateManager.pop();

            }

            float x21 = (Gdx.graphics.getWidth() / 2) - (button.getWidth() / 2);
            float x22 = (Gdx.graphics.getWidth() / 2) + (button.getWidth() / 2);
            float y21 = Gdx.graphics.getHeight() - button.getHeight() - 100;
            float y22 = Gdx.graphics.getHeight() + button.getHeight() - 100;

            if (x > x21 && x < x22 && y > y21 && y < y22 && wifiDirect.isConnected()) {
//                if(wifiDirect.isServer()){
//                    MultiplayerServer multiplayer= new MultiplayerServer(game,
//                            stateManager, (float)game.getWidth(), (float)game.getHeight(), peerHelperInterface, wifiDirect);
//                    //howToPlayState.changeConnected();
//                    stateManager.push(multiplayer);
//                } else {
                    MultiplayerClient multiplayer= new MultiplayerClient(game,
                            stateManager, (float)game.getWidth(), (float)game.getHeight(), peerHelperInterface, wifiDirect);
                    //howToPlayState.changeConnected();
                    stateManager.push(multiplayer);
                game.getInput().setGameState((GameState) multiplayer);
//                }
            }
        }
    }

    public void setupPeerList(List<String> peers) {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("helvetica_bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();

        for(int i = 0; i < peers.size(); i++){
//            BitmapFont font = new BitmapFont();
//            Skin skin = new Skin();
            String name = peers.get(i);

//        skin.addRegions(buttonAtlas);
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font = font;
//        textButtonStyle.up = skin.getDrawable("up-button");
//        textButtonStyle.down = skin.getDrawable("down-button");
//        textButtonStyle.checked = skin.getDrawable("checked-button");
            TextButton button = new TextButton(name, textButtonStyle);
//            button.getLabel().setFontScale(5.0f);
            button.setX((float) ((game.getWidth() - button.getWidth()) /2 ));
            button.setY((float) (game.getHeight() * 0.7 - i * button.getHeight()* 1.5));
            stage.addActor(button);

            button.addListener(new ChangeListener() {
                @Override
                public void changed (ChangeEvent event, Actor actor) {
                    System.out.println(((TextButton)actor).getText()+" button Pressed");
                }
            });
        }
    }

    public void setupCustomInputProcessor() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(game);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }
}
